package edu.ub.prog2.DempereGuillermoGerman.model;

import edu.ub.prog2.utils.ImageList;
import java.io.Serializable;
import java.util.ArrayList;

public class DadesVisor implements Serializable {

    private final BibliotecaImatges lib;
    private final ArrayList<AlbumImatges> albums;

    public DadesVisor() {
        lib = new BibliotecaImatges();
        albums = new ArrayList<AlbumImatges>();
    }

    public BibliotecaImatges getLib() {
        return this.lib;
    }

    public void addAlbum(AlbumImatges album){
        this.albums.add(album);
    }
    
    public void removeAlbum(AlbumImatges album){
        albums.remove(album);
    }
    
    public ArrayList<AlbumImatges> getAlbums() {
        return this.albums;
    }
    
    public ImageList getAlbum(int index) {
        return this.albums.get(index);
    }

}
