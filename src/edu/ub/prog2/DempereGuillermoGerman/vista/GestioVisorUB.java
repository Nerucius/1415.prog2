package edu.ub.prog2.DempereGuillermoGerman.vista;

import edu.ub.prog2.DempereGuillermoGerman.model.Imatge;
import edu.ub.prog2.DempereGuillermoGerman.model.ImatgeSepia;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Main class
 */
public class GestioVisorUB {

    /**
     * Program entry point
     *
     * @param args Optional arguments
     */
    public static void main(String... args) throws Exception {

	VisorUB4 vista = new VisorUB4();
	vista.setVisible(true);

	//JFrame test = new FrmAfegirImatge(null);
	//test.setVisible(true);
	//test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// */
    }

}
