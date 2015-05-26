package edu.ub.prog2.DempereGuillermoGerman.model;

import java.util.Date;

import edu.ub.prog2.utils.ImageFile;
import edu.ub.prog2.utils.VisorException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * Class containing an Image file reference and intrinsic data about the image.
 */
public class Imatge extends ImageFile implements Serializable {

    private static final long serialVersionUID = -1816757792965439946L;

    /**
     * Transcient: Do not Serialize
     */
    protected transient BufferedImage buffer;

    //private transient BufferedImage bufferImg;
    public enum Type {

	NORMAL,
	SEPIA,
	BLACKNWHITE
    }

    protected String title;
    protected final Date lastModDate;
    protected final String fileName;
    protected final String filePath;
    protected String fileExt;
    protected Imatge.Type type;

    /**
     * Creates a new image from a file path. Throws IOException if image file is not found.
     *
     * @param filePath The string file path to the image. Absolute or relative.
     * @throws java.io.FileNotFoundException
     */
    public Imatge(String filePath) throws FileNotFoundException, VisorException {
	super(filePath);
	this.type = Type.NORMAL;

	if (!exists()) throw new FileNotFoundException("Imatge no trobada");
	if (super.getImage() == null)
	    throw new VisorException("El fitxer no es una Imatge.");

	// Split assets/imgs/filename.png into [assets, imgs, filename.png]
	String[] comps = this.getAbsolutePath().split("\\\\");

	this.title = "default";
	this.lastModDate = new Date(this.lastModified());
	this.filePath = this.getAbsolutePath();
	this.fileName = comps[comps.length - 1].split("\\.")[0]; // Split name.png into [name, png]
	this.fileExt = comps[comps.length - 1].split("\\.")[1].toLowerCase();
    }

    public Imatge(Imatge img) throws Exception {
	this(img.getAbsolutePath());
    }

    /**
     * Method that returns the BufferedImage representing this Image. checks if we have the image
     * buffered, if it's not found, read the image from disc, stores it in the buffer, and then
     * returns the buffer.<br>
     * <br>
     * <b>NOTE: The buffer returned is not a copy, but the real buffer.</b>
     */
    @Override
    public Image getImage() {
	if (buffer == null) {
	    buffer = (BufferedImage)super.getImage();
	    applyFilter();
	}
	return buffer;
    }

    /**
     * Method to be overwritten by subclasses, passing their own filters
     */
    protected void applyFilter() {
    }

    public boolean saveImageToFile(String path) throws IOException {
	return false;
    }

    /**
     * Returns a new instance of a scaled version of this image. If the image has a filter, the
     * returned image has that filter applied
     *
     * @param width
     * @param height
     * @param fit If true, the image will be scaled uniformly.
     * @return
     */
    public BufferedImage resizeImage(int width, int height, boolean fit) {
	BufferedImage inImg = (BufferedImage)this.getImage();
	// Isomorphic scaling
	if (fit) {
	    int imgW = inImg.getWidth(null);
	    int imgH = inImg.getHeight(null);
	    float ratio = (float)imgW / imgH;
	    if (imgW > imgH)
		// Image is wider than taller
		height = (int)(width / ratio);
	    else
		// Image is taller than wider
		width = (int)(height * ratio);
	}

	BufferedImage scaledImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	Graphics g = scaledImg.createGraphics();

	g.drawImage(inImg, 0, 0, width, height, null);
	g.dispose();

	return scaledImg;
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
     * Sets the Name/Title of the image, will be saved along the image to the list of images and is
     * used for quick identification.
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

    public String getFilename() {
	return this.fileName;
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
     * Returns a Date object representing the last modification date of the image file.
     *
     * @return a Date object representing the last modification time.
     */
    @Override
    public Date getLastModification() {
	return new Date(lastModDate.getTime());
    }

    public String toString() {
	StringBuilder sb = new StringBuilder();

	sb.append(title).append(" (").append(getName()).append(")");

	return sb.toString();
    }

    public String toStringLong() {
	StringBuilder sb = new StringBuilder();

	sb.append("Imatge {");
	//sb.append(type.toString());
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
	if (this == o) return true;
	if (!(o instanceof ImageFile)) return false;

	ImageFile img = (ImageFile)o;
	// non-identity compare, returns true if the images have the same path
	return this.getAbsolutePath().equalsIgnoreCase(img.getAbsolutePath());
	//&& this.type.equals(img.getType())

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
