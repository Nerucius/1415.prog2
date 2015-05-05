/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.prog2.DempereGuillermoGerman.controlador;

import edu.ub.prog2.DempereGuillermoGerman.model.BibliotecaImatges;
import edu.ub.prog2.DempereGuillermoGerman.model.DadesVisor;
import edu.ub.prog2.DempereGuillermoGerman.model.Imatge;
import edu.ub.prog2.utils.BasicCtrl;
import edu.ub.prog2.utils.ImageFile;
import edu.ub.prog2.utils.ImageList;
import edu.ub.prog2.utils.VisorException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author mat.aules
 */
public class CtrlVisor extends BasicCtrl {

    private DadesVisor data;

    public CtrlVisor() {
        this.data = new DadesVisor();
    }

    public boolean addImageToList(String imgName, String imgPath, ImageList list) {
        try {
            Imatge newImg = new Imatge(imgPath);
            newImg.setTitle(imgName);
            list.addImage(newImg);

            return true;
        } catch (FileNotFoundException | VisorException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public boolean removeImageFromList(ImageFile img, ImageList list) {
        try {
            list.removeImage(img);

            // Si eliminen de la biblioteca, eliminar a tot arreu
            if (list instanceof BibliotecaImatges) {
                for (ImageList al : getData().getAlbums()) {
                    al.removeImage(img);
                }
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
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
            return false;
        }

        if (newData == null) {
            return false;
        }

        this.data = newData;
        return true;
    }

}
