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
            DataOutputStream toIoTNodes = new DataOutputStream(client.getOutputStream()); //Write to outputStream
            DataInputStream fromIoTNodes = new DataInputStream(client.getInputStream());//Read from input

            //Get data-stream from IoT node and convert to SavedObject instance
            SavedObject newObj = new SavedObject(fromIoTNodes);

            //Check FeedBack loop to confirm Cloud-server status (Overloaded or Receiving)
                //Set up connection to Cloud-Server
                connect_CloudServer = new Socket("localhost", 56941);

                //TODO - Create IO Stream for CacheNode to communicate to cloud server

                //TODO - Create a class to implement the Feedback loop mechanism

                //TODO - Create a Cloud-Server

            String serverStatus = "";
            if(serverStatus.equals("Overloaded")){ //Cache the sent object
                CacheNode.caching(newObj);
            }

            if(serverStatus.equals("Receiving")){
                List<SavedObject> cachedObjects = CacheNode.uncaching();
            }


        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
