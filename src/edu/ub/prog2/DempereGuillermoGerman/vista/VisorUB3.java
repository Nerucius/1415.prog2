package edu.ub.prog2.DempereGuillermoGerman.vista;

import edu.ub.prog2.DempereGuillermoGerman.controlador.CtrlVisor;
import edu.ub.prog2.DempereGuillermoGerman.model.AlbumImatges;
import edu.ub.prog2.DempereGuillermoGerman.model.BibliotecaImatges;
import edu.ub.prog2.utils.ImageFile;
import edu.ub.prog2.utils.ImageList;
import java.util.Scanner;

import edu.ub.prog2.utils.Menu;
import edu.ub.prog2.utils.VisorException;
import java.io.File;
import java.util.ArrayList;

/**
 * First implementation of the image visor, console only.
 */
public class VisorUB3 implements VisorUB {

    private static final boolean autoSaveAndLoad = true;

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
        VIEW_IMG,
        TRANSFORM_IMG,
        GO_BACK,
    };

    // LibraryMenu || AlbumMenu /VIEW_IMG
    private enum ViewImageMenuOPT {

        VIEW,
        VIEW_SEPIA,
        VIEW_BNW,
        GO_BACK
    };

    // LibraryMenu || AlbumMenu /TRANSFORM_IMG
    private enum TransformImageMenuOPT {

        TRANS_SEPIA,
        TRANS_BNW,
        TRANS_NORMAL,
        GO_BACK
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
        VIEW_IMG,
        TRANSFORM_IMG,
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
        "Veure Imatge",
        "Transformar Imatge",
        "Tornar"
    };

    private final String[] viewImageMenuSTR = {
        "Visualitzar Imatge",
        "Visualitzar amb filtre Sepia",
        "Visualitzar amb filtre Blanc&Negre",
        "Tornar"
    };

    private final String[] transformImageMenuSTR = {
        "Visualitzar Imatge",
        "Visualitzar amb filtre Sepia",
        "Visualitzar amb filtre Blanc&Negre",
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
        "Veure Imatge",
        "Transformar Imatge",
        "Modificar Dades",
        "Tornar"
    };

    Menu<MainMenuOPT> mainMenu;
    Menu<LibraryMenuOPT> libraryMenu;

    Menu<ViewImageMenuOPT> viewImageMenu;
    Menu<TransformImageMenuOPT> transformImageMenu;

    Menu<AlbumsMenuOPT> albumsMenu;
    Menu<AlbumMenuOPT> albumMenu;

    public VisorUB3() {
        // Input processor
        sc = new Scanner(System.in);
        ctrl = new CtrlVisor();

        // Autoload:
        if (autoSaveAndLoad) {
            File f = new File("data.bin");
            if (f.exists()) {
                ctrl.loadDataFromObjectStream("data.bin");
                System.out.println("Data auto-carregada desde " + f.getName());
            }
        }

        // Menu setup
        mainMenu = new Menu<>("Menu Principal", MainMenuOPT.values());
        mainMenu.setDescripcions(mainMenuSTR);

        libraryMenu = new Menu<>("Biblioteca", LibraryMenuOPT.values());
        libraryMenu.setDescripcions(libraryMenuSTR);

        viewImageMenu = new Menu<>("Veure Imatges", ViewImageMenuOPT.values());
        viewImageMenu.setDescripcions(viewImageMenuSTR);

        transformImageMenu = new Menu<>("Veure Imatges", TransformImageMenuOPT.values());
        transformImageMenu.setDescripcions(transformImageMenuSTR);

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
                    if (autoSaveAndLoad) {
                        if (ctrl.saveDataToObjectStream("data.bin")) {
                            System.out.println("Data auto-guardad en data.bin");
                        } else {
                            System.out.println("Error d'auto-guardat");
                        }
                    }
                    break;

                default:
                    throw new AssertionError(opt.name());
            }
        } // End while

    }

    /// MENUS
    private void libraryMenu() {
        LibraryMenuOPT opt = null;
        ImageList lib = ctrl.getData().getLib();

        while (opt != LibraryMenuOPT.GO_BACK) {
            libraryMenu.mostrarMenu();
            opt = libraryMenu.getOpcio(sc);

            switch (opt) {
                case ADD_IMG:
                    addImageForm(lib);
                    break;
                case SHOW_LIB:
                    System.out.println("\nImatges a la Llibreria:");
                    System.out.println("-----------------------");
                    showImageList(lib);
                    System.out.println("");
                    break;
                case REMOVE_IMG:
                    removeImageForm(lib);
                    break;
                case VIEW_IMG:
                    viewImageForm(lib);
                    break;
                case TRANSFORM_IMG:
                    transformImageForm(lib);
                    break;
                case GO_BACK:
                    break;
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
                    removeImageForm(album);
                    break;
                case EDIT_ALBUM:
                    albumEditForm(album);
                    break;
                case VIEW_IMG:
                    viewImageForm(album);
                    break;
                case TRANSFORM_IMG:
                    transformImageForm(album);
                    break;
                case GO_BACK:
                    break;
            }
        }
    }

    private void viewImageForm(ImageList list) {
        System.out.println("");
        System.out.println("Selecciona la Imatge");
        System.out.println("--------------------");
        showImageList(list);

        int imgIndex = -1;
        while (imgIndex < 0 || imgIndex >= list.getSize()) {
            imgIndex = sc.nextInt() - 1;
        }

        ImageFile img = list.getAt(imgIndex);

        ViewImageMenuOPT opt = null;

        while (opt != ViewImageMenuOPT.GO_BACK) {
            viewImageMenu.mostrarMenu();

            switch (viewImageMenu.getOpcio(sc)) {
                case VIEW:
                    try {
                        System.out.println("Showing Image...");
                        img.show(GestioVisorUB.imgPanel, false);
                    } catch (Exception e) {
                        System.out.println("Failed to show");
                    }
                    break;
                case VIEW_SEPIA:
                    break;
                case VIEW_BNW:
                    break;
                case GO_BACK:
                    return;

            }
        }

    }

    private void transformImageForm(ImageList list) {

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
        ArrayList<ImageList> albums = ctrl.getData().getAlbums();

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
        ArrayList<ImageList> albums = ctrl.getData().getAlbums();

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
            }
        } else {
            // Index out of bounds
            System.out.println("La imatge no existeix");
        }
    }

    /**
     * Form to edit the metadata of an album, namely the Title and Author, as
     * well as the cover image
     */
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

    /**
     * Form to add an Image to an imagelist, doesn't matter if it's the Lib or
     * an Album.
     */
    public void addImageForm(ImageList list) {
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

    /**
     * Form to ask the player to select an image, that will be removed from
     * either an ImageList or a Library. If the target is a library it will be
     * removed from all albums aswell.
     *
     * @param list The ImageList to consider.
     */
    public void removeImageForm(ImageList list) {
        System.out.println("");
        System.out.println("Eliminar una imatge");
        System.out.println("-------------------");

        showImageList(list);

        System.out.print("Index a eliminar (-1 per sortir): ");
        int delIndex = sc.nextInt() - 1;

        // Comprobació de rang, si es valid eliminar la imatge
        if (delIndex >= 0 && delIndex < list.getSize()) {
            ImageFile removed = list.getAt(delIndex);

            // If we're removing from the library, remove from all albums
            if (list instanceof BibliotecaImatges) {
                ctrl.removeImageFromAll(removed);
            } else {
                list.removeImage(removed);
            }

            System.out.println("Imatge eliminada.");
        }
    }

    // IMAGELIST HELPER METHODS
    /**
     * Shows a list of all the albums in Albums.
     */
    private void showAlbumList() {
        int i = 1;
        for (ImageList al : ctrl.getData().getAlbums()) {
            System.out.println("\t" + i + ") " + al.toString());
            i++;
        }
    }

    /**
     * Shows an index 1 list of Images
     */
    private void showImageList(ImageList list) {
        int i = 1;
        for (ImageFile img : list.getList()) {
            System.out.println("\t" + i + ") " + img.toString());
            i++;
        }
    }

    /**
     * Form to ask the player for a file to save the data in.
     */
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
     * Form to load the Image Database from a file.
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
