package com.example.unittestdemo.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unittestdemo.HomeScreenActivity;
import com.example.unittestdemo.R;
import com.example.unittestdemo.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

   
    EditText username_et,password_et;
    Button  login_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        username_et=findViewById(R.id.username);
        password_et=findViewById(R.id.password);
        login_btn=findViewById(R.id.login);
        
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
              //  if()
                
                startActivity(new Intent(LoginActivity.this, HomeScreenActivity.class));
                
            }
        });



    }

   
    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}