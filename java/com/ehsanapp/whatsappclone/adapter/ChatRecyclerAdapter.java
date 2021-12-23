package com.ehsanapp.whatsappclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ehsanapp.whatsappclone.R;
import com.ehsanapp.whatsappclone.structure.MessageStructure;
import com.parse.ParseUser;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder> {

  private final List<MessageStructure> chatHistoryList;
  private final LayoutInflater inflater;

  private static final int INCOMING_MESSAGE = 111;
  private static final int OUTGOING_MESSAGE = 222;

  private static String getProfileUrl(String userId) {
    String hex = "";
    try {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      byte[] hash = digest.digest(userId.getBytes());
      BigInteger bigInteger = new BigInteger(hash);
      hex = bigInteger.abs().toString(16);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "https://www.gravatar.com/avatar/" + hex + "?d= identicon";
  }

  public ChatRecyclerAdapter(Context context, List<MessageStructure> chatHistoryList) {
    this.chatHistoryList = chatHistoryList;
    inflater = LayoutInflater.from(context);
  }

  @Override
  public int getItemViewType(int position) {

    String currentUser = ParseUser.getCurrentUser().getUsername();
    MessageStructure structure = chatHistoryList.get(position);
      if (structure.getSenderUserName().equals(currentUser)) {
        return OUTGOING_MESSAGE;
      } else {
        return INCOMING_MESSAGE;
      }
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    int resourse;
    if (viewType == INCOMING_MESSAGE) {
      resourse = R.layout.layout_incoming_chat;
      View view = inflater.inflate(resourse, parent, false);
      return new ViewHolder.IncomingMessageViewHolder(view);
    } else {
      resourse = R.layout.layout_outgoing_chat;
      View view = inflater.inflate(resourse, parent, false);
      return new ViewHolder.OutGoingMessageViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    MessageStructure messageStructure = (MessageStructure) chatHistoryList.get(position);
    holder.bindMessage(messageStructure);
  }


  @Override
  public int getItemCount() {
    return chatHistoryList.size();
  }

  public static abstract class ViewHolder extends RecyclerView.ViewHolder {

    TextView txt_body;

    public abstract void bindMessage(MessageStructure messageStructure);

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
    }

    public static class OutGoingMessageViewHolder extends ViewHolder {

      ImageView img_profileMe;

      public OutGoingMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        img_profileMe = itemView.findViewById(R.id.img_profileMe);
        txt_body = itemView.findViewById(R.id.txt_body);
      }

      @Override
      public void bindMessage(MessageStructure messageStructure) {
        txt_body.setText(messageStructure.getMessageBody());
      }
    }

    public static class IncomingMessageViewHolder extends ViewHolder {

      TextView txt_name;
      ImageView img_profileOther;

      public IncomingMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_body = itemView.findViewById(R.id.txt_body);
        txt_name = itemView.findViewById(R.id.txt_name);
        img_profileOther = itemView.findViewById(R.id.img_profileOther);
      }

      @Override
      public void bindMessage(MessageStructure message) {
          /*
        Glide.with(context)
          .load(getProfileUrl(message.getUserId()))
          .circleCrop()
          .into(img_profileOther);
           */
        txt_name.setText(message.getReceiverUserName());
        txt_body.setText(message.getMessageBody());
      }
    }
  }
}
