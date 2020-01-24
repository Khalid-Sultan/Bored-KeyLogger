package app;

import java.util.Map;

//Mouse Hook Libraries
import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;

public class Mouse implements Runnable{

    Thread mouseThread;
	private String name;
	Sync sync;

    public Mouse(String name, Sync sync) {
		this.name = name;
		this.sync = sync;
    }

    // private static boolean run = true;


    @Override
	public void run() {
        GlobalMouseHook mouseHook = new GlobalMouseHook(); // Add true to the constructor, to switch to raw input mode
		System.out.println("Global mouse hook successfully started, press [middle] mouse button to shutdown. Connected mice:");		
		for (Map.Entry<Long,String> mouse:GlobalMouseHook.listMice().entrySet()) {
			System.out.format("Key : %d: Value: %s\n", mouse.getKey(), mouse.getValue());
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
					// run = false;
					sync.notify_all(false);
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
			while(sync.flag) { 
				Thread.sleep(128); 
			}
		} catch(InterruptedException e) { 
			//Do nothing
		} finally {
			mouseHook.shutdownHook(); 
        }
    }
    public void start(){
        System.out.println("Thread started");
        if (mouseThread== null) {
            mouseThread = new Thread(this, name);
            mouseThread.start();
        }
    }
}