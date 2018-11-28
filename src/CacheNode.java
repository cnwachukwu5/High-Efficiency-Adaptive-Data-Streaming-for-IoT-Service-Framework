/*
This class implements the caching node. It acts as server, so IoT nodes connect to it and send data streams.
It saves (caches) the sent object to the defined cache if network is congested based on the feedback mechanism from
cloud-based server

Project members: Sambath Pich and Cyprian Nwachukwu

 */
import java.net.*;
import java.io.*;
import java.util.List;
import java.util.Set;


public class CacheNode {

    private static Cache<String, SavedObject> cache; //Memory caching
    private static ServerSocket serverSocket;//Socket for client connection

    public synchronized static void caching(SavedObject object) throws Exception{
        /*
        Caches the sent object if not already exist
        @Param: object -> data stream to be sent to cloud server
         */
        SavedObject sentObject;

        if((sentObject = cache.get(object.getFile().getName())) != null ){
            System.out.println("Object already exists");
        }else{
            cache.put(object.getFile().getName(), object);
        }
    }

    public synchronized static List<SavedObject> uncaching() throws Exception{
        /*
        add all SavedObject instances to a list, and return the list of SavedObjects to be sent to
        cloud-server
         */
        return cache.cleanFIFO();
    }

    public synchronized static double percentCacheSize(){

            int cacheSize = cache.size();
            return ((cacheSize*1.0)/cache.getMaxNumItems()) * 100;

    }

    private static void init(){
        int port = 10567;
        try{
            serverSocket = new ServerSocket(port);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        int maxNumOfCachedItem = 10;
        cache = new Cache<String, SavedObject>(maxNumOfCachedItem);
        init();
        Socket client = null;
        while (true) {
            try {
                System.out.println("waiting for IoTNode connection ...");
                client = serverSocket.accept(); //Accept client connection
                System.out.println("Connection established ...");
                new Thread(new Threads(client)).start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
