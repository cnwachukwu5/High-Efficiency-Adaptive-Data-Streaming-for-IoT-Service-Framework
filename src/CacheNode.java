/*
This class implements the caching node. It acts as server, so IoT nodes connect to it and send data streams.
It saves (caches) the sent object to the defined cache if network is congested based on the feedback mechanism from
cloud-based server

Project members: Sambath Pich and Cyprian Nwachukwu

 */
import java.net.*;
import java.io.*;
import java.text.DecimalFormat;
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

        if((sentObject = cache.get(object.getName())) != null ){
            System.out.println("Object already exists");
        }else{
            cache.put(object.getName(), object);
        }
    }

    public synchronized static List<SavedObject> uncaching(int percentage_Of_Items_To_Remove) throws Exception{
        /*
        add all SavedObject instances to a list, and return the list of SavedObjects to be sent to
        cloud-server
         */
        return cache.cleanFIFO(percentage_Of_Items_To_Remove);
    }

    public synchronized static double percentCacheSize(){

            int cacheSize = cache.size();
        DecimalFormat df = new DecimalFormat("##.00");
        double val = ((cacheSize*1.0)/cache.getMaxNumItems()) * 100;
            return Double.parseDouble(df.format(val));

    }

    public synchronized static int cacheSize(){
        return cache.size();
    }

    private static void init(){
        int port = 10567;
        try{
            serverSocket = new ServerSocket(port);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public synchronized static void increase_Cache_Size(int increase_value){
        int increaseLimit = cache.getMaxNumItems() * increase_value/100;
        cache.setMaxNumItems(cache.getMaxNumItems() + increaseLimit);
        System.out.println("Increasing cache by: " + increase_value);
    }

    public synchronized static void decrease_Cache_Size(int decreaseSize){
        System.out.println("Current cache capacity: " +cache.getMaxNumItems());

        if(cache.getMaxNumItems() > 10){
            int diff = cache.getMaxNumItems() - cache.size();
            double percent_diff = (diff*1.0/cache.getMaxNumItems()) * 100;

            if(percent_diff >= 40){
                int decreaseLimit = cache.getMaxNumItems() * decreaseSize/100;
                cache.setMaxNumItems(cache.getMaxNumItems() - decreaseLimit);
                System.out.println("decreasing cache size by " + decreaseSize);
            }
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
