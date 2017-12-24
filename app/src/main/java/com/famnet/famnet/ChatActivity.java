package com.famnet.famnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.famnet.famnet.Model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    // Constant
    private static final String TAG = "ChatActivity-Debug";

    // Views
    private EditText mEditText;
    private Button mSendButton;
    private RecyclerView mRecyclerView;
    private MessageAdapter mMessageAdapter;

    // Firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;
    private ChildEventListener mChildEventListener;

    // Properties
    private String mUsername = "";
    private List<Message> mMessages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Views
        mEditText = findViewById(R.id.new_chat_text);
        mSendButton = findViewById(R.id.send_button);

        //Firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesReference = mFirebaseDatabase.getReference("Messages");
        mCurrentUser = mFirebaseAuth.getCurrentUser();

        // Properties
        if (mCurrentUser.getDisplayName() != null) {
            mUsername = mCurrentUser.getDisplayName();
        }

        mMessages = new ArrayList<>();


        // Check User
        if (mFirebaseAuth.getCurrentUser() == null) {
            startActivity(MainActivity.createIntent(this));
            finish();
            return;
        }

        //Recycler View
        mRecyclerView = findViewById(R.id.chat_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //TODO: Still having bug when loading Message
        mMessagesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Log.d(TAG, "inside dataChange");
                    List<Message> tempMessages = new ArrayList<>();

                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                        Message message = postSnapShot.getValue(Message.class);
                        Log.d(TAG, "load Message: " + message.getText());
                        tempMessages.add(message);
                    }

                    mMessages = tempMessages;

                    updateMessages(mMessages);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "Failed to read data", Toast.LENGTH_SHORT).show();
            }
        });



        // Message implementation
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mEditText.getText().toString().equals("")) { // Message cannot be empty
                    Message message = new Message(mEditText.getText().toString(), mUsername, null);
                    mMessagesReference.push().setValue(message); // The messages will update because of valueListener of messageReference

                    // Delete the sent message
                    mEditText.setText("");
//                Toast.makeText(ChatActivity.this, "" + count, Toast.LENGTH_SHORT).show();
                }
            }
        });



        //Navigation bar
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setSelectedItemId(R.id.navigation_chat);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_chat:
                        break;
                    case R.id.navigation_tasks:
                        Intent intent2 = new Intent(ChatActivity.this, TasksActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_personal_task:
                        Intent intent3 = new Intent(ChatActivity.this, PersonalTasksActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.navigation_account:
                        Intent intent4 = new Intent(ChatActivity.this, AccountActivity.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });
    }


    // MessageHolder for Recycler View

    public class MessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Message mMessage;
        private TextView mLeftChat;
        private TextView mLeftSender;
//        private TextView mRightChat;
//        private TextView mRightSender;

        public MessageHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mLeftChat = itemView.findViewById(R.id.chat_left_textView);
            mLeftSender = itemView.findViewById(R.id.chat_sender_left_textView);
//            mRightChat = itemView.findViewById(R.id.chat_right_textView);
//            mRightSender = itemView.findViewById(R.id.chat_sender_right_textView);
        }

        public void bind(Message message) {
            mMessage = message;
            mLeftChat.setText(mMessage.getText());
            mLeftSender.setText(mMessage.getSender());
        }

        @Override
        public void onClick(View v) {

        }
    }

    // ChatAdapter for Recycler View

    public class MessageAdapter extends RecyclerView.Adapter<MessageHolder> {

        List<Message> mMessages;

        public MessageAdapter(List<Message> messages) {
            mMessages = messages;
        }

        @Override
        public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message, parent, false);
            return new MessageHolder(view);
        }

        @Override
        public void onBindViewHolder(MessageHolder holder, int position) {
            Message message = mMessages.get(position);
            holder.bind(message);
        }

        @Override
        public int getItemCount() {
            Log.d("getItemCount", "Item count: " + mMessages.size());
            return mMessages.size();
        }
    }

    private void updateMessages(List<Message> messages){
        mMessageAdapter = new MessageAdapter(messages);
        mRecyclerView.setAdapter(mMessageAdapter);
    }


}
