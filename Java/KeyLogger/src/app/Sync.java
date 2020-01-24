package app;

public class Sync{
    boolean flag = true;
    public synchronized void notify_all(boolean value){
        if(value){
            try{
                wait();
            }
            catch(InterruptedException ex){
                ex.printStackTrace();
            }
        }
        flag = false;
        notify();
    }
}