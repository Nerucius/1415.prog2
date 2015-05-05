package edu.ub.prog2.DempereGuillermoGerman.model;

import edu.ub.prog2.utils.ImageList;
import java.io.Serializable;
import java.util.ArrayList;

public class DadesVisor implements Serializable {

    private final ImageList lib;
    private final ArrayList<ImageList> albums;

    public DadesVisor() {
        lib = new BibliotecaImatges();
        albums = new ArrayList<ImageList>();
    }

    public ImageList getLib() {
        return this.lib;
    }

    public void addAlbum(ImageList album){
        this.albums.add(album);
    }
    
    public void removeAlbum(ImageList album){
        albums.remove(album);
    }
    
    public ArrayList<ImageList> getAlbums() {
        return this.albums;
    }
    
    public ImageList getAlbum(int index) {
        return this.albums.get(index);
    }

}
