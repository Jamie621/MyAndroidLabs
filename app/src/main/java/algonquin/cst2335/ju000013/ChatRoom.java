package algonquin.cst2335.ju000013;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algonquin.cst2335.ju000013.databinding.ActivityChatRoomBinding;

        /**
        * The main activity class for the ChatRoom application, handling the user interface and interactions.
        */
public class ChatRoom extends AppCompatActivity {
    private ActivityChatRoomBinding binding; // Binding object for interacting with the layout views
    private ChatRoomViewModel chatModel; // ViewModel for handling data related to chat messages
    private ChatAdapter myAdapter; // Adapter for the RecyclerView that displays chat messages
    private ExecutorService executorService; // ExecutorService for running database operations on a background thread
    private ChatMessageDAO mDAO; // Data Access Object for accessing the chat messages database

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button,
        // so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.item_delete) {
            // Show a confirmation dialog to delete all messages
            new AlertDialog.Builder(this)
                    .setTitle("Delete all messages")
                    .setMessage("Are you sure you want to delete all messages?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Execute database operation to delete all messages on a background thread
                        executorService.execute(() -> {
                            mDAO.deleteAllMessages();
                            runOnUiThread(() -> {
                                myAdapter.clearMessages();
                                myAdapter.notifyDataSetChanged();
                                Toast.makeText(this, "All messages deleted", Toast.LENGTH_SHORT).show();
                            });
                        });
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        } else if (id == R.id.item_about) {
            // Show an "About" message
            Toast.makeText(this, "Version 1.0, created by Jungmin Ju", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


   /* @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item_delete) {
            new AlertDialog.Builder(this)
                    .setTitle("Delete all messages")
                    .setMessage("Are you sure you want to delete all messages?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Your code to delete messages goes here
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        } else if (id == R.id.item_about) {
            Toast.makeText(this, "Version 1.0, created by Jungmin Ju", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                // Confirmation dialog to delete messages
                new AlertDialog.Builder(this)
                        .setTitle("Delete all messages")
                        .setMessage("Are you sure you want to delete all messages?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Deletion code
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            case R.id.item_about:
                // Display version and creator info
                Toast.makeText(this, "Version 1.0, created by Jungmin Ju", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.myToolbar);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        myAdapter = new ChatAdapter(new ArrayList<>());
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(myAdapter);

        executorService = Executors.newSingleThreadExecutor();
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "chat_database").build();
        mDAO = db.cmDAO();

        loadMessages();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Setup the listeners for the send and receive buttons
        binding.sendButton.setOnClickListener(v -> sendMessage(mDAO, sdf, true));
        binding.receiveButton.setOnClickListener(v -> sendMessage(mDAO, sdf, false));
    }

    /**
     * Sends a message, either sent by the user or received from others.
     *
     * @param mDAO The DAO to access the database.
     * @param sdf  The SimpleDateFormat for formatting the date.
     * @param isSent True if the message is sent by the user, false if received.
     */
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

    /**
     * Loads messages from the database and displays them in the RecyclerView.
     */
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
                // Shutdown the ExecutorService to avoid memory leaks
                executorService.shutdown();
            }

            /**
             * Adapter class for managing the display of chat messages in a RecyclerView.
             */
            class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyRowHolder> {
                private List<ChatMessage> messages; // List to hold chat messages

                public ChatAdapter(List<ChatMessage> messages) {
                    this.messages = messages;
                }

                public void setMessages(List<ChatMessage> messages) {
                    this.messages = messages;
                    notifyDataSetChanged();
                }

                public void addMessage(ChatMessage message) {
                    this.messages.add(message);
                    notifyItemInserted(messages.size() - 1);
                }

                public void clearMessages() {
                    messages.clear();
                    notifyDataSetChanged();
                }

                @NonNull
                @Override
                public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    // Inflate the appropriate layout based on the message type (sent or received)
                    View itemView = LayoutInflater.from(parent.getContext()).inflate(viewType == 0 ? R.layout.sent_message : R.layout.received_message, parent, false);
                    return new MyRowHolder(itemView);
                }

                @Override
                public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                    // Bind the message data to the view
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
                    // Determine the view type based on whether the message was sent or received
                    return messages.get(position).isSent() ? 0 : 1;
                }

                /**
                 * ViewHolder class for holding views related to each individual chat message.
                 */
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
                                    .setMessage("Do you want to delete this message?")
                                    .setTitle("Confirm")
                                    .setNegativeButton("No", null)
                                    .setPositiveButton("Yes", (dialog, which) -> {
                                        ChatMessage removedMessage = messages.remove(position);
                                        notifyItemRemoved(position);
                                        executorService.execute(() -> mDAO.deleteMessage(removedMessage));
                                    })
                                    .show();
                        });
                    }
                }
            }
        }






/* public class ChatRoom extends AppCompatActivity {
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
