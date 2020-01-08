package app;

import java.util.Map;

//Keyboard Hook Libraries
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class Keyboard implements Runnable{

    Thread keyboardThread;
    private String name;
    public Keyboard(String name) {
     this.name = name;
    }

    private static boolean run = true;

	@Override
	public void run() {
        // Might throw a UnsatisfiedLinkError if the native library fails to load or a RuntimeException if hooking fails 
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true); // Use false here to switch to hook instead of raw input

		System.out.println("Key Logging Started, press [escape] key to shutdown. Connected keyboards:");		
		for (Map.Entry<Long, String> keyboard : GlobalKeyboardHook.listKeyboards().entrySet()) {
			System.out.format("%d: %s\n", keyboard.getKey(), keyboard.getValue());
		}
		keyboardHook.addKeyListener(new GlobalKeyAdapter() {
		
			@Override 
			public void keyPressed(GlobalKeyEvent event) {
				System.out.println(event);
				if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_ESCAPE) {
					run = false;
				}
			}
			
			@Override 
			public void keyReleased(GlobalKeyEvent event) {
				System.out.println(event); 
			}
		});
		
		try {
			while(run) { 
				Thread.sleep(128); 
			}
		} catch(InterruptedException e) { 
			//Do nothing
		} finally {
			keyboardHook.shutdownHook(); 
		}
	}

    public void start(){
        System.out.println("Thread started");
        if (keyboardThread== null) {
            keyboardThread = new Thread(this, name);
            keyboardThread.start();
        }
    }
}