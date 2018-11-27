

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.DataInputStream;
import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

public class SavedObject {

    private String name;
    private File file;
    private LocalDateTime timeSent;
    private DataInputStream reader;

    public SavedObject(DataInputStream reader){

        synchronized (this){
            this.file = null;
            this.timeSent = LocalDateTime.now();

            this.reader = reader;
            try{
                byte[] bytes = IOUtils.toByteArray(reader);
                this.name = generateFileName();
                this.file = new File(this.name);
                FileUtils.writeByteArrayToFile(this.file, bytes);
            }catch(Exception e){
                e.printStackTrace();
            }
        }



    }

    private String generateFileName(){
        String name = UUID.randomUUID().toString();
        name = name.replace("-","");
        return name;
    }

    private String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }
}
