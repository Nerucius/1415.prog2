package edu.ub.prog2.DempereGuillermoGerman.model;

import edu.ub.prog2.utils.ImageFile;
import edu.ub.prog2.utils.ImageList;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Second implementation of ImageList, this time using an ArrayList as the
 * backing array for the images.
 */
public class LlistaImatges extends ImageList {

    private static final long serialVersionUID = 516176151684615L;

    private final ArrayList<ImageFile> list;
    private final int maxCap;

    public LlistaImatges(int maxCap) {
        this.maxCap = maxCap;
        list = new ArrayList<>(maxCap);
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public void addImage(ImageFile i) {
        if (!isFull()) {
            list.add(i);
        } else {
            throw new ArrayStoreException("List is full");
        }
    }

    @Override
    public void removeImage(ImageFile image) {
        Iterator<ImageFile> iter = list.iterator();
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

    @Override
    public ImageFile getAt(int i) {
        if (list.contains(list.get(i))) {
            return list.get(i);
        } else {
            return null;
        }

    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean isFull() {
        return list.size() == maxCap;
    }

    @Override
    public String toString() {
        int count = 1;

        StringBuilder sb = new StringBuilder();

        sb.append("Llista Fitxers\n")
                .append("==============\n\n");

        for (ImageFile img : list) {
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
