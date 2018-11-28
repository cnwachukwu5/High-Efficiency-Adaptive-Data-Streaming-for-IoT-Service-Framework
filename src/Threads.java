import java.io.*;
import java.net.Socket;
import java.util.List;

public class Threads implements Runnable {

    private final Socket client;

    public Threads(Socket client){
        this.client = client;
    }

    @Override
    public void run(){
        Socket connect_CloudServer;

        try{
            PrintWriter toIoTNodes = new PrintWriter(client.getOutputStream(), true); //Write to outputStream
            DataInputStream fromIoTNodes = new DataInputStream(client.getInputStream());//Read from input

            //Send message to connected IoTNode whether Caching Node is accepting data stream
            String msg_IoTNode = new FeedBackLoopMechanism().cacheNode_To_IoTNode_Msg(CacheNode.percentCacheSize());

            if(msg_IoTNode.equals("loaded")){
                toIoTNodes.println("Loaded");
            }else{

                if(msg_IoTNode.equals("active")){
                    toIoTNodes.println("active");
                    //Get data-stream from IoT node and convert to SavedObject instance
                    SavedObject newObjToCache_or_Send_To_Server = new SavedObject(fromIoTNodes);

                    //Check FeedBack loop to confirm Cloud-server status (Overloaded or Receiving)
                    //Set up connection to Cloud-Server
                    //connect_CloudServer = new Socket("localhost", 56941);

                    String serverStatus = "Overloaded";

                    if(serverStatus.equals("Overloaded")){ //Cache the sent object
                        CacheNode.caching(newObjToCache_or_Send_To_Server);
                        System.out.println("object cached...");
                        System.out.println(CacheNode.cacheSize());
                    }


                    if(serverStatus.equals("Receiving")){
                        CacheNode.caching(newObjToCache_or_Send_To_Server);
                        List<SavedObject> cachedObjects = CacheNode.uncaching();

                        //TODO - loop through the list and send every stream to server
                    }
                }
            }




                //TODO - Create IO Stream for CacheNode to communicate to cloud server

                //TODO - Create a Cloud-Server - separate project


        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
