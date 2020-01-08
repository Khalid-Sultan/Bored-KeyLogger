package app;

import java.util.Map;

//Keyboard Hook Libraries
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

//Mouse Hook Libraries
import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;

public class App {

    private static boolean run = true;


    public static void LogKeyboard(){
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
    public static void LogMouse(){
        GlobalMouseHook mouseHook = new GlobalMouseHook(); // Add true to the constructor, to switch to raw input mode
		System.out.println("Global mouse hook successfully started, press [middle] mouse button to shutdown. Connected mice:");		
		for (Map.Entry<Long,String> mouse:GlobalMouseHook.listMice().entrySet()) {
			System.out.format("%d: %s\n", mouse.getKey(), mouse.getValue());
		}
        mouseHook.addMouseListener(new GlobalMouseAdapter() {
		
			@Override 
			public void mousePressed(GlobalMouseEvent event)  {
				System.out.println(event);
				if ((event.getButtons() & GlobalMouseEvent.BUTTON_LEFT) != GlobalMouseEvent.BUTTON_NO
				&& (event.getButtons() & GlobalMouseEvent.BUTTON_RIGHT) != GlobalMouseEvent.BUTTON_NO) {
					System.out.println("Both mouse buttons are currently pressed!");
				}
				if (event.getButton()==GlobalMouseEvent.BUTTON_MIDDLE) {
					run = false;
				}
			}
			
			@Override 
			public void mouseReleased(GlobalMouseEvent event)  {
				System.out.println(event); 
			}
			
			@Override 
			public void mouseMoved(GlobalMouseEvent event) {
				System.out.println(event); 
			}
			
			@Override 
			public void mouseWheel(GlobalMouseEvent event) {
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
			mouseHook.shutdownHook(); 
		}
    }

    public static void main(String[] args) throws Exception {
        LogKeyboard();
        LogMouse();
    }
}