package edu.ub.prog2.DempereGuillermoGerman.model;

import edu.ub.prog2.utils.ImageFile;
import edu.ub.prog2.utils.ImageList;
import edu.ub.prog2.utils.VisorException;
import java.util.ArrayList;
import java.util.Iterator;

public class BibliotecaImatges extends ImageList {

    public BibliotecaImatges() {
        super();
        setList(new ArrayList<ImageFile>());
    }

    @Override
    public int getSize() {
        return getList().size();

    }

    @Override
    public void addImage(ImageFile image) throws VisorException {
        // Throw an exception if the image is already in the library
        Iterator<ImageFile> iter = getList().iterator();
        while (iter.hasNext()) {
            // equals() is defined in Imatge.java
            if (image.equals(iter.next())) {
                throw new VisorException("Imatge duplicada");
            }
        }
        
        getList().add(image);
    }

    @Override
    public void removeImage(ImageFile image) {
        Iterator<ImageFile> iter = getList().iterator();
        
        // Removes all matching elements in the ArrayList
        while (iter.hasNext()) {
            if (image.equals(iter.next())) {
                iter.remove();
            }
        }

    }

    @Override
    public ImageFile getAt(int i) {
        return getList().get(i);
    }

    @Override
    public void clear() {
        getList().clear();
    }

    @Override
    public boolean isFull() {
        return false;
    }

}
