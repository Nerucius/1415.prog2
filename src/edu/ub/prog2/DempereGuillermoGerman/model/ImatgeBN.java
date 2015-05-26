package edu.ub.prog2.DempereGuillermoGerman.model;

import edu.ub.prog2.utils.ImageFile;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

/**
 *
 * @author Akira
 */
public class ImatgeBN extends Imatge {

    public ImatgeBN(String filePath) throws FileNotFoundException {
        super(filePath);
        this.type = Type.BLACKNWHITE;
    }

    public ImatgeBN(ImageFile src) throws FileNotFoundException {
        this(src.getAbsolutePath());
    }

    @Override
    protected void applyFilter(){
	ImatgeBN.applyBWFilter(buffer);
    }

    /** Applies a Black&White filter onto an existing buffered image.
     * 
     * @param image The BufferedImage to alter.
     * @return the finished image. Please note that the original reference is
     * also modified.
     */
    public static BufferedImage applyBWFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                Color c = new Color(image.getRGB(w, h));
                int red = (int) (c.getRed() * 0.2126);
                int green = (int) (c.getGreen() * 0.7152);
                int blue = (int) (c.getBlue() * 0.0722);
                Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue, c.getAlpha());
                image.setRGB(w, h, newColor.getRGB());
            }
        }
        
        return image;
    }
    
    @Override
    public String toString(){
	return "BN | "+super.toString();
    }


}
