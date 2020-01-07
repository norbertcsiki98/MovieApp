package com.example.movieapp.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.database.DatabaseHelper;
import com.example.movieapp.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText usernameView, emailView, passwordView, confirmpasswordView;
    Button registerButton;
    String username, email, password, confirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new DatabaseHelper(this);
        usernameView = findViewById(R.id.username);
        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);
        confirmpasswordView = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.register_button);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameView.getText().toString();
                email = emailView.getText().toString();
                password = passwordView.getText().toString();
                confirmpassword = confirmpasswordView.getText().toString();
                if (checkDataEntered() == true) {
                    if (password.equals(confirmpassword)) {
                        if ( true) {
                            boolean insert = db.insertUser(username, password);
                            if (insert == true) {
                                Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                //intent.putExtra("email",emailS);
                                startActivity(intent);

                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Enter other email", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "The passwords don't match", Toast.LENGTH_LONG).show();
                    }

                }


            }
        });

    }


    //check data entered
    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }


    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }


    boolean checkDataEntered() {
        boolean dataValidation = true;
        if (isEmpty(usernameView)) {
            usernameView.setError("Username is required!");
            dataValidation = false;
        }


        if (isEmail(emailView) == false) {
            emailView.setError("Enter valid email!");
            dataValidation = false;
        }

        if (isEmpty(passwordView)) {
            passwordView.setError("Password is required!");
            dataValidation = false;
        }
        if (isEmpty(confirmpasswordView)) {
            confirmpasswordView.setError("Confirm password is required!");
            dataValidation = false;
        }
        return dataValidation;
    }
}
