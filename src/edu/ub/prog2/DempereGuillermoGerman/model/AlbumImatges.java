package edu.ub.prog2.DempereGuillermoGerman.model;

import edu.ub.prog2.utils.ImageFile;
import edu.ub.prog2.utils.ImageList;
import edu.ub.prog2.utils.VisorException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Classe del album d'imatges. Guarda imatges repetides o no en una llista de
 * capacitat donada per parametre en el constructor.
 *
 * Te una imatge que s'usa com a portada.
 *
 * @author German
 */
public class AlbumImatges extends ImageList {

    private final int maxCap;
    private ImageFile cover;
    private String title;
    private String author;

    public AlbumImatges(int maxCap) {
        super();
        setList(new ArrayList<ImageFile>(maxCap));

        this.maxCap = maxCap;
        this.cover = null;
        this.title = "Default Title";
        this.author = "Default Author";
    }

    public AlbumImatges() {
        this(10);
    }

    @Override
    public void addImage(ImageFile i) throws VisorException {
        if (getList().size() <= maxCap) {
            getList().add(i);

            // Si a la llista hi ha nomes una imatge, posarla com a cover;
            if (getList().size() == 1) {
                this.cover = this.getAt(0);
            }

        } else {
            throw new VisorException("Album ple.");
        }

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
        // Si a la llista hi ha nomes una imatge, posarla com a cover;
        if (getList().size() == 1) {
            this.cover = this.getAt(0);
        }

    }

    // ALBUM METHODS
    public ImageFile getCover() {
        return this.cover;
    }

    public void setCover(ImageFile cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    // IMAGELIST METHODS
    @Override
    public int getSize() {
        return getList().size();
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
        return "[Album] " + this.getTitle() + ", creat per " + this.getAuthor() + ".";
    }

}
