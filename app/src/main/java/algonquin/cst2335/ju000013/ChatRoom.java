package algonquin.cst2335.ju000013;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import algonquin.cst2335.ju000013.databinding.ActivityChatRoomBinding;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.room.Room;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import com.google.android.material.snackbar.Snackbar;

public class ChatRoom extends AppCompatActivity {
    private ActivityChatRoomBinding binding;
    private ChatRoomViewModel chatModel;
    private ChatAdapter myAdapter;
    private ExecutorService executorService;
    private ChatMessageDAO mDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        myAdapter = new ChatAdapter(new ArrayList<>());
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(myAdapter);

        executorService = Executors.newSingleThreadExecutor();
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "chat_database").build();
        mDAO = db.cmDAO();

        loadMessages();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        binding.sendButton.setOnClickListener(v -> sendMessage(mDAO, sdf, true));
        binding.receiveButton.setOnClickListener(v -> sendMessage(mDAO, sdf, false));
    }

    private void sendMessage(ChatMessageDAO mDAO, SimpleDateFormat sdf, boolean isSent) {
        String messageText = binding.textInput.getText().toString();
        if (!messageText.isEmpty()) {
            String currentTime = sdf.format(new Date());
            ChatMessage message = new ChatMessage(messageText, currentTime, isSent);

            executorService.execute(() -> {
                long id = mDAO.insertMessage(message);
                message.setId(id);
                runOnUiThread(() -> {
                    myAdapter.addMessage(message);
                    binding.recycleView.scrollToPosition(myAdapter.getItemCount() - 1);
                    binding.textInput.setText("");
                });
            });
        }
    }

    private void loadMessages() {
        executorService.execute(() -> {
            List<ChatMessage> messages = mDAO.getAllMessages();
            runOnUiThread(() -> {
                myAdapter.setMessages(messages);
                myAdapter.notifyDataSetChanged();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyRowHolder> {
        private List<ChatMessage> messages;

        public ChatAdapter(List<ChatMessage> messages) {
            this.messages = messages;
        }

        public void setMessages(List<ChatMessage> messages) {
            this.messages = messages;
        }

        public void addMessage(ChatMessage message) {
            this.messages.add(message);
            notifyItemInserted(messages.size() - 1);
        }

        public void removeMessage(int position) {
            ChatMessage message = messages.get(position);
            executorService.execute(() -> {
                mDAO.deleteMessage(message);
                runOnUiThread(() -> {
                    messages.remove(position);
                    notifyItemRemoved(position);
                });
            });
        }

        @NonNull
        @Override
        public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView;
            if (viewType == 0) { // Sent message
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_message, parent, false);
            } else { // Received message
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_message, parent, false);
            }
            return new MyRowHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
            ChatMessage chatMessage = messages.get(position);
            holder.messageText.setText(chatMessage.getMessage());
            holder.timeText.setText(chatMessage.getTimeSent());
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        @Override
        public int getItemViewType(int position) {
            return messages.get(position).isSent() ? 0 : 1;
        }

        public class MyRowHolder extends RecyclerView.ViewHolder {
            TextView messageText;
            TextView timeText;

            public MyRowHolder(View itemView) {
                super(itemView);
                messageText = itemView.findViewById(R.id.messageText);
                timeText = itemView.findViewById(R.id.timeText);

                itemView.setOnClickListener(click -> {
                    int position = getAbsoluteAdapterPosition();

                    new AlertDialog.Builder(itemView.getContext())
                            .setMessage("Do you want to delete the message: " + messageText.getText())
                            .setTitle("Question:")
                            .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                            .setPositiveButton("Yes", (dialog, cl) -> {
                                ChatMessage removedMessage = messages.get(position);
                                messages.remove(position);
                                ChatAdapter.this.notifyItemRemoved(position); // Corrected here

                                Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                        .setAction("Undo", clk -> {
                                            messages.add(position, removedMessage);
                                            ChatAdapter.this.notifyItemInserted(position); // Corrected here
                                        })
                                        .show();
                            }).create().show();


                });
            }
        }
    }
}







/*public class ChatRoom extends AppCompatActivity {
    private ActivityChatRoomBinding binding;
    private ChatRoomViewModel chatModel;
    private ChatAdapter myAdapter;
    private ExecutorService executorService;
    private ChatMessageDAO mDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        myAdapter = new ChatAdapter(new ArrayList<>());
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(myAdapter);

        executorService = Executors.newSingleThreadExecutor();

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(),
                MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        executorService.execute(() -> {
            List<ChatMessage> messages = mDAO.getAllMessages();
            runOnUiThread(() -> {
                myAdapter.setMessages(messages);
                myAdapter.notifyDataSetChanged();
            });
        });

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");

        binding.sendButton.setOnClickListener(v -> handleSendButton(mDAO, sdf));
        binding.receiveButton.setOnClickListener(v -> handleReceiveButton(mDAO, sdf));
    }

    private void handleSendButton(ChatMessageDAO mDAO, SimpleDateFormat sdf) {
        String messageText = binding.textInput.getText().toString();
        if (!messageText.isEmpty()) {
            String currentDateandTime = sdf.format(new Date());
            ChatMessage message = new ChatMessage(messageText, currentDateandTime, true);

            executorService.execute(() -> {
                long id = mDAO.insertMessage(message);
                message.setId(id);
                runOnUiThread(() -> {
                    myAdapter.addMessage(message);
                    binding.recycleView.scrollToPosition(myAdapter.getItemCount() - 1);
                    binding.textInput.setText("");
                });
            });
        }
    }

    private void handleReceiveButton(ChatMessageDAO mDAO, SimpleDateFormat sdf) {
        // Similar logic for receiving messages
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyRowHolder> {
        private List<ChatMessage> messages;

        public ChatAdapter(List<ChatMessage> messages) {
            this.messages = messages;
        }

        public void setMessages(List<ChatMessage> messages) {
            this.messages = messages;
        }

        public void addMessage(ChatMessage message) {
            this.messages.add(message);
            notifyItemInserted(messages.size() - 1);
        }

        public void removeMessage(int position) {
            ChatMessage message = messages.get(position);
            mDAO.deleteMessage(message);
            messages.remove(position);
            notifyItemRemoved(position);
        }

        @NonNull
        @Override
        public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView;
            if (viewType == 0) {
                SentMessageBinding binding = SentMessageBinding.inflate(inflater, parent, false);
                itemView = binding.getRoot();
            } else {
                ReceivedMessageBinding binding = ReceivedMessageBinding.inflate(inflater, parent, false);
                itemView = binding.getRoot();
            }
            return new MyRowHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
            ChatMessage chatMessage = messages.get(position);
            holder.messageText.setText(chatMessage.getMessage());
            holder.timeText.setText(chatMessage.getTimeSent());

            holder.itemView.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete Message");
                builder.setMessage("Do you want to delete this message?");
                builder.setPositiveButton("Yes", (dialog, which) -> removeMessage(holder.getAdapterPosition()));
                builder.setNegativeButton("No", null);
                builder.show();
            });
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        @Override
        public int getItemViewType(int position) {
            return messages.get(position).isSent() ? 0 : 1;
        }

        class MyRowHolder extends RecyclerView.ViewHolder {
            TextView messageText;
            TextView timeText;

            public MyRowHolder(@NonNull View itemView) {
                super(itemView);
                messageText = itemView.findViewById(R.id.messageText);
                timeText = itemView.findViewById(R.id.timeText);
            }
        }
    }
}*/

/*
public class ChatRoom extends AppCompatActivity {
    private ActivityChatRoomBinding binding;
    private ChatRoomViewModel chatModel; // ViewModel to hold messages
    private ChatAdapter myAdapter; // Adapter for RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        final ArrayList<ChatMessage> messages = chatModel.messages.getValue(); // Declare as final
        if (messages == null) {
            chatModel.messages.postValue(new ArrayList<>()); // Use postValue for background thread if necessary
        }

        myAdapter = new ChatAdapter(messages);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(myAdapter);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");

        binding.sendButton.setOnClickListener(v -> {
            String messageText = binding.textInput.getText().toString();
            if (!messageText.isEmpty()) {
                String currentDateandTime = sdf.format(new Date());
                ChatMessage message = new ChatMessage(messageText, currentDateandTime, true);
                messages.add(message);
                chatModel.messages.postValue(messages);
                myAdapter.notifyItemInserted(messages.size() - 1);
                binding.recycleView.scrollToPosition(messages.size() - 1);
                binding.textInput.setText("");
            }
        });

        binding.receiveButton.setOnClickListener(v -> {
            String messageText = binding.textInput.getText().toString();
            if (!messageText.isEmpty()) {
                String currentDateandTime = sdf.format(new Date());
                ChatMessage message = new ChatMessage(messageText, currentDateandTime, false);
                messages.add(message);
                chatModel.messages.postValue(messages);
                myAdapter.notifyItemInserted(messages.size() - 1);
                binding.recycleView.scrollToPosition(messages.size() - 1);
                binding.textInput.setText("");
            }
        });
    }

    static class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message); // Correct ID
            timeText = itemView.findViewById(R.id.time); // Correct ID
        }
    }

    class ChatAdapter extends RecyclerView.Adapter<MyRowHolder> {
        private final ArrayList<ChatMessage> messages;

        public ChatAdapter(ArrayList<ChatMessage> messages) {
            this.messages = messages;
        }

        @NonNull
        @Override
        public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == 0) {
                SentMessageBinding binding = SentMessageBinding.inflate(inflater, parent, false);
                return new MyRowHolder(binding.getRoot());
            } else {
                ReceivedMessageBinding binding = ReceivedMessageBinding.inflate(inflater, parent, false);
                return new MyRowHolder(binding.getRoot());
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
            ChatMessage chatMessage = messages.get(position);
            holder.messageText.setText(chatMessage.getMessage());
            holder.timeText.setText(chatMessage.getTimeSent());
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        @Override
        public int getItemViewType(int position) {
            // Differentiate view type based on the message sent status
            return messages.get(position).isSentButton() ? 0 : 1;
        }
    }
}*/
