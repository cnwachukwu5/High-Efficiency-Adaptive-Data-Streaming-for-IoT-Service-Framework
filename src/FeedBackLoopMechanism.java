
public class FeedBackLoopMechanism {

    public String cacheNode_To_IoTNode_Msg(double percentCacheSize){
        if (percentCacheSize >= 80.0){
            return "loaded";
        }else{
            return "active";
        }
    }
}
