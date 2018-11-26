
import java.util.*;


public class Cache<K,V> {
    private HashMap cacheMap;
    private int maxNumItems;

    //Inner class representing value of cached map
    protected class CachedObject {
        public V value;

        public CachedObject(V value){
            this.value = value;
        }
    }//End of inner class representing a CachedObject

    public Cache(int maxNumItems){
        this.maxNumItems = maxNumItems;
        cacheMap = new HashMap();
    }

    @SuppressWarnings("unchecked")
    public void put(K key, V value){
        synchronized(cacheMap){
            int mapSize = cacheMap.size();
            int maxSize = maxNumItems;

            if(cacheMap.containsKey(key)){
                CachedObject c = (CachedObject) cacheMap.get(key);//get the old value mapped to key
                c = (CachedObject) value;
            }else{
                if(mapSize >= maxSize){
                    //TODO - decide action here
                }
            }
            cacheMap.put(key, new CachedObject(value));
        }
    }

    @SuppressWarnings("unchecked")
    public V get(K key){
        synchronized(cacheMap){
            CachedObject c = (CachedObject) cacheMap.get(key);

            if(c==null){
                return null;
            }else{
                return c.value;
            }
        }
    }

    public void remove(K key) {
        synchronized (cacheMap) {
            cacheMap.remove(key);
        }
    }

    public int size() {
        synchronized (cacheMap) {
            return cacheMap.size();
        }
    }

    @SuppressWarnings("unchecked")
    public void cleanFIFO(){

        ArrayList<K> cacheObjectList = null;

        synchronized(cacheMap){
            Set cacheSet = cacheMap.entrySet();//Return all cached items as a set
            Iterator itr = cacheSet.iterator(); //create an iterator to loop through the set
            cacheObjectList = new ArrayList<K>();

            K key = null;
            CachedObject c = null;

            while (itr.hasNext()) {
                Map.Entry mentry = (Map.Entry)itr.next();
                key = (K) mentry.getKey();

                //Add item key to the list
                cacheObjectList.add(key);
            }

            //Removed the first object in cache (FIFO)
            c = (CachedObject)cacheMap.get(cacheObjectList.get(0)); //Object evicted
            cacheMap.remove(cacheObjectList.get(0));
        }//End of synchronized block
    }
}
