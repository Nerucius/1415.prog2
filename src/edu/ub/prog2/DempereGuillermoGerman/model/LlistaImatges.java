package edu.ub.prog2.DempereGuillermoGerman.model;

import edu.ub.prog2.utils.ImageFile;
import edu.ub.prog2.utils.ImageList;
import edu.ub.prog2.utils.VisorException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Second implementation of ImageList, this time using an ArrayList as the
 * backing array for the images.
 */
public class LlistaImatges extends ImageList implements Serializable {

    private static final long serialVersionUID = 516176151684615L;
    protected int maxCap;

    public LlistaImatges(int maxCap) {
        super();
        super.setList(new ArrayList<ImageFile>(maxCap));
        
        this.maxCap = maxCap;
    }

    @Override
    public int getSize() {
        return getList().size();
    }

    @Override
    public void addImage(ImageFile img) throws VisorException {
        if (!isFull()) {
            getList().add(img);
        } else {
            throw new VisorException("La llista esta plena.");
        }
    }

    @Override
    public void removeImage(ImageFile image) {
        Iterator<ImageFile> iter = getList().iterator();
        // This version actually works better than the previous one in TaulaImatges
        // since it actually checks for all of the items in the array, and removes
        // all matches, and not the first.

        // Either way, if the addImage() method in the Controller does it's job
        // correctly, not allowing duplicate images, this is mostly just for show.
        while (iter.hasNext()) {
            if (image.equals(iter.next())) {
                iter.remove();
            }
        }

    }
    
    public int indexOf(ImageFile img){
        return getList().indexOf(img);
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
        return getList().size() >= maxCap;
    }

    @Override
    public String toString() {
        int count = 1;

        StringBuilder sb = new StringBuilder();

        sb.append("Llista Fitxers\n")
                .append("==============\n\n");

        for (ImageFile img : getList()) {
            if (img == null) {
                continue;
            }

            sb.append("  [")
                    .append(count)
                    .append("]  ")
                    .append(img.toString())
                    .append("\n");
            count++;
        }

        return sb.toString();
    }

}
