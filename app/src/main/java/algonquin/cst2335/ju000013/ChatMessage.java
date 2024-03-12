package algonquin.cst2335.ju000013;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "time_sent")
    private String timeSent;

    @ColumnInfo(name = "is_sent")
    private boolean isSent;

    public ChatMessage() {}

    public ChatMessage(String message, String timeSent, boolean isSent) {
        this.message = message;
        this.timeSent = timeSent;
        this.isSent = isSent;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTimeSent() { return timeSent; }
    public void setTimeSent(String timeSent) { this.timeSent = timeSent; }

    public boolean isSent() { return isSent; }
    public void setSent(boolean sent) { isSent = sent; }
}