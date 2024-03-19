package algonquin.cst2335.ju000013;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;


@Dao
public interface ChatMessageDAO {
    @Insert
    long insertMessage(ChatMessage m);

    @Query("SELECT * FROM ChatMessage")
    List<ChatMessage> getAllMessages();

    @Delete
    void deleteMessage(ChatMessage m);

    @Query("DELETE FROM ChatMessage")
    void deleteAllMessages();
}