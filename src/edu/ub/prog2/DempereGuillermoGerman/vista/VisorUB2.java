package edu.ub.prog2.DempereGuillermoGerman.vista;

import edu.ub.prog2.DempereGuillermoGerman.controlador.CtrlVisor;
import edu.ub.prog2.DempereGuillermoGerman.model.AlbumImatges;
import edu.ub.prog2.DempereGuillermoGerman.model.BibliotecaImatges;
import edu.ub.prog2.utils.ImageFile;
import edu.ub.prog2.utils.ImageList;
import java.util.Scanner;

import edu.ub.prog2.utils.Menu;
import edu.ub.prog2.utils.VisorException;
import java.util.ArrayList;

/**
 * First implementation of the image visor, console only.
 */
public class VisorUB2 implements VisorUB {

    private final Scanner sc;
    CtrlVisor ctrl;

    // MainMenu
    private enum MainMenuOPT {

        LIBRARY,
        ALBUM,
        SAVE_DATA,
        LOAD_DATA,
        EXIT_APP,
    };

    // LibMenu
    private enum LibraryMenuOPT {

        ADD_IMG,
        SHOW_LIB,
        REMOVE_IMG,
        GO_BACK,
    };

    // AlbumsMenu
    private enum AlbumsMenuOPT {

        ADD_ALBUM,
        SHOW_ALBUMS,
        REMOVE_ALBUM,
        ALBUM,
        GO_BACK,
    };

    // AlbumMenu
    private enum AlbumMenuOPT {

        ADD_IMG,
        SHOW_ALBUM,
        REMOVE_IMG,
        EDIT_ALBUM,
        GO_BACK,
    };

    private final String[] mainMenuSTR = {
        "Gestio de la Biblioteca",
        "Gestio dels Albums",
        "Guardar les dades a un fitxer",
        "Carregar les dades desde un fitxer",
        "Sortir"
    };

    private final String[] libraryMenuSTR = {
        "Afegir Imatge",
        "Mostrar tota la biblioteca",
        "Eliminar Imatge",
        "Tornar"
    };

    private final String[] albumsMenuSTR = {
        "Afegir Album",
        "Mostrar Albums",
        "Eliminar Album",
        "Gestionar un Album",
        "Tornar"
    };

    private final String[] albumMenuSTR = {
        "Afegir Imatge",
        "Mostrar Album",
        "Eliminar Imatge",
        "Modificar Dades",
        "Tornar"
    };

    Menu<MainMenuOPT> mainMenu;
    Menu<LibraryMenuOPT> libraryMenu;
    Menu<AlbumsMenuOPT> albumsMenu;
    Menu<AlbumMenuOPT> albumMenu;

    public VisorUB2() {
        // Input processor
        sc = new Scanner(System.in);
        ctrl = new CtrlVisor();

        // Menu setup
        mainMenu = new Menu<>("Menu Principal", MainMenuOPT.values());
        mainMenu.setDescripcions(mainMenuSTR);

        libraryMenu = new Menu<>("Biblioteca", LibraryMenuOPT.values());
        libraryMenu.setDescripcions(libraryMenuSTR);

        albumsMenu = new Menu<>("Albums", AlbumsMenuOPT.values());
        albumsMenu.setDescripcions(albumsMenuSTR);

        albumMenu = new Menu<>("Album", AlbumMenuOPT.values());
        albumMenu.setDescripcions(albumMenuSTR);

    }

    /**
     * Prints the menu and keeps asking the user for the menu options
     */
    @Override
    public void gestioVistorUB() {
        MainMenuOPT opt = null;

        while (opt != MainMenuOPT.EXIT_APP) {
            mainMenu.mostrarMenu();
            opt = mainMenu.getOpcio(sc);

            switch (opt) {
                case LIBRARY:
                    libraryMenu();
                    break;
                case ALBUM:
                    albumsMenu();
                    break;
                case SAVE_DATA:
                    this.saveDataToDiskForm();
                    break;
                case LOAD_DATA:
                    this.loadDataFromDiskForm();
                    break;
                case EXIT_APP:
                    System.out.println("\nSortint de l'aplicacio...");
                    break;

                default:
                    throw new AssertionError(opt.name());
            }
        } // End while

    }

    /// MENUS
    private void libraryMenu() {
        LibraryMenuOPT opt = null;

        while (opt != LibraryMenuOPT.GO_BACK) {
            libraryMenu.mostrarMenu();
            opt = libraryMenu.getOpcio(sc);

            switch (opt) {
                case ADD_IMG:
                    imageAddForm(ctrl.getData().getLib());
                    break;
                case SHOW_LIB:
                    System.out.println("\nImatges a la Llibreria:");
                    System.out.println("-----------------------");
                    showImageList(ctrl.getData().getLib());
                    System.out.println("");
                    break;
                case REMOVE_IMG:
                    imageRemoveForm(ctrl.getData().getLib());
                    break;
                case GO_BACK:
                    break;

                default:
                    throw new AssertionError(opt.name());

            }
        }
    }

    private void albumsMenu() {
        AlbumsMenuOPT opt = null;

        while (opt != AlbumsMenuOPT.GO_BACK) {
            albumsMenu.mostrarMenu();
            opt = albumsMenu.getOpcio(sc);

            switch (opt) {
                case ADD_ALBUM:
                    addAlbumForm();
                    break;
                case SHOW_ALBUMS:
                    System.out.println("Llista d'albums");
                    System.out.println("---------------");
                    showAlbumList();
                    break;
                case REMOVE_ALBUM:
                    removeAlbumForm();
                    break;
                case ALBUM:
                    selectAlbumForm();
                    break;
                case GO_BACK:
                    break;
            }
        }
    }

    private void albumMenu(ImageList list) {
        AlbumMenuOPT opt = null;

        if (!(list instanceof AlbumImatges)) {
            System.out.println("Hi ha hagut un error.");
            return;
        }
        AlbumImatges album = (AlbumImatges) list;

        while (opt != AlbumMenuOPT.GO_BACK) {
            System.out.println("");
            System.out.println("-----------------------------");
            System.out.println("TITOL: " + album.getTitle());
            System.out.println("AUTOR: " + album.getAuthor());
            albumMenu.mostrarMenu();
            opt = albumMenu.getOpcio(sc);

            switch (opt) {
                case ADD_IMG:
                    imageAddToAlbumForm(album);
                    break;
                case SHOW_ALBUM:
                    System.out.println("\nImatges a l'album:");
                    System.out.println("------------------");
                    showImageList(album);
                    System.out.println("");
                    break;
                case REMOVE_IMG:
                    imageRemoveForm(album);
                    break;
                case EDIT_ALBUM:
                    albumEditForm(album);
                    break;
                case GO_BACK:
                    break;
            }
        }
    }

    // ALBUM METHODS
    private void addAlbumForm() {
        System.out.println("");
        System.out.println("Formulari de nou Album");
        System.out.println("----------------------");

        System.out.print("Nom del album: ");
        String alTitle = sc.nextLine();
        System.out.print("Autor del album: ");
        String alAuthor = sc.nextLine();
        System.out.print("Numero maxim d'imatges: ");
        int alCap = sc.nextInt();

        AlbumImatges album = new AlbumImatges(Math.min(1, alCap));
        album.setTitle(alTitle);
        album.setAuthor(alAuthor);
        ctrl.getData().addAlbum(album);

        System.out.println("\nAlbum afegit correctament.");
    }

    private void removeAlbumForm() {
        System.out.println("");
        System.out.println("Eliminar un album");
        System.out.println("-----------------");
        ArrayList<AlbumImatges> albums = ctrl.getData().getAlbums();

        if (albums.size() <= 0) {
            System.out.println("No hi ha albums.");
            return;
        }

        showAlbumList();
        System.out.print("Numero del album: ");
        int alIndex = sc.nextInt() - 1;

        if (alIndex >= 0 && alIndex < albums.size()) {
            ctrl.getData().removeAlbum(albums.get(alIndex));
        } else {
            System.out.println("No existeix l'album.");
        }
    }

    private void selectAlbumForm() {
        System.out.println("");
        System.out.println("Selecciona un album");
        System.out.println("-------------------");
        ArrayList<AlbumImatges> albums = ctrl.getData().getAlbums();

        if (albums.size() <= 0) {
            System.out.println("No hi ha albums.");
            return;
        }

        showAlbumList();
        System.out.print("Numero del album: ");
        int alIndex = sc.nextInt() - 1;

        if (alIndex >= 0 && alIndex < albums.size()) {
            albumMenu(albums.get(alIndex));
        } else {
            System.out.println("No existeix l'album.");
        }
    }

    private void imageAddToAlbumForm(ImageList list) {
        if (!(list instanceof AlbumImatges)) {
            System.out.println("Hi ha hagut un error.");
            return;
        }
        AlbumImatges album = (AlbumImatges) list;

        System.out.println("");
        System.out.println("Afegir una imatge al album");
        System.out.println("--------------------------");

        ImageList lib = ctrl.getData().getLib();

        System.out.println("\nImatges de la biblioteca: ");
        showImageList(lib);
        System.out.print("Imatge a afegir: ");
        int imageIndex = sc.nextInt() - 1;

        if (imageIndex >= 0 && imageIndex < lib.getSize()) {
            // AddImage throws an exception if album is full, so
            // needs a try-catch
            try {
                album.addImage(lib.getAt(imageIndex));
            } catch (VisorException e) {
                System.out.println("L'album esta ple.");
                return;
            }
        } else {
            // Index out of bounds
            System.out.println("La imatge no existeix");
        }
    }

    private void albumEditForm(AlbumImatges album) {
        System.out.println("");
        System.out.println("Editar Album");
        System.out.println("------------");

        System.out.print("Nou titol del album: ");
        album.setTitle(sc.nextLine());
        System.out.print("Nou autor del album: ");
        album.setAuthor(sc.nextLine());

        System.out.println("\nNova imatge de portada: ");
        showImageList(album);
        System.out.print("Numero: ");
        int newCoverIndex = sc.nextInt() - 1;

        if (newCoverIndex >= 0 && newCoverIndex < album.getSize()) {
            album.setCover(album.getAt(newCoverIndex));
        } else {
            System.out.println("La imatge no existeix");
        }

    }

    public void imageAddForm(ImageList list) {
        System.out.println("");
        System.out.println("Afegir una imatge");
        System.out.println("-----------------");

        System.out.print("Nom/Titol de la imatge: ");
        String imgName = sc.nextLine();
        System.out.print("Fitxer de la imatge: ");
        String imgPath = sc.nextLine();

        if (ctrl.addImageToList(imgName, imgPath, list)) {
            System.out.println("Imatge afegida correctament.");
        } else {
            System.out.println("Error al afegir la imatge.");
        }
    }

    public void imageRemoveForm(ImageList list) {

        System.out.println("");
        System.out.println("Eliminar una imatge");
        System.out.println("-------------------");

        showImageList(list);

        System.out.print("Index a eliminar (-1 per sortir): ");
        int delIndex = sc.nextInt() - 1;

        // Comprobació de rang, si es valid eliminar la imatge
        if (delIndex >= 0 && delIndex < list.getSize()) {
            ImageFile removed = list.getAt(delIndex);
            list.removeImage(removed);
            
            // Si eliminen de la biblioteca, eliminar a tot arreu
            if(list instanceof BibliotecaImatges){
                for(ImageList al : ctrl.getData().getAlbums())
                    al.removeImage(removed);
            }
            
            System.out.println("Imatge eliminada.");
        }
    }

    // IMAGELIST HELPER METHODS
    private void showAlbumList() {
        int i = 1;
        for (ImageList al : ctrl.getData().getAlbums()) {
            System.out.println("\t" + i + ") " + al.toString());
            i++;
        }
    }

    private void showImageList(ImageList list) {
        int i = 1;
        for (ImageFile img : list.getList()) {
            System.out.println("\t" + i + ") " + img.toString());
            i++;
        }
    }

    public void saveDataToDiskForm() {
        System.out.println("");
        System.out.println("Guardar la llista");
        System.out.println("-----------------");

        System.out.print("Directori i nom del fitxer on guardar: ");
        String filePath = sc.nextLine();

        if (ctrl.saveDataToObjectStream(filePath)) {
            System.out.println("Dades del programa guardades correctament.");
        } else {
            System.out.println("Hi ha hagut un error, torna a provar.");
        }
    }

    /**
     * Form to load the ImageTable from a file
     */
    public void loadDataFromDiskForm() {

        System.out.println("");
        System.out.println("Carregar una Llista");
        System.out.println("-------------------");

        System.out.print("Directori i nom del fitxer desde on carregar: ");
        String filePath = sc.nextLine();

        if (ctrl.loadDataFromObjectStream(filePath)) {
            System.out.println("Dades del programa carregades correctament.");
        } else {
            System.out.println("Hi ha hagut un error, torna a provar.");
        }

    }

}
