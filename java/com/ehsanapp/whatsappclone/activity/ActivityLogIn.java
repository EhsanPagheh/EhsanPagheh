package com.ehsanapp.whatsappclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ehsanapp.whatsappclone.R;
import com.ehsanapp.whatsappclone.app.App;
import com.ehsanapp.whatsappclone.helper.ParseUserRunner;
import com.parse.ParseUser;

public class ActivityLogIn extends AppCompatActivity {

  EditText edt_userName, edt_password;
  Button btn_logIn;
  TextView txt_singUp;
  ConstraintLayout log_cl_root;

  String userName, password;

  ParseUserRunner runner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_log_in);
    initialize();


    if (ParseUser.getCurrentUser() != null){
      Intent intent = new Intent(ActivityLogIn.this, ActivityUserList.class);
      startActivity(intent);
      finish();
    }

    log_cl_root.setOnClickListener(App::hideKeyboared);

    btn_logIn.setOnClickListener(view -> {
      getEditsText();
      if (isInputFormValid()) {
        runner.logIn(userName, password, (successful, error) -> {
          if (!successful){
            Toast.makeText(ActivityLogIn.this, error.getMessage(), Toast.LENGTH_SHORT).show();
          }else{
            Intent intent = new Intent(ActivityLogIn.this, ActivityUserList.class);
            startActivity(intent);
            ActivityLogIn.this.finish();
          }
        });
      }
    });

    edt_password.setOnKeyListener((view, i, keyEvent) -> {
      if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
        btn_logIn.callOnClick();
        return true;
      }
      return false;
    });

    txt_singUp.setOnClickListener(view -> {
      App.transit(ActivityLogIn.this,  ActivitySignUp.class, true);
    });
  }

  private void initialize() {
    edt_password = findViewById(R.id.edt_password);
    edt_userName = findViewById(R.id.edt_userName);
    btn_logIn = findViewById(R.id.btn_logIn);
    txt_singUp = findViewById(R.id.txt_singUp);
    log_cl_root = findViewById(R.id.log_cl_root);

    runner = new ParseUserRunner(this);
  }

  private void getEditsText() {
    userName = edt_userName.getText().toString();
    password = edt_password.getText().toString();
  }


  private boolean isInputFormValid() {
    edt_userName.setError(null);
    edt_password.setError(null);

    //username or email address
    if (userName == null || userName.isEmpty()) {
      edt_userName.setError(getString(R.string.error_username));
      edt_userName.requestFocus();
      return false;
    }
    if (password == null || password.isEmpty()) {
      edt_password.setError(getString(R.string.error_password));
      edt_password.requestFocus();
      return false;
    }
    return true;
  }
}