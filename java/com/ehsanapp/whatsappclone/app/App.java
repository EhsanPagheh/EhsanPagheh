package com.ehsanapp.whatsappclone.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ehsanapp.whatsappclone.R;
import com.ehsanapp.whatsappclone.structure.MessageStructure;
import com.parse.Parse;
import com.parse.ParseObject;

import androidx.appcompat.app.AppCompatActivity;

public class App extends Application {

  private static Context app;

  @Override
  public void onCreate() {
    super.onCreate();
    app = this;

    ParseObject.registerSubclass(MessageStructure.class);
    Parse.initialize(new Parse.Configuration.Builder(this)
      .applicationId(this.getString(R.string.back4app_app_id))
      .clientKey(this.getString(R.string.back4app_client_key))
      .server(this.getString(R.string.back4app_server_url))
      .build()
    );
  }

  public static void hideKeyboared(View view) {
    InputMethodManager iMM = (InputMethodManager) app.getSystemService(Context.INPUT_METHOD_SERVICE);
    iMM.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  public static void transit(Context context, Class<?> appCompatActivityClass, boolean hasFinishActivity) {
    Intent intent = new Intent(context, appCompatActivityClass);
    context.startActivity(intent);
    if (hasFinishActivity) {
      AppCompatActivity activity = (AppCompatActivity) context;
      activity.finish();
    }
  }

  public static void transit(Context context, Class<?> appCompatActivityClass, boolean hasFinishActivity, String title) {
    Intent intent = new Intent(context, appCompatActivityClass);
    intent.putExtra("ACTIONBAR_TITLE", title);
    context.startActivity(intent);
    if (hasFinishActivity) {
      AppCompatActivity activity = (AppCompatActivity) context;
      activity.finish();
    }
  }
}
