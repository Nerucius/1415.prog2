/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ub.prog2.DempereGuillermoGerman.vista.listener;

/**
 * Classe listener usada per les demes clases que vulguin usar la funció del
 * temporitzador Timer del controlador.
 * 
 * @author German Dempere
 */
public abstract class VisorTimerListener{
   
    /** Called when the timer is started */
    public abstract void onStart();
    
    /** Called when the timer ticks
     * @param index The current index of the timer count.
     */
    public abstract void onTimer(int index);
    
    /** Called when the timer is paused */
    public abstract void onPause();
    
    /** Called when the timer is resumed. */
    public abstract void onResume();
    
    /** Called when the timer is stopped manually. */
    public abstract void onStop();
    
    /** Called when the timer runs out */
    public abstract void onFinish();


}
