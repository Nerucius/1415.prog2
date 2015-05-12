package edu.ub.prog2.DempereGuillermoGerman.controlador;

import edu.ub.prog2.DempereGuillermoGerman.model.AlbumImatges;
import edu.ub.prog2.DempereGuillermoGerman.model.BibliotecaImatges;
import edu.ub.prog2.DempereGuillermoGerman.model.DadesVisor;
import edu.ub.prog2.DempereGuillermoGerman.model.Imatge;
import edu.ub.prog2.DempereGuillermoGerman.model.ImatgeBN;
import edu.ub.prog2.DempereGuillermoGerman.model.ImatgeSepia;
import edu.ub.prog2.DempereGuillermoGerman.vista.Listener.VisorTimerListener;
import edu.ub.prog2.utils.BasicCtrl;
import edu.ub.prog2.utils.ImageFile;
import edu.ub.prog2.utils.ImageList;
import edu.ub.prog2.utils.VisorException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author mat.aules
 */
public class CtrlVisor extends BasicCtrl {

    private DadesVisor data;
    private ArrayList<VisorTimerListener> listeners;

    public CtrlVisor() {
        this.data = new DadesVisor();
        listeners = new ArrayList<VisorTimerListener>();
    }

    /**
     * Method to remove from all albums and finally from the library
     */
    public boolean removeImageFromAll(ImageFile img) {
        try {
            for (ImageList al : getData().getAlbums()) {
                al.removeImage(img);
            }
            getData().getLib().removeImage(img);
        } catch (Exception e) {
            return false;
        }

        return true;

    }

    public boolean transformImage(ImageFile img, ImageList list, Imatge.Type type) {
        Imatge newImg = null;

        try {
            // Create new Image
            switch (type) {
                case NORMAL:
                    newImg = new Imatge(img.getAbsolutePath());
                    break;
                case SEPIA:
                    newImg = new ImatgeSepia(img);
                    break;
                case BLACKNWHITE:
                    newImg = new ImatgeBN(img);
                    break;
            }
            // remove old and add new to list
            // in the case of a library, remove from all albums as well
            if (list instanceof BibliotecaImatges) {
                removeImageFromAll(img);
            } else {
                list.removeImage(img);
            }

            list.addImage(newImg);

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private int numItems;
    private int cIndex;
    private boolean playing = false;
    private boolean paused = false;

    /**
     * Adds a new listener to the timer scheduling events.
     */
    public void addTimerListener(VisorTimerListener ls) {
        listeners.add(ls);
    }

    /**
     * Begins the timer ticking
     */
    public void play(int nItems) {
        if (playing) {
            return;
        }
        if (!paused) {
            cIndex = 0;
        }

        if (!paused) {
            // Start code
            for (VisorTimerListener listener : listeners) {
                listener.onStart();
            }

            numItems = nItems;
            playing = true;
            // Call the first tick inmediately
            onTimer();

        } else {
            // Resume code
            for (VisorTimerListener listener : listeners) {
                listener.onResume();
            }

            paused = false;
        }

        try {
            this.startTimer();
        } catch (VisorException e) {
        }
    }

    /**
     * Pause the current timer. Does not reset the index.
     */
    public void pause() {
        if (!playing || paused) {
            return;
        }
        paused = true;

        for (VisorTimerListener listener : listeners) {
            listener.onPause();
        }

        try {
            this.stopTimer();
            this.playing = false;
        } catch (VisorException e) {
        }
    }

    /**
     * Stops the timer and resets the index
     */
    public void stop() {
        if (paused) paused = false;
        this.playing = false;
        
        for (VisorTimerListener listener : listeners) {
            listener.onStop();
        }
        
        try {
            stopTimer();
        } catch (VisorException e) {
        }

    }

    /**
     * Is called every timer tick by the time.
     */
    @Override
    public void onTimer() {
        for (VisorTimerListener listener : listeners) {
            listener.onTimer(cIndex);
        }

        cIndex++;
        // Finish status
        if (cIndex == numItems) {
            for (VisorTimerListener listener : listeners) {
                listener.onFinish();
            }
            try {
                stopTimer();
                playing =  false;
                paused = false;
            }catch(VisorException e){}
        }
    }

    /**
     * Change the timer value on the fly.
     */
    public void changeTimer(int ms) {
        setTimer(ms);
        if (playing) {
            try {
                startTimer();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Helpful method to add an image to a list, only requiring the Image
     * name/tile and the absolute or relative path.
     */
    public boolean addImageToList(String imgName, String imgPath, ImageList list) {
        try {
            Imatge newImg = new Imatge(imgPath);
            if (ImageIO.read(new File(imgPath)) == null) {
                return false;
            }

            newImg.setTitle(imgName);
            list.addImage(newImg);

            return true;
        } catch (VisorException | IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public DadesVisor getData() {
        return data;
    }

    /**
     * Saves the current list to a file as a serialized object.
     *
     * @param filePath path of the file containing the list
     * @return Operation success status
     */
    public boolean saveDataToObjectStream(String filePath) {
        FileOutputStream fos;
        ObjectOutputStream oos;

        try {
            // Write data
            fos = new FileOutputStream(filePath, false);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(data);

            // Close streams
            oos.close();
            fos.close();

        } catch (IOException e) {
            return false;
        }

        return true;

    }

    /**
     * Loads a list from a file as a serialized object. Overwrites all images
     * currently in the list.
     *
     * @param filePath path of the file containing the list
     * @return Operation success status
     */
    public boolean loadDataFromObjectStream(String filePath) {
        FileInputStream fis;
        ObjectInputStream ois;
        DadesVisor newData = null;

        try {
            // Read data
            fis = new FileInputStream(filePath);
            ois = new ObjectInputStream(fis);
            newData = (DadesVisor) ois.readObject();

            // Close streams
            ois.close();
            fis.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        if (newData == null) {
            return false;
        }

        this.data = newData;
        return true;
    }

    public boolean addAlbum(String alTitle, String alAuthor, int cap) {
        AlbumImatges album = new AlbumImatges(cap);
        album.setTitle(alTitle);
        album.setAuthor(alAuthor);

        if (data.getAlbums().contains(album)) {
            return false;
        }

        data.addAlbum(album);
        return true;
    }

    public void removeAlbum(ImageList currentList) {
        data.removeAlbum((AlbumImatges) currentList);
    }

}
