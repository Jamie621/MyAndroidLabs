package algonquin.cst2335.ju000013;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import algonquin.cst2335.ju000013.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.ju000013.databinding.SentMessageBinding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import algonquin.cst2335.ju000013.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.ju000013.databinding.SentMessageBinding;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import algonquin.cst2335.ju000013.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.ju000013.databinding.SentMessageBinding;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import algonquin.cst2335.ju000013.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.ju000013.databinding.SentMessageBinding;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import algonquin.cst2335.ju000013.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.ju000013.databinding.SentMessageBinding;
import algonquin.cst2335.ju000013.databinding.ReceivedMessageBinding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.ju000013.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.ju000013.databinding.SentMessageBinding;
import algonquin.cst2335.ju000013.databinding.ReceivedMessageBinding;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algonquin.cst2335.ju000013.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.ju000013.databinding.ReceivedMessageBinding;
import algonquin.cst2335.ju000013.databinding.SentMessageBinding;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algonquin.cst2335.ju000013.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.ju000013.databinding.ReceivedMessageBinding;
import algonquin.cst2335.ju000013.databinding.SentMessageBinding;

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
        // Implementation similar to send button logic for handling received messages
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    class ChatAdapter extends RecyclerView.Adapter<MyRowHolder> {
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
            return messages.get(position).isSentButton() ? 0 : 1;
        }
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ChatMessage message = messages.get(position);

                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Question:")
                            .setMessage("Do you want to delete the message: " + message.getMessage())
                            .setPositiveButton("Yes", (dialog, which) -> {
                                // Delete from database
                                executorService.execute(() -> {
                                    mDAO.deleteMessage(message);
                                    runOnUiThread(() -> {
                                        messages.remove(position);
                                        notifyItemRemoved(position);
                                    });
                                });
                            })
                            .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                            .create()
                            .show();
                }
            });

            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}


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
