package edu.ub.prog2.DempereGuillermoGerman.model;

import edu.ub.prog2.utils.ImageFile;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

/**
 *
 * @author Akira
 */
public class ImatgeSepia extends Imatge {


    public ImatgeSepia(String filePath) throws FileNotFoundException {
	super(filePath);
	this.type = Type.SEPIA;
    }

    public ImatgeSepia(ImageFile src) throws FileNotFoundException {
	this(src.getAbsolutePath());
    }
    
    @Override
    protected void applyFilter(){
	ImatgeSepia.applySepiaFilter(buffer);
    }

    public static BufferedImage applySepiaFilter(BufferedImage image) {
	int width = image.getWidth();
	int height = image.getHeight();

	int r, g, b, red, green, blue, alpha;

	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		Color c = new Color(image.getRGB(x, y));
		red = (int)(c.getRed());
		green = (int)(c.getGreen());
		blue = (int)(c.getBlue());
		alpha = (int)c.getAlpha();

		r = (int)((red * 0.393) + (green * 0.769) + (blue * 0.189));
		g = (int)((red * 0.349) + (green * 0.686) + (blue * 0.168));
		b = (int)((red * 0.272) + (green * 0.534) + (blue * 0.131));
		r = Math.min(r, 255);
		g = Math.min(g, 255);
		b = Math.min(b, 255);

		Color newColor = new Color(r, g, b, alpha);
		image.setRGB(x, y, newColor.getRGB());
	    }
	}
	
	return image;
    }

    @Override
    public String toString() {
	return "SEPIA | " + super.toString();
    }
}
