package edu.ub.prog2.DempereGuillermoGerman.vista;

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
        
        //Imatge img = new Imatge("img/earth.jpg");
        //img.show(true);

         VisorUB vista = new VisorUB3();
                
         // Call the menu manager method on the view, the program takes care of itself from this point on
         vista.gestioVistorUB();
         System.exit(0);
    }

}
