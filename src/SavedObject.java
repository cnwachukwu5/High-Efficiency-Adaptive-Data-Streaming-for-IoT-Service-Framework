
import java.time.LocalDateTime;
import java.util.UUID;

public class SavedObject {

    private String name;
    private LocalDateTime timeSent;
    private String tempValue;

    public SavedObject(){

        synchronized (this){
            this.timeSent = LocalDateTime.now();
            this.name = generateFileName();
            this.tempValue = generateFileName();
        }



    }

    public String generateFileName(){
        String name = UUID.randomUUID().toString();
        name = name.replace("-","");
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }
}
