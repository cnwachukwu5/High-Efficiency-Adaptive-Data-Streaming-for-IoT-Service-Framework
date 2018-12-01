
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
    public List<V> cleanFIFO(int percentage_Of_Items_To_Remove){

        ArrayList<V> cacheObjectList = null;
        ArrayList<K> cacheObjectList_Key = null;
        ArrayList<V> sendingList = new ArrayList<>();

        synchronized(cacheMap){
            Set cacheSet = cacheMap.entrySet();//Return all cached items as a set
            Iterator itr = cacheSet.iterator(); //create an iterator to loop through the set
            cacheObjectList = new ArrayList<V>();
            cacheObjectList_Key = new ArrayList<K>();



            V value = null;
            K key = null;
            CachedObject c = null;

            while (itr.hasNext()) {
                Map.Entry mentry = (Map.Entry)itr.next();
                value = (V) mentry.getValue();
                key = (K) mentry.getKey();

                //Add item key to the list
                cacheObjectList.add(value);
                cacheObjectList_Key.add(key);
            }


            if (percentage_Of_Items_To_Remove < 100){
                int number_of_items_clear_from_list = size() * percentage_Of_Items_To_Remove/100;
                for(int i = 0; i < number_of_items_clear_from_list; i++){
                    sendingList.add(cacheObjectList.get(i));
                    cacheMap.remove(cacheObjectList_Key.get(i));
                }
            }else{
                //Removed the all objects in cache (FIFO)
                cacheMap.clear();
            }

        }//End of synchronized block

        //return cacheObjectList;
        if (percentage_Of_Items_To_Remove < 100){
            return sendingList;
        }else{
            return cacheObjectList;
        }
    }

    public int getMaxNumItems() {
        return maxNumItems;
    }

    public void setMaxNumItems(int maxNumItems) {
        this.maxNumItems = maxNumItems;
    }
}
