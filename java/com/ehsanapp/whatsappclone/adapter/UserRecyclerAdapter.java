package com.ehsanapp.whatsappclone.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ehsanapp.whatsappclone.R;
import com.ehsanapp.whatsappclone.activity.ActivityChat;
import com.ehsanapp.whatsappclone.app.App;
import com.ehsanapp.whatsappclone.structure.UsersStructure;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {

  private final List<?> list;
  private final LayoutInflater inflater;
  private final Context context;

  public UserRecyclerAdapter(Context context, List<?> list) {
    this.list = list;
    this.context = context;
    inflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = inflater.inflate(R.layout.layout_users_list, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    String userName = (String) list.get(position);
    holder.txt_position.setText(String.valueOf(position));
    holder.txt_userName.setText(userName);

    holder.lin_userListRoot.setOnClickListener(view -> {
      int currentPosition = holder.getAdapterPosition();
      String title= (String) list.get(currentPosition);
      App.transit(context, ActivityChat.class, false,title);
    });
  }


  @Override
  public int getItemCount() {
    return list.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    TextView txt_userName, txt_position;
    LinearLayout lin_userListRoot;

    public ViewHolder(@NonNull View view) {
      super(view);
      txt_userName = view.findViewById(R.id.txt_userName);
      lin_userListRoot = view.findViewById(R.id.lin_userListRoot);
      txt_position = view.findViewById(R.id.txt_position);
    }
  }
}
