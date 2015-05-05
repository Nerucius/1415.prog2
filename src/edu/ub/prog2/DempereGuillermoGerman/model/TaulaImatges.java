package edu.ub.prog2.DempereGuillermoGerman.model;

import edu.ub.prog2.utils.ImageFile;
import edu.ub.prog2.utils.InImageList;

/**
 * Class implementing the ImageList interface, manages a native array containing
 * Images, and allows for easy insertion, deletion and clearing of the array.
 */
@Deprecated
public class TaulaImatges implements InImageList {

    private static final long serialVersionUID = 3017607517201416001L;

    ImageFile[] array;
    int cInd; // Current largest empty index
    int maxCap; // Max list capacity
    boolean resizeArray;

    /**
     * Creates a new image table with the initial capacity set to the parameter
     * given.
     *
     * @param initialCap The initial capacity of the array.
     * @param resize Whether to resize or not the array on overflow.
     */
    public TaulaImatges(int initialCap, boolean resize) {
        array = new Imatge[initialCap];
        this.cInd = 0;
        this.maxCap = initialCap;
        this.resizeArray = resize;
    }

    /**
     * Creates a new image table with the initial capacity set to the parameter
     * given. By default the table will use auto-resize.
     *
     * @param initialCap The initial capacity of the array.
     */
    public TaulaImatges(int initialCap) {
        this(initialCap, true);
    }

    @Override
    public void addImage(ImageFile img) {
        if (cInd == maxCap) { // No using isFull() cause it takes into account resizability
            if (!resizeArray) {
                throw new ArrayStoreException("Array is full");
            }

            // Resize the array to fit new elements
            int newCap = (int) (maxCap * 1.5f) + 1;
            Imatge[] newArray = new Imatge[newCap];

            System.arraycopy(array, 0, newArray, 0, maxCap);
            this.maxCap = newCap;
            this.array = newArray;
            // System.out.println("Array resized to new size: " + newCap);
        }

        array[cInd] = (Imatge) img;
        cInd++;
    }

    /**
     * Removes all the items in the table
     */
    @Override
    public void clear() {
        for (int i = 0; i < cInd; i++) {
            array[i] = null;
        }
        cInd = 0;
    }

    /**
     * Returns a reference to the image at index i
     *
     * @param i index to return
     * @return ImageFile at index n
     */
    @Override
    public ImageFile getAt(int i) {
        return array[i];

    }

    /**
     * Returs the current number of objects in the table
     *
     * @return Size of the table
     */
    @Override
    public int getSize() {
        return cInd;
    }

    /**
     * Returns True if the table is full and it can't be resized
     *
     * @return True if table is full
     */
    @Override
    public boolean isFull() {
        return cInd == maxCap && !resizeArray;
    }

    /**
     * Removes an image by reference, rearanges the array so the blank is
     * filled.
     *
     * @param srcImg The image to remove
     */
    @Override
    public void removeImage(ImageFile srcImg) throws ArrayStoreException {
        Imatge img = (Imatge) srcImg;

        // For each filled index in the array
        for (int i = 0; i < cInd; i++) {

            // If there is a match
            if (array[i].equals(img)) {
                // That element will be set to null
                array[i] = null;

                // And if it was NOT the last element, swap them
                if (!(i == cInd - 1)) {
                    array[i] = array[cInd - 1];
                    array[cInd - 1] = null;
                }
                // Decrement the current index
                cInd--;
                return;
            }
        }

        throw new ArrayStoreException("Image not found in list");
    }

    @Override
    public String toString() {
        int count = 1;

        StringBuilder sb = new StringBuilder();

        sb.append("Llista Fitxers\n")
                .append("==============\n\n");

        for (ImageFile img : array) {
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
