package com.ehsanapp.whatsappclone.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ehsanapp.whatsappclone.R;
import com.ehsanapp.whatsappclone.adapter.ChatRecyclerAdapter;
import com.ehsanapp.whatsappclone.structure.MessageStructure;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityChat extends AppCompatActivity {

  EditText edt_sendMessage;
  ImageButton img_send;
  RecyclerView rv_chat;

  List<MessageStructure> messageStructureList = new ArrayList<>();

  String receiverName = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat);

    receiverName = getIntent().getStringExtra("ACTIONBAR_TITLE");
    setTitle(receiverName);
    initializeUi();
    refreshMessages();

    img_send.setOnClickListener(view -> {
      if (!edt_sendMessage.getText().toString().equals("")) {
        MessageStructure message = new MessageStructure();
        message.setSenderUserName(ParseUser.getCurrentUser().getUsername());
        message.setReceiverUserName(receiverName);

        message.setMessageBody(edt_sendMessage.getText().toString());

        message.saveInBackground(exception -> {
          if (exception == null) {
            FancyToast.makeText(ActivityChat.this, "sent", Toast.LENGTH_SHORT).show();
            messageStructureList.add(message);
          } else {
            FancyToast.makeText(ActivityChat.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
          }
          refreshMessages();

        });
        edt_sendMessage.setText(null);
      }

    });
  }

  private void refreshMessages() {

    String username = ParseUser.getCurrentUser().getUsername();
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
    query.whereContainedIn(MessageStructure.RECEIVER_USER_NAME, Arrays.asList(username, receiverName));
    query.whereContainedIn(MessageStructure.SENDER_USER_NAME, Arrays.asList(receiverName, username));

//    query.whereEqualTo(MessageStructure.SENDER_USER_NAME, username);
//    query.whereEqualTo(MessageStructure.RECEIVER_USER_NAME,username);
//    query.whereEqualTo(MessageStructure.RECEIVER_USER_NAME, receiverName);
//    query.whereEqualTo(MessageStructure.SENDER_USER_NAME, receiverName);
    query.orderByAscending("createdAt");
    query.findInBackground((serverMessages, e) -> {
      if (e == null) {
        messageStructureList.clear();
        if (serverMessages.size() > 0) {
          for (ParseObject message : serverMessages) {
            MessageStructure structure = new MessageStructure();
            structure.setMessageBody(message.getString(MessageStructure.BODY));
            structure.setReceiverUserName(message.getString(MessageStructure.RECEIVER_USER_NAME));
            structure.setSenderUserName(message.getString(MessageStructure.SENDER_USER_NAME));
            messageStructureList.add(structure);
          }
          rv_chat.setAdapter(new ChatRecyclerAdapter(ActivityChat.this, messageStructureList));
          rv_chat.scrollToPosition(messageStructureList.size() - 1);
        }
      }
    });

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    rv_chat.setLayoutManager(linearLayoutManager);

  }

  private void initializeUi() {
    edt_sendMessage = findViewById(R.id.edt_sendMessage);
    img_send = findViewById(R.id.img_send);
    rv_chat = findViewById(R.id.rv_chat);
  }
}