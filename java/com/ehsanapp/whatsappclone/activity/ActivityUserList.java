package com.ehsanapp.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ehsanapp.whatsappclone.R;
import com.ehsanapp.whatsappclone.adapter.UserRecyclerAdapter;
import com.ehsanapp.whatsappclone.app.App;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ActivityUserList extends AppCompatActivity {

  RecyclerView rv_userList;
  SwipeRefreshLayout refreshLayout;
  List<String> appUserList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_list);
    rv_userList = findViewById(R.id.rv_userLiat);
    refreshLayout = findViewById(R.id.lay_refresh);
    appUserList = new ArrayList<>();
    getUserQuery();

    refreshLayout.setOnRefreshListener(() -> {
      getUserQuery();
      refreshLayout.setRefreshing(false);
    });

  }

  private void getUserQuery() {
    //this works fine but every time you refresh the recycler it creates a new adapter
    //and I think that is bad for performance
    //the solution is using of query.whereNotContain
    //this query gives you the items that are not in the usersList or they are new

    ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
    userQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
    userQuery.findInBackground((userList, exception) -> {
      if (exception == null) {
        if (userList.size() > 0) {
          Log.i("Ehsan", "size of the uers : " + userList.size());
          appUserList.clear();
          for (ParseUser user : userList) {
            appUserList.add(user.getUsername());
          }
          rv_userList.setAdapter(new UserRecyclerAdapter(ActivityUserList.this, appUserList));
        }
      } else {
        Toast.makeText(ActivityUserList.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  public boolean onCreateOptionsMenu(Menu menu) {
    MenuItem logOutItem = menu.add(10, 10, 10, "Log out '" + ParseUser.getCurrentUser().getUsername() + "'");
    logOutItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == 10) {
      ParseUser.logOutInBackground(e -> {
        if (e == null) {
          App.transit(ActivityUserList.this, ActivityLogIn.class, true);
        } else {
          Toast.makeText(ActivityUserList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      });
    }
    return super.onOptionsItemSelected(item);
  }
}