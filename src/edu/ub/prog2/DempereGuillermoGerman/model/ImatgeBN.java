/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.prog2.DempereGuillermoGerman.model;

import edu.ub.prog2.utils.ImageFile;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 *
 * @author Akira
 */
public class ImatgeBN extends Imatge {

    private transient BufferedImage imageBN;

    public ImatgeBN(String filePath) throws FileNotFoundException {
        super(filePath);
        this.type = Type.BLACKNWHITE;
        this.imageBN = color2gray();
    }

    public ImatgeBN(ImageFile src) throws FileNotFoundException {
        super(src.getAbsolutePath());
    }

    @Override
    public JDialog show(boolean modal) throws IOException, Exception {
        if (this.imageBN == null) {
            this.imageBN = color2gray();
        }

        JDialog dialog = new JDialog();
        //dialog.setUndecorated(true);
        JLabel label = new JLabel(new ImageIcon(imageBN));
        dialog.add(label);
        dialog.setModal(modal);
        dialog.pack();
        dialog.setVisible(true);
        dialog.setResizable(false);

        return dialog;
    }

    private BufferedImage color2gray() {
        BufferedImage inBufferedImage = (BufferedImage) getImage();
        int width = inBufferedImage.getWidth();
        int height = inBufferedImage.getHeight();
        BufferedImage outImage = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color c = new Color(inBufferedImage.getRGB(j, i));
                int red = (int) (c.getRed() * 0.2126);
                int green = (int) (c.getGreen() * 0.7152);
                int blue = (int) (c.getBlue() * 0.0722);
                Color newColor = new Color(red + green + blue, red + green + blue, red + green + blue);
                outImage.setRGB(j, i, newColor.getRGB());
            }
        }

        return outImage;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ImatgeBN {");
        sb.append("nom: ").append(this.title).append(", ");
        sb.append("data: ").append(this.lastModDate.toString()).append(", ");
        sb.append("nom fitxer: ").append(this.getName()).append(", ");
        sb.append("extensio: ").append(this.fileExt).append(", ");
        sb.append("Cami complet: ").append(this.filePath);
        sb.append("}");

        return sb.toString();
    }

}
