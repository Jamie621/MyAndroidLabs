package algonquin.cst2335.ju000013;

import androidx.room.Entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {
    @PrimaryKey(autoGenerate = true)
    private int id; // Assuming your table has an ID
    private String message;
    private String timeSent;
    private boolean isSent; // I've renamed this to match the error message

    // Empty constructor required by Room
    public ChatMessage() {
    }

    // Constructor matching fields
    public ChatMessage(String message, String timeSent, boolean isSent) {
        this.message = message;
        this.timeSent = timeSent;
        this.isSent = isSent;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    // Other methods, if any...
}





/*@Entity
public class ChatMessage {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "timeSent")
    private String timeSent;

    @ColumnInfo(name = "sendOrReceive")
    private int sendOrReceive; // Assuming this is what sendOrReceive corresponds to; true for send, false for receive.

    public ChatMessage(String message, String timeSent, boolean isSent) {
        this.message = message;
        this.timeSent = timeSent;
        this.sendOrReceive = isSent ? 1 : 0; // Converting boolean to int, 1 for true, 0 for false.
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSent() {
        return sendOrReceive == 1;
    }
}*/



/*
public class ChatMessage {
    private String message;
    private String timeSent;
    private boolean isSentButton;

    public ChatMessage(String message, String timeSent, boolean isSentButton) {
        this.message = message;
        this.timeSent = timeSent;
        this.isSentButton = isSentButton;
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }
}
*/
