package app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

//Mouse Hook Libraries
import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;

public class Mouse implements Runnable{

    Thread mouseThread;
	private String name;
	private FileWriter fileWriter;

	Sync sync;

    public Mouse(String name, Sync sync) {
		this.name = name;
		this.sync = sync;
	}
	public void start(){
        System.out.println("Thread started");
        if (mouseThread== null) {
            mouseThread = new Thread(this, name);
            mouseThread.start();

			File file = new File("Java/KeyLogger/Logged_Mouse/");
            boolean check = file.mkdirs();
            if(check){				
				try{
					fileWriter = new FileWriter(new File("Java/KeyLogger/Logged_Mouse/" + new SimpleDateFormat("yyyy.MM.dd HH.mm.ss").format(new java.util.Date()) + ".txt"), true);
				}	
				catch(IOException ex){
				}
			}
		}
    }



	@Override
	public void run() {
        GlobalMouseHook mouseHook = new GlobalMouseHook(); // Add true to the constructor, to switch to raw input mode
		System.out.println("Global mouse hook successfully started, press [middle] mouse button to shutdown. Connected mice:");		
		for (Map.Entry<Long,String> mouse:GlobalMouseHook.listMice().entrySet()) {
			System.out.format("Key : %d: Value: %s\n", mouse.getKey(), mouse.getValue());
		}
        mouseHook.addMouseListener(new GlobalMouseAdapter() {
		
			//If a Key is pressed, Log it into the file
			@Override 
			public void mousePressed(GlobalMouseEvent event)  {
				try{
					fileWriter.write("[PRESSED] \t" + event.toString());
					fileWriter.write("\n");
					System.out.println("Pressed Mouse Key Event Captured");
					if (event.getButton()==GlobalMouseEvent.BUTTON_MIDDLE) {
						sync.notify_all(false);
					}
				}
				catch(IOException ex){
					System.out.println("Could not write release key using file writer");
				}
			}
			
			//If a Key is released, Log it into the file
			@Override 
			public void mouseReleased(GlobalMouseEvent event)  {
				try{
					fileWriter.write("[RELEASED] \t" + event.toString());
					fileWriter.write("\n");
					System.out.println("Released Mouse Key Event Captured");
				}
				catch(IOException ex){
					System.out.println("Could not write release key using file writer");
				}
			}
			
			//If a Key is moved, Log it into the file
			@Override 
			public void mouseMoved(GlobalMouseEvent event) {
				try{
					fileWriter.write("[MOVED] \t" + event.toString());
					fileWriter.write("\n");
					System.out.println("Moved Mouse Key Event Captured");
				}
				catch(IOException ex){
					System.out.println("Could not write release key using file writer");
				}
			}
			
			//If a Wheel is used, Log it into the file
			@Override 
			public void mouseWheel(GlobalMouseEvent event) {
				try{
					fileWriter.write("[WHEEL] \t" + event.toString());
					fileWriter.write("\n");
					System.out.println("Wheel Mouse Key Event Captured");
				}
				catch(IOException ex){
					System.out.println("Could not write release key using file writer");
				}
			}
		});
		
		//As long as the sync object tells you to keep running, keep logging inputs
		try {
			while(sync.flag) { 
				Thread.sleep(128); 
			}
		} catch(InterruptedException e) { 
			//Do nothing
		} finally {
			mouseHook.shutdownHook(); 
		}

		//Close the file writer and finish of the thread.		
		try{
			fileWriter.close();
		}
		catch(IOException ex){
			System.out.println("Could not successfully close file writer");
		}

    }
}