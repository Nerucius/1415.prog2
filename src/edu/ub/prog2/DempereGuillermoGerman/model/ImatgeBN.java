/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.prog2.DempereGuillermoGerman.model;

import edu.ub.prog2.utils.ImageFile;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

/**
 *
 * @author Akira
 */
public class ImatgeBN extends Imatge {

    private transient BufferedImage imageBN;

    public ImatgeBN(String filePath) throws FileNotFoundException {
        super(filePath);
        this.type = Type.BLACKNWHITE;
    }

    public ImatgeBN(ImageFile src) throws FileNotFoundException {
        this(src.getAbsolutePath());
    }

    public Image getImage(){
        if(imageBN == null)
            imageBN = applyBWFilter((BufferedImage) getImage());
        return imageBN;
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
                Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);
                image.setRGB(w, h, newColor.getRGB());
            }
        }
        
        return image;
    }


}
