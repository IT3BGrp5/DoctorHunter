package com.healthcare.medicfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login2 extends AppCompatActivity {

    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private SharedPreferences sharedPreferences;

    private DatabaseReference medicsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login2);

        medicsRef = FirebaseDatabase.getInstance().getReference("medics");
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        // Check if SharedPreferences already contain email and password
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        if (!email.isEmpty() && !password.isEmpty()) {
            // SharedPreferences exist, proceed to medic activity
            Intent intent = new Intent(login2.this, medic.class);
            startActivity(intent);
            finish();
        }

        TextView textView = findViewById(R.id.register);
        TextView textView2 = findViewById(R.id.loginAsDoc);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login2.this, register.class);
                startActivity(intent);
                finish();
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login2.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        Button loginButton = findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String username = usernameEditText.getText().toString().trim().replace(".", ",");
        String password = passwordEditText.getText().toString().trim().replace(".", ",");


        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(login2.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        medicsRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedPassword = dataSnapshot.child("password").getValue(String.class);
                    if (password.equals(storedPassword)) {
                        // Password matches, login successful
                        Toast.makeText(login2.this, "Login successful", Toast.LENGTH_SHORT).show();
                        // Save email (username) and password in SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", username);
                        editor.putString("password", password);
                        editor.apply();
                        // Proceed to the medic activity
                        Intent intent = new Intent(login2.this, medic.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Password does not match
                        Toast.makeText(login2.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // User does not exist
                    Toast.makeText(login2.this, "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error occurred while accessing the database
                Toast.makeText(login2.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
