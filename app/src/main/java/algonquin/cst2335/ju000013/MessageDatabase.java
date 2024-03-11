package algonquin.cst2335.ju000013;

import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {ChatMessage.class}, version = 1, exportSchema = false) // Add this line if you don't want to export schema
public abstract class MessageDatabase extends RoomDatabase {
    public abstract ChatMessageDAO cmDAO();
}