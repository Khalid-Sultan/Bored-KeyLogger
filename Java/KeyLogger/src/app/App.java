package app;

public class App {
   public static void main(String[] args) throws Exception {
    Keyboard keyboardLogger = new Keyboard("Keyboard");
    keyboardLogger.start();
    Mouse mouseLogger = new Mouse("Mouse");
    mouseLogger.start();
    }
}