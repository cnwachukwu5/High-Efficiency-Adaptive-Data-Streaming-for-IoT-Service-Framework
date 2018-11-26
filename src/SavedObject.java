import java.io.File;
import java.time.LocalDateTime;

public class SavedObject {

    private String name;
    private File file;
    private LocalDateTime timeSent;

    public SavedObject(File file, LocalDateTime timeSent){
        this.file = file;
        this.timeSent = timeSent;
        this.name = file.getName();
    }

    public String getName() {
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
