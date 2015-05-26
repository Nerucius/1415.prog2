package edu.ub.prog2.DempereGuillermoGerman.model;

import edu.ub.prog2.utils.ImageFile;
import edu.ub.prog2.utils.VisorException;
import java.util.Iterator;

/** Classe que defineix una biblioteca d'imatges amb imatges no repetides.
 * Les Imatges guardades en aquesta llista son les referencies originals, i tota
 * Imatge guardada a altres llites (Albums etc.) han de ser referencies adicionals
 * a les imatges d'aquesta llista.
 * 
 * @author German
 */
public class BibliotecaImatges extends LlistaImatges {

    public BibliotecaImatges() {
        super(256);
    }


    @Override
    public void addImage(ImageFile image) throws VisorException{
        if(isFull())
            maxCap = maxCap*2+1;        

        // Throw an exception if the image is already in the library
        Iterator<ImageFile> iter = getList().iterator();
        while (iter.hasNext()) 
            if (image.equals(iter.next())) 
                throw new VisorException("Imatge duplicada");
        
        super.addImage(image);
    }
    
    @Override
    public String toString(){
        return "Biblioteca: "+getSize()+" Imatges.";
    }
}
