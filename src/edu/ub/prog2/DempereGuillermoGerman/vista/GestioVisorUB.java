package edu.ub.prog2.DempereGuillermoGerman.vista;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** Main class */
public class GestioVisorUB {

        public static JFrame frame;
        public static JPanel imgPanel;
    
	/** Program entry point
	 * @param args Optional arguments
	 */
	public static void main(String... args) {
                frame = new JFrame("Visor UB");
                imgPanel = new JPanel(new BorderLayout());
                
                frame.add(imgPanel);
                               
                frame.setSize(300, 400);
                frame.setResizable(false);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            
		// View implementation
		VisorUB vista = new VisorUB3();

		// Call the menu manager method on the view, the program takes care of itself from this point on
		vista.gestioVistorUB();
                System.exit(0);
	}

}
