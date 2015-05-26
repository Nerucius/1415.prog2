package edu.ub.prog2.DempereGuillermoGerman.model;

import edu.ub.prog2.utils.ImageFile;
import edu.ub.prog2.utils.VisorException;

/**
 * Classe del album d'imatges. Manté un numero limitat de referencies (repetides
 * o no) que provenen de la llibreria.
 * 
 * Te una imatge que s'usa com a portada.
 *
 * @author German
 */
public class AlbumImatges extends LlistaImatges {

    private ImageFile cover;
    private String title;
    private String author;

    public AlbumImatges(int maxCap) {
        super(maxCap);

        this.cover = null;
        this.title = "Default Title";
        this.author = "Default Author";
    }

    public AlbumImatges() {
        this(10);
    }

    @Override
    public void addImage(ImageFile img) throws VisorException {
        super.addImage(img);
        // Si es la primera imatge que s'afegeix, sera la nova
        // caratula
        if (getList().size() == 1) {
            this.cover = getAt(0);
        }
    }

    @Override
    public void removeImage(ImageFile image) {
        super.removeImage(image);
        // Si ens carregem la caratula, posarla a null
        if (cover.equals(image)) {
            this.cover = null;
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

    @Override
    public String toString() {
        return "Album: " + getTitle() + ", creat per " + getAuthor() + ".";
    }

    public boolean equals(Object o) {
        if (!(o instanceof AlbumImatges)) 
            return false;
    
        AlbumImatges al = (AlbumImatges) o;
        
        return this.title.equalsIgnoreCase(al.getTitle());
    }

    public int getCap() {
	return maxCap;
    }
    
    /** Sets the cap to match either the given one, or the current size of the album */
    public void setCap(int cap){
	this.maxCap = Math.max(this.getSize(), cap);	
    }
}
