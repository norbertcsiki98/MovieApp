package com.example.movieapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieapp.MainActivity;
import com.example.movieapp.R;
import com.example.movieapp.database.DatabaseHelper;
import com.example.movieapp.ui.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    Button signUpButton, loginButton;
    EditText usernameView, passwordView;
    DatabaseHelper db;
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameView = findViewById(R.id.username);
        passwordView = findViewById(R.id.password);
        signUpButton = findViewById(R.id.login_sign_up_button);
        loginButton = findViewById(R.id.login_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db = new DatabaseHelper(v.getContext());
                String username = usernameView.getText().toString();
                String password = passwordView.getText().toString();

                if (!checkDataEntered()) {
                    return;
                }

                boolean CheckEmailPassword = db.checkLogin(username, password);
                if (CheckEmailPassword) {
                    SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                    prefs.edit().putString("username", username).apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(v.getContext(), "Successfully login", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public boolean checkDataEntered() {
        boolean dataValidation = true;
        if (isEmpty(usernameView)) {
            usernameView.setError("Email is required!");
            dataValidation = false;
        }

        if (isEmpty(passwordView)) {
            passwordView.setError("Password is required!");
            dataValidation = false;
        }
        return dataValidation;
    }

}
