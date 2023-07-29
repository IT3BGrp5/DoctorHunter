package com.healthcare.medicfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText password2EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.register);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        password2EditText = findViewById(R.id.password2);

        TextView textView = findViewById(R.id.login);
        TextView regasdoc = findViewById(R.id.regAsDoc);
        Button registerBtn = findViewById(R.id.registerBtn);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        regasdoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this, register2.class);
                startActivity(intent);
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String password2 = password2EditText.getText().toString().trim();

                if (validateInputs(username, password, password2)) {
                    saveUserInfoToFirebase(username, password);
                }
            }
        });
    }

    private boolean validateInputs(String username, String password, String password2) {
        if (username.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            Toast.makeText(register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!password.equals(password2)) {
            Toast.makeText(register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveUserInfoToFirebase(String username, String password) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        // Use the username as the key for the user in the database
        usersRef.child(username).child("username").setValue(username);
        usersRef.child(username).child("password").setValue(password);
        usernameEditText.setText("");
        passwordEditText.setText("");
        password2EditText.setText("");
        Toast.makeText(register.this, "User registered successfully", Toast.LENGTH_SHORT).show();
    }
}
