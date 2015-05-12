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
public class ImatgeSepia extends Imatge {

    private transient BufferedImage imageSepia;

    public ImatgeSepia(String filePath) throws FileNotFoundException {
        super(filePath);
        this.type = Type.SEPIA;
        this.imageSepia = color2sepia();
    }

    public ImatgeSepia(ImageFile src) throws FileNotFoundException {
        this(src.getAbsolutePath());
    }

    @Override
    public Image getImage() {
        if(imageSepia == null)
            imageSepia = applySepiaFilter((BufferedImage) getImage());
        return imageSepia;
    }

    public static BufferedImage applySepiaFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color c = new Color(image.getRGB(j, i));
                int red = (int) (c.getRed());
                int green = (int) (c.getGreen());
                int blue = (int) (c.getBlue());
                int r = (int) ((red * 0.393) + (green * 0.769) + (blue * 0.189));
                int g = (int) ((red * 0.349) + (green * 0.686) + (blue * 0.168));
                int b = (int) ((red * 0.272) + (green * 0.534) + (blue * 0.131));
                r = Math.min(r, 255);
                g = Math.min(g, 255);
                b = Math.min(b, 255);
                Color newColor = new Color(r, g, b);
                image.setRGB(j, i, newColor.getRGB());
            }
        }

        return image;
    }


    private BufferedImage color2sepia() {
        BufferedImage inBufferedImage = (BufferedImage) getImage();
        int width = inBufferedImage.getWidth();
        int height = inBufferedImage.getHeight();
        BufferedImage outImage = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color c = new Color(inBufferedImage.getRGB(j, i));
                int red = (int) (c.getRed());
                int green = (int) (c.getGreen());
                int blue = (int) (c.getBlue());
                int r = (int) ((red * 0.393) + (green * 0.769) + (blue * 0.189));
                int g = (int) ((red * 0.349) + (green * 0.686) + (blue * 0.168));
                int b = (int) ((red * 0.272) + (green * 0.534) + (blue * 0.131));
                r = Math.min(r, 255);
                g = Math.min(g, 255);
                b = Math.min(b, 255);
                Color newColor = new Color(r, g, b);
                outImage.setRGB(j, i, newColor.getRGB());
            }
        }
        return (outImage);
    }


}
