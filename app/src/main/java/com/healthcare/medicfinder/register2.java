package com.healthcare.medicfinder;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class register2 extends AppCompatActivity {
    private Calendar fromTimeCalendar;
    private Calendar toTimeCalendar;

    private TextView fromTimeTextView;
    private TextView toTimeTextView;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmPasswordEditText;

    private DatabaseReference medicsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.register2);

        // Initialize Firebase database reference
        medicsRef = FirebaseDatabase.getInstance().getReference("medics");

        fromTimeCalendar = Calendar.getInstance();
        toTimeCalendar = Calendar.getInstance();

        fromTimeTextView = findViewById(R.id.from_time);
        toTimeTextView = findViewById(R.id.to_time);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);

        Button registerButton = findViewById(R.id.registerBtn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerMedic();
            }
        });

        TextView textView = findViewById(R.id.login);
        TextView regAsDoc = findViewById(R.id.regAsDoc);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register2.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        regAsDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register2.this, register.class);
                startActivity(intent);
                finish();
            }
        });

        // Set initial time text
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String formattedTime = sdf.format(fromTimeCalendar.getTime());
        fromTimeTextView.setText(formattedTime);


        formattedTime = sdf.format(toTimeCalendar.getTime());
        toTimeTextView.setText(formattedTime);
    }


    public void showFromTimePickerDialog(View view) {
        Calendar calendar = Calendar.getInstance(); // Initialize the Calendar object

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = false; // Set to false for 12-hour format

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                fromTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                fromTimeCalendar.set(Calendar.MINUTE, 0);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String formattedTime = sdf.format(fromTimeCalendar.getTime());

                fromTimeTextView.setText(formattedTime);
            }
        }, hour, minute, is24HourFormat);

        timePickerDialog.show();
    }


    public void showToTimePickerDialog(View view) {
        int hour = toTimeCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = toTimeCalendar.get(Calendar.MINUTE);
        boolean is24HourFormat = false; // Set to false for 12-hour format

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                toTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                toTimeCalendar.set(Calendar.MINUTE, 0);

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String formattedTime = sdf.format(toTimeCalendar.getTime());

                toTimeTextView.setText(formattedTime);
            }
        }, hour, minute, is24HourFormat);

        timePickerDialog.show();
    }


    private void clearInputFields() {
        ((TextInputEditText) findViewById(R.id.fullname)).setText("");
        ((TextInputEditText) findViewById(R.id.email)).setText("");
        ((TextInputEditText) findViewById(R.id.phone)).setText("");
        ((TextInputEditText) findViewById(R.id.address)).setText("");
        ((TextInputEditText) findViewById(R.id.medical_personnel_type)).setText("");
        passwordEditText.setText("");
        confirmPasswordEditText.setText("");
        fromTimeTextView.setText("");
        toTimeTextView.setText("");
    }

    private void registerMedic() {
        String fullname = ((TextInputEditText) findViewById(R.id.fullname)).getText().toString().trim();
        String email = ((TextInputEditText) findViewById(R.id.email)).getText().toString().trim();
        String phone = ((TextInputEditText) findViewById(R.id.phone)).getText().toString().trim();
        String address = ((TextInputEditText) findViewById(R.id.address)).getText().toString().trim();
        String medicalPersonnelType = ((TextInputEditText) findViewById(R.id.medical_personnel_type)).getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String fromTime = fromTimeTextView.getText().toString().trim();
        String toTime = toTimeTextView.getText().toString().trim();

        // Check if password and confirm password match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if all fields are filled
        if (fullname.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()
                || medicalPersonnelType.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
                || fromTime.isEmpty() || toTime.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a unique ID for the medic using the email address
        String id = email.replace(".", ",");

        // Get the current timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = sdf.format(Calendar.getInstance().getTime());

        // Create a HashMap to store the medic's information
        HashMap<String, Object> medicData = new HashMap<>();
        medicData.put("fullname", fullname);
        medicData.put("email", email);
        medicData.put("phone", phone);
        medicData.put("address", address);
        medicData.put("medicalPersonnelType", medicalPersonnelType);
        medicData.put("password", password); // Add the password to the HashMap
        medicData.put("fromTime", fromTime);
        medicData.put("toTime", toTime);
        medicData.put("status", "Available");
        medicData.put("timestamp", timestamp);

        // Save the medic's data to the Firebase database
        medicsRef.child(id).setValue(medicData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(register2.this, "Medic registered successfully", Toast.LENGTH_SHORT).show();
                            clearInputFields();
                        } else {
                            Toast.makeText(register2.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
