package com.healthcare.medicfinder;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class medic extends AppCompatActivity {

    private DatabaseReference medicsRef;
    private SharedPreferences sharedPreferences;

    private TextView fullNameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView addressTextView;
    private TextView medTypeTextView;
    private TextView timeToFromTextView;
    private TextView availDaysView;


    private TextView availStatusView;
    private TextView paymentTextView;
    private Button logoutButton;
    private Button deleteAccountButton;
    private Button updateLocation;

    private Button updatePhone;
    private Button paymentMethods;
    private Button availDays;

    private Button availStatus;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.medic);

        medicsRef = FirebaseDatabase.getInstance().getReference("medics");
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        fullNameTextView = findViewById(R.id.fullname);
        emailTextView = findViewById(R.id.email);
        phoneTextView = findViewById(R.id.phone);
        addressTextView = findViewById(R.id.address);
        medTypeTextView = findViewById(R.id.medtype);
        timeToFromTextView = findViewById(R.id.timetofrom);
        logoutButton = findViewById(R.id.logoutbtn);
        deleteAccountButton = findViewById(R.id.deleteacc);
        paymentTextView = findViewById(R.id.payments);
        updateLocation = findViewById(R.id.updateLoc);
        updatePhone = findViewById(R.id.updatephone);
        paymentMethods = findViewById(R.id.paymentBtn);
        availDays = findViewById(R.id.availDays);
        availDaysView = findViewById(R.id.AvDays);

        String email = sharedPreferences.getString("email", "");

        medicsRef.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fullName = dataSnapshot.child("fullname").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);
                    String medType = dataSnapshot.child("medicalPersonnelType").getValue(String.class);
                    String timeFrom = dataSnapshot.child("fromTime").getValue(String.class);
                    String timeTo = dataSnapshot.child("toTime").getValue(String.class);
                    String payment = dataSnapshot.child("payment_methods").getValue(String.class);
                    String avdays = dataSnapshot.child("avail_days").getValue(String.class);

                    fullNameTextView.setText("Fullname: " + fullName);
                    emailTextView.setText("Email: " + email);
                    phoneTextView.setText("Phone: " + phone);
                    addressTextView.setText("Address: " + address);
                    medTypeTextView.setText("Specialization: " + medType);
                    paymentTextView.setText("Payment: " + payment);
                    timeToFromTextView.setText("Active From: " + timeFrom + " to " + timeTo);
                    availDaysView.setText("Availability: " + avdays);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Error occurred while accessing the database
                Toast.makeText(medic.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Redirect to login2.java
                Intent intent = new Intent(medic.this, login2.class);
                startActivity(intent);
                finish();
            }
        });


        updateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLocation();
            }
        });
        updatePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpdatePhone();
            }
        });
        paymentMethods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePayment();
            }
        });
        availDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                availableDays();
            }
        });
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete current data from Firebase
                String email = sharedPreferences.getString("email", "");
                medicsRef.child(email).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            // Clear SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();

                            Toast.makeText(medic.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                            // Redirect to login2.java
                            Intent intent = new Intent(medic.this, login2.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Error occurred while deleting account
                            Toast.makeText(medic.this, "Failed to delete account: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void updateLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Location");
        builder.setMessage("What is your current location?");

        // Set up the input field
        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String location = input.getText().toString().trim();

                if (!location.isEmpty()) {
                    String email = sharedPreferences.getString("email", "");
                    medicsRef.child(email).child("address").setValue(location, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(medic.this, "Location updated successfully", Toast.LENGTH_SHORT).show();
                                // Reload data after updating location
                                medicsRef.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            String newLocation = dataSnapshot.child("address").getValue(String.class);
                                            addressTextView.setText("Address: "+newLocation);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Error occurred while accessing the database
                                        Toast.makeText(medic.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                // Error occurred while updating location
                                Toast.makeText(medic.this, "Failed to update location: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // Location input is empty
                    Toast.makeText(medic.this, "Please enter a location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void setUpdatePhone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Information");
        builder.setMessage("Please enter your new phone number:");

        // Set up the input field
        final EditText phoneInput = new EditText(this);
        phoneInput.setHint("Phone Number");

        // Create a layout to hold the input field
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(phoneInput);

        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String phone = phoneInput.getText().toString().trim();

                if (!phone.isEmpty()) {
                    String email = sharedPreferences.getString("email", "");
                    medicsRef.child(email).child("phone").setValue(phone, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(medic.this, "Phone number updated successfully", Toast.LENGTH_SHORT).show();
                                // Reload data after updating phone number
                                medicsRef.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            String newPhone = dataSnapshot.child("phone").getValue(String.class);
                                            phoneTextView.setText("Phone Number: " + newPhone);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Error occurred while accessing the database
                                        Toast.makeText(medic.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                // Error occurred while updating phone number
                                Toast.makeText(medic.this, "Failed to update phone number: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // Phone number input is empty
                    Toast.makeText(medic.this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updatePayment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Payment Options");

        View checkBoxView = View.inflate(this, R.layout.dialog_payment_methods, null);
        builder.setView(checkBoxView);

        final LinearLayout checkboxContainer = checkBoxView.findViewById(R.id.checkboxContainer);

        // Add checkboxes for each payment method
        addCheckBox(checkboxContainer, "ASIANCARE HEALTH SYSTEMS, INC.");
        addCheckBox(checkboxContainer, "COCOLIFE");
        addCheckBox(checkboxContainer, "COOPERATIVE HEALTH MANAGEMENT FEDERATION");
        addCheckBox(checkboxContainer, "EASTWEST HEALTHCARE INC.");
        addCheckBox(checkboxContainer, "HEALTH PLAN PHILIPPINES, INC. (HPPI)");
        addCheckBox(checkboxContainer, "INSULAR HEALTH CARE, INC. (FORMERLY ICARE)");
        addCheckBox(checkboxContainer, "MAXICARE");
        addCheckBox(checkboxContainer, "MEDICARD");
        addCheckBox(checkboxContainer, "MEDICARE PLUS INC.");
        addCheckBox(checkboxContainer, "MEDOCARE HEALTH SYSTEMS, INC.");
        addCheckBox(checkboxContainer, "METROCARE HEALTHCARE SYSTEMS, INC");
        addCheckBox(checkboxContainer, "OPTIMUM MEDICAL AND HEALTHCARE SERVICES, INC.");
        addCheckBox(checkboxContainer, "PACIFIC CROSS HEALTH CARE, INC..");
        addCheckBox(checkboxContainer, "PHILCARE PHILHEALTH CARE, INC.");
        addCheckBox(checkboxContainer, "CASH");
        addCheckBox(checkboxContainer, "GCASH");
        addCheckBox(checkboxContainer, "BANK");

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder paymentOptions = new StringBuilder();

                for (int i = 0; i < checkboxContainer.getChildCount(); i++) {
                    View view = checkboxContainer.getChildAt(i);
                    if (view instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) view;
                        if (checkBox.isChecked()) {
                            paymentOptions.append(checkBox.getText().toString()).append(", ");
                        }
                    }
                }

                if (paymentOptions.length() > 0) {
                    // Remove trailing comma and space
                    paymentOptions.setLength(paymentOptions.length() - 2);

                    String email = sharedPreferences.getString("email", "");
                    DatabaseReference userRef = medicsRef.child(email);
                    userRef.child("payment_methods").setValue(paymentOptions.toString());

                    paymentTextView.setText("Payment: " + paymentOptions.toString());
                    Toast.makeText(medic.this, "Payment options updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // No payment options selected
                    Toast.makeText(medic.this, "Please select payment options", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addCheckBox(LinearLayout container, String text) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(text);
        container.addView(checkBox);
    }



    private void availableDays() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Available Days");

        // Inflate the custom dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_days_of_week, null);
        builder.setView(dialogView);

        // Get the CheckBox views from the dialog layout
        CheckBox cbMonday = dialogView.findViewById(R.id.cbMonday);
        CheckBox cbTuesday = dialogView.findViewById(R.id.cbTuesday);
        CheckBox cbWednesday = dialogView.findViewById(R.id.cbWednesday);
        CheckBox cbThursday = dialogView.findViewById(R.id.cbThursday);
        CheckBox cbFriday = dialogView.findViewById(R.id.cbFriday);
        CheckBox cbSaturday = dialogView.findViewById(R.id.cbSaturday);
        CheckBox cbSunday = dialogView.findViewById(R.id.cbSunday);
        CheckBox cbOnLeave = dialogView.findViewById(R.id.cbOnLeave);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder selectedDaysBuilder = new StringBuilder();

                // Check if each CheckBox is selected and append to the StringBuilder
                if (cbMonday.isChecked()) {
                    selectedDaysBuilder.append("Monday, ");
                }
                if (cbTuesday.isChecked()) {
                    selectedDaysBuilder.append("Tuesday, ");
                }
                if (cbWednesday.isChecked()) {
                    selectedDaysBuilder.append("Wednesday, ");
                }
                if (cbThursday.isChecked()) {
                    selectedDaysBuilder.append("Thursday, ");
                }
                if (cbFriday.isChecked()) {
                    selectedDaysBuilder.append("Friday, ");
                }
                if (cbSaturday.isChecked()) {
                    selectedDaysBuilder.append("Saturday, ");
                }
                if (cbSunday.isChecked()) {
                    selectedDaysBuilder.append("Sunday, ");
                }
                if (cbOnLeave.isChecked()) {
                    selectedDaysBuilder.append("On Leave, ");
                }

                String selectedDays = selectedDaysBuilder.toString().trim();

                if (!selectedDays.isEmpty()) {
                    String email = sharedPreferences.getString("email", "");
                    DatabaseReference userRef = medicsRef.child(email);

                    userRef.child("avail_days").setValue(selectedDays);
                    Toast.makeText(medic.this, "Available days updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(medic.this, "Please select at least one day", Toast.LENGTH_SHORT).show();
                }


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
