package com.healthcare.medicfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        // Check if SharedPreferences already contain username and password
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        if (!username.isEmpty() && !password.isEmpty()) {
            // SharedPreferences exist, proceed to MainActivity
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        TextView registerTextView = findViewById(R.id.register);
        TextView loginAsDocTextView = findViewById(R.id.loginAsDoc);
        Button loginButton = findViewById(R.id.loginBtn);

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, register.class);
                startActivity(intent);
                finish();
            }
        });

        loginAsDocTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, login2.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (validateInputs(username, password)) {
                    loginUser(username, password);
                }
            }
        });
    }

    private boolean validateInputs(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void loginUser(final String username, final String password) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        usersRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedPassword = dataSnapshot.child("password").getValue(String.class);
                    if (password.equals(storedPassword)) {
                        // Password matches, login successful
                        Toast.makeText(login.this, "Login successful", Toast.LENGTH_SHORT).show();
                        // Save username and password in SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.apply();
                        // Proceed to the main activity
                        Intent intent = new Intent(login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Password does not match
                        Toast.makeText(login.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // User does not exist
                    Toast.makeText(login.this, "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error occurred while accessing the database
                Toast.makeText(login.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
