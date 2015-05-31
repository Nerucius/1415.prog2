package edu.ub.prog2.DempereGuillermoGerman.controlador;

import edu.ub.prog2.DempereGuillermoGerman.model.AlbumImatges;
import edu.ub.prog2.DempereGuillermoGerman.model.BibliotecaImatges;
import edu.ub.prog2.DempereGuillermoGerman.model.DadesVisor;
import edu.ub.prog2.DempereGuillermoGerman.model.Imatge;
import edu.ub.prog2.DempereGuillermoGerman.model.ImatgeBN;
import edu.ub.prog2.DempereGuillermoGerman.model.ImatgeSepia;
import edu.ub.prog2.DempereGuillermoGerman.vista.listener.VisorTimerListener;
import edu.ub.prog2.utils.BasicCtrl;
import edu.ub.prog2.utils.ImageFile;
import edu.ub.prog2.utils.ImageList;
import edu.ub.prog2.utils.VisorException;
import java.awt.image.BufferedImage;
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

    public boolean transformImageFromList(Imatge oldImg, ImageList list, Imatge.Type type) {
	if (type == Imatge.Type.NORMAL) return false;

	Imatge newImg = null;
	System.out.println("Transform image: " + type.toString());
	try {
	    if (type == Imatge.Type.NORMAL) newImg = new Imatge(oldImg);
	    if (type == Imatge.Type.SEPIA) newImg = new ImatgeSepia(oldImg);
	    if (type == Imatge.Type.BLACKNWHITE) newImg = new ImatgeBN(oldImg);

	    // Save new image
	    BufferedImage outBuffer = (BufferedImage)newImg.getImage();
	    String outPath = newImg.getAbsolutePath();

	    // Generate new filename for out file
	    outPath = outPath.replaceAll(newImg.getFilename(),
		    newImg.getFilename() + "_" + type.name().toLowerCase());
	    outPath = outPath.replaceAll(newImg.getExtension(), "png");

	    File outFile = new File(outPath);

	    System.out.println(outPath);

	    ImageIO.write(outBuffer, "png", outFile);

	    newImg = new Imatge(outFile.getAbsolutePath());
	    newImg.setTitle(oldImg.getTitle());

	    // Replace everywhere always
	    replaceOccurences(oldImg, newImg, data.getLib());
	    for (AlbumImatges al : data.getAlbums())
		replaceOccurences(oldImg, newImg, al);

	    /* Replace everywhere only if in library
	     if (list == data.getLib()) {
	     replaceOccurences(oldImg, newImg, data.getLib());
	     for (AlbumImatges al : data.getAlbums())
	     replaceOccurences(oldImg, newImg, al);
	     } else {
	     replaceOccurences(oldImg, newImg, list);
	     }
	     */
	    return true;
	} catch (Exception e) {
	    System.out.println("Failed to create new Image");
	    e.printStackTrace();
	    return false;
	}
    }

    /**
     * Replaced all ocurrences of oldImg inside the list by newImg, conserving indices.
     */
    private void replaceOccurences(ImageFile oldImg, ImageFile newImg, ImageList list) {
	ArrayList<ImageFile> array = list.getList();
	ArrayList<ImageFile> temp = new ArrayList<>();

	// Replace ops into temp array
	for (ImageFile img : array)
	    if (img.equals(oldImg))
		temp.add(newImg);
	    else
		temp.add(img);

	// Copy over array
	array.clear();
	array.addAll(temp);
    }

    /**
     * Method for old Visors
     */
    @Deprecated
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

    public boolean isPlaying(){
	return playing;
    }
    
    private ArrayList<Integer> play_order;

    /**
     * Begins the timer ticking
     *
     * @param numItems The number of items to tick for.
     */
    public void play(int numItems) {
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

	    this.numItems = numItems;
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
		playing = false;
		paused = false;
	    } catch (VisorException e) {
	    }
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
     * Helpful method to add an image to a list, only requiring the Image name/tile and the absolute
     * or relative path.
     */
    public boolean addImageToList(String imgName, String imgPath, ImageList list) {
	try {
	    // Create new image, set the title and add to the given list.
	    Imatge newImg = new Imatge(imgPath);
	    newImg.setTitle(imgName);
	    list.addImage(newImg);

	} catch (VisorException | IOException ex) {
	    // Image not found or file corrupted
	    System.out.println(ex.getMessage());
	    return false;
	}
	return true;
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
     * Loads a list from a file as a serialized object. Overwrites all images currently in the list.
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
	    newData = (DadesVisor)ois.readObject();

	    // Close streams
	    ois.close();
	    fis.close();

	} catch (IOException | ClassNotFoundException e) {
	    //e.printStackTrace();
	    System.out.println(e.getMessage());
	    return false;
	}

	if (newData == null) {
	    return false;
	}

	this.data = newData;
	return true;
    }

    public boolean addAlbum(String alTitle, String alAuthor, int cap) {
	// Basic property checking
	if (alTitle.length() <= 0 || alAuthor.length() <= 0 || cap <= 0)
	    return false;

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
	data.removeAlbum((AlbumImatges)currentList);
    }

}
