import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            BufferedReader readIn = new BufferedReader(new InputStreamReader(client.getInputStream()));//Read IoTNode status


            System.out.println("Connected to Cloud_Server");
            System.out.println();

            while(true){
                //Set up connection to Cloud-Server
                connect_CloudServer = new Socket("localhost", 56941);
                DataOutputStream sendToServer = new DataOutputStream(connect_CloudServer.getOutputStream());
                BufferedReader readFromCloudServer = new BufferedReader(new InputStreamReader(connect_CloudServer.getInputStream()));

                //Get IotNode status and Cloud Server status
                String IoTNode_Status = readIn.readLine();
                System.out.println("IoT_Node status: " + IoTNode_Status);

                String cloud_Server_status = readFromCloudServer.readLine();
                System.out.println("Cloud_Server Status: " + cloud_Server_status);

                //Get data-stream from IoT node and convert to SavedObject instance
                SavedObject newObjToCache_or_Send_To_Server = new SavedObject();
                if(cloud_Server_status.equals("CONGESTED")){
                    System.out.println("percentCacheSize: " + CacheNode.percentCacheSize());
                    if(CacheNode.percentCacheSize() >= 80.0){
                        if(IoTNode_Status.equals("HIGH_SEND")){
                            CacheNode.increase_Cache_Size(30);
                        }else if (IoTNode_Status.equals("MODERATE_SEND")){
                            CacheNode.increase_Cache_Size(20);
                        }else if (IoTNode_Status.equals("LOW")){
                            CacheNode.increase_Cache_Size(10);
                        }else{
                            CacheNode.increase_Cache_Size(0);
                        }
                    }else{
                        CacheNode.decrease_Cache_Size(30);

                    }

                    CacheNode.caching(newObjToCache_or_Send_To_Server);
                }else if (cloud_Server_status.equals("MODERATELY_CONGESTED")){
                    CacheNode.caching(newObjToCache_or_Send_To_Server);
                    //Remove items in cache by 30%
                    List<SavedObject> cachedObjects = CacheNode.uncaching(30);

                }else if (cloud_Server_status.equals("NOT_CONGESTED")){
                    CacheNode.caching(newObjToCache_or_Send_To_Server);
                    List<SavedObject> cachedObjects = CacheNode.uncaching(100);
                }

                connect_CloudServer.close();
                sendToServer.flush();
                sendToServer.close();
                readFromCloudServer.close();

            }//End of while

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean check_IoTNodeStatus(String str){
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(str);
           return matcher.find();
    }

}
