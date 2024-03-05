package algonquin.cst2335.ju000013;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

public class ChatRoomViewModel extends ViewModel {
    public MutableLiveData<ArrayList<ChatMessage>> messages = new MutableLiveData<>(new ArrayList<>());
}
