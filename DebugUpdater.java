package PolygonArt;

import PolygonArt.Debug;

public class DebugUpdater implements Runnable {
    Debug debug;

    DebugUpdater(Debug d){
        debug = d;
    }


    @Override
    public void run() {
        while(true){
        if(ArtBoard.imagesSet() == true){
            debug.update();
           // ArtBoard.setBack();
        }
            try{
                Thread.sleep(20);
            }catch(InterruptedException e){
            }       
        }
    }
}
