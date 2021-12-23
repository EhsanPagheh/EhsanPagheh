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
import com.ehsanapp.whatsappclone.helper.ParseResultListener;
import com.ehsanapp.whatsappclone.helper.ParseUserRunner;

public class ActivitySignUp extends AppCompatActivity {

  EditText edt_name, edt_email, edt_password;
  String name, email, password;
  TextView txt_logIn;

  ConstraintLayout sign_cl_root;

  ParseUserRunner runner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);
    setTitle("Sign Up Activity");
    initialaizeUiWidgets();

    runner = new ParseUserRunner(this);
    Button btn_sign = findViewById(R.id.btn_sign);

    sign_cl_root.setOnClickListener(App::hideKeyboared);

    btn_sign.setOnClickListener(view -> {
      getEditsText();
      if (isInputFormValid()) {
        runner.signUp(name, password, email, (ParseResultListener) (successful, error) -> {
          if (successful) {
            Intent intent = new Intent(ActivitySignUp.this, ActivityUserList.class);
            ActivitySignUp.this.startActivity(intent);
            ActivitySignUp.this.finish();
          } else {
            Toast.makeText(ActivitySignUp.this, error.getMessage(), Toast.LENGTH_SHORT).show();
          }
        });
      }
    });

    edt_password.setOnKeyListener((view, i, keyEvent) -> {
      if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
        btn_sign.callOnClick();
        return true;
      }
      return false;
    });

    txt_logIn.setOnClickListener(view ->
      App.transit(ActivitySignUp.this, ActivityLogIn.class, true)
    );
  }

  private void initialaizeUiWidgets() {
    edt_name = findViewById(R.id.s_edt_name);
    edt_email = findViewById(R.id.s_edt_email);
    edt_password = findViewById(R.id.s_edt_password);
    sign_cl_root = findViewById(R.id.sign_cl_root);

    txt_logIn = findViewById(R.id.txt_logIn);
  }

  private void getEditsText() {
    name = edt_name.getText().toString();
    email = edt_email.getText().toString();
    password = edt_password.getText().toString();
  }

  private boolean isInputFormValid() {
    edt_name.setError(null);
    edt_password.setError(null);
    edt_email.setError(null);

    //username or email address
    if (name == null || name.isEmpty()) {
      edt_name.setError(getString(R.string.error_username));
      edt_name.requestFocus();
      return false;
    }
    if (email == null || email.isEmpty()) {
      edt_email.setError(getString(R.string.error_email));
      edt_email.requestFocus();
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