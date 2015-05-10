package edu.ub.prog2.DempereGuillermoGerman.model;

import edu.ub.prog2.utils.ImageFile;
import edu.ub.prog2.utils.VisorException;
import java.util.Iterator;

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
}
