package edu.ub.prog2.DempereGuillermoGerman.model;

import java.util.Date;

import edu.ub.prog2.utils.ImageFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * Class containing an Image file reference and intrinsic data about the image.
 */
public class Imatge extends ImageFile {

    public enum Type {

        NORMAL,
        SEPIA,
        BLACKNWHITE
    }

    private static final long serialVersionUID = -1816757792965439946L;

    protected String title;
    protected final Date lastModDate;
    protected final String fileName;
    protected final String filePath;
    protected String fileExt;
    protected Imatge.Type type;

    /**
     * Creates a new image from a file path. Throws IOException if image file is
     * not found.
     *
     * @param filePath The string file path to the image. Absolute or relative.
     * @throws java.io.FileNotFoundException
     */
    public Imatge(String filePath) throws FileNotFoundException {
        super(filePath);

        this.type = Type.NORMAL;

        if (!exists()) {
            throw new FileNotFoundException("Imatge no trobada");
        }

        // BufferedImage img = ImageIO.read(this);
        String[] comps = filePath.split("/");

        this.title = "default";
        this.lastModDate = new Date(this.lastModified());
        this.filePath = this.getAbsolutePath();
        this.fileName = comps[comps.length - 1];
        this.fileExt = this.fileName.split("\\.")[1].toLowerCase();
    }

    public boolean saveImageToFile(String path) throws IOException {

        return false;
    }

    @Override
    public JDialog show(boolean modal) throws IOException, Exception {
        JDialog dialog = new JDialog();
        //dialog.setUndecorated(true);
        JLabel label = new JLabel(new ImageIcon(getImage()));
        dialog.add(label);
        dialog.setModal(modal);
        dialog.pack();
        dialog.setVisible(true);
        dialog.setResizable(false);

        return dialog;
    }

    /**
     * Sets the Name/Title of the image, will be saved along the image to the
     * list of images and is used for quick identification.
     *
     * @param title String with the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns this image's title.
     *
     * @return The title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns this image's extension (jpg, png, gif...).
     *
     * @return String with the extension
     */
    @Override
    public String getExtension() {
        return this.fileExt;
    }

    /**
     * Returns the full absolute path to the image file.
     *
     * @return the full OS formatted path
     */
    @Override
    public String getFullPath() {
        return this.getAbsolutePath();
    }

    /**
     * Returns a Date object representing the last modification date of the
     * image file.
     *
     * @return a Date object representing the last modification time.
     */
    @Override
    public Date getLastModification() {
        return new Date(lastModDate.getTime());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Imatge {");
        sb.append("nom: ").append(this.title).append(", ");
        sb.append("data: ").append(this.lastModDate.toString()).append(", ");
        sb.append("nom fitxer: ").append(this.getName()).append(", ");
        sb.append("extensio: ").append(this.fileExt).append(", ");
        sb.append("Cami complet: ").append(this.filePath);
        sb.append("}");

        return sb.toString();
    }

    /**
     * Overrides the default File.equals()
     *
     * @param o Another image
     * @return True if same object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImageFile)) {
            return false;
        }

        Imatge img = (Imatge) o;
        // non-identity compare, returns true if the images have the same
        // full path
        return this.getFullPath().equalsIgnoreCase(img.getFullPath())
                && this.type.equals(img.getType());
    }

    public Imatge.Type getType() {
        return this.type;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.title);
        hash = 73 * hash + Objects.hashCode(this.lastModDate);
        hash = 73 * hash + Objects.hashCode(this.fileName);
        hash = 73 * hash + Objects.hashCode(this.filePath);
        hash = 73 * hash + Objects.hashCode(this.fileExt);
        return hash;
    }

}
