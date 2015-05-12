package edu.ub.prog2.DempereGuillermoGerman.vista;

import javax.swing.JFrame;

/**
 * Main class
 */
public class GestioVisorUB {

    /**
     * Program entry point
     *
     * @param args Optional arguments
     */
    public static void main(String... args) {

        VisorUB4 vista = new VisorUB4();
        vista.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vista.setVisible(true);
         
        //JFrame test = new FrmAfegirImatge(null);
        //test.setVisible(true);
        //test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
