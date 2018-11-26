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

    private static ServerSocket serverSocket;
    private static Cache<String, SavedObject> cache; //Memory caching

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

    public synchronized static List<SavedObject> uncaching() throws Exception{
        /*
        add all SavedObject instances to a list, and return the list of SavedObjects to be sent to
        cloud-server
         */
        return cache.cleanFIFO();
    }


}
