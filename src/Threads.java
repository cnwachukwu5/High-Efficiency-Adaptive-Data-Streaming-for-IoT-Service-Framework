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

            //Set up connection to Cloud-Server
            connect_CloudServer = new Socket("localhost", 56941);
            PrintWriter sendToServer = new PrintWriter(connect_CloudServer.getOutputStream(), true);
            BufferedReader readFromCloudServer = new BufferedReader(new InputStreamReader(connect_CloudServer.getInputStream()));

            int counter = 0;

            while(true){

                //Get IotNode status and Cloud Server status
                String IoTNode_Status = readIn.readLine();

                if(IoTNode_Status != null){
                    if(!IoTNode_Status.equals("IoT_Node is idle")) {
                        counter++;
                        String[] clientData = IoTNode_Status.split(":");
                        String nodeStatus = clientData[0];
                        String data = clientData[1];
                        System.out.println("IoT_Node status: " + nodeStatus);
                        System.out.println("Data from node: " + data);

                        String cloud_Server_status = readFromCloudServer.readLine();
                        System.out.println("Cloud_Server Status: " + cloud_Server_status);

                        sendToServer.println("Not actual data");

                        //Get data-stream from IoT node and convert to SavedObject instance
                        SavedObject newObjToCache_or_Send_To_Server = new SavedObject(data);

                        if (cloud_Server_status.equals("CONGESTED")) {

                            if (CacheNode.percentCacheSize() >= 80.0) {//This can be adjusted to get different behavior of the cache
                                if (IoTNode_Status.equals("HIGH_SEND")) {

                                    CacheNode.increase_Cache_Size(30);
                                    CacheNode.caching(newObjToCache_or_Send_To_Server);
                                    System.out.println("percentCacheSize: " + CacheNode.percentCacheSize());

                                } else if (IoTNode_Status.equals("MODERATE_SEND")) {

                                    CacheNode.increase_Cache_Size(20);
                                    CacheNode.caching(newObjToCache_or_Send_To_Server);
                                    System.out.println("percentCacheSize: " + CacheNode.percentCacheSize());

                                } else if (IoTNode_Status.equals("LOW")) {

                                    CacheNode.increase_Cache_Size(10);
                                    CacheNode.caching(newObjToCache_or_Send_To_Server);
                                    System.out.println("percentCacheSize: " + CacheNode.percentCacheSize());
                                }
                            } else {
                                CacheNode.caching(newObjToCache_or_Send_To_Server);
                                CacheNode.decrease_Cache_Size(30);//Checks cache capacity and reduce if okay to do so
                            }
                        } else if (cloud_Server_status.equals("MODERATELY_CONGESTED")) {
                            CacheNode.caching(newObjToCache_or_Send_To_Server);
                            //Remove items in cache by 30%
                            List<String> cachedObjects = CacheNode.uncaching(30);

                            //Send to server
                            int x = 0;
                            while(x < cachedObjects.size()){
                                String val = cachedObjects.get(x).toString();
                                System.out.println("Sending: " + val);
                                sendToServer.println(val);
                                ++x;
                            }


                            System.out.println("percentCacheSize: " + CacheNode.percentCacheSize());

                        } else if (cloud_Server_status.equals("NOT_CONGESTED")) {
                            CacheNode.caching(newObjToCache_or_Send_To_Server);
                            List<String> cachedObjects = CacheNode.uncaching(100);

                            //Send to server
                            int x = 0;
                            while(x < cachedObjects.size()){
                                String val = cachedObjects.get(x).toString();
                                System.out.println("Sending: " + val);
                                sendToServer.println(val);
                                x++;
                            }

                            System.out.println("percentCacheSize: " + CacheNode.percentCacheSize());
                        }

//                        connect_CloudServer.close();
//                        sendToServer.flush();
//                        sendToServer.close();
//                        readFromCloudServer.close();
                    }else{
                        System.out.println(IoTNode_Status);
                    }

                }//End of if-statement

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
