package com.healthcare.medicfinder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private Button previousButton;

    public Button btnType;
    public String selectedSpecialty;
    public Button btnLocation;
    public String selectedCity;
    public Button btnDays;
    public StringBuilder selectedDays;
    public Button btnSearchFinal;
    public String finalParam;
    public String[] cities = {
            "Angeles City",
            "Antipolo",
            "Bacolod",
            "Bacoor",
            "Bago",
            "Baguio",
            "Bais",
            "Balanga",
            "Batac",
            "Batangas City",
            "Bayawan",
            "Baybay",
            "Bayugan",
            "Biñan",
            "Bislig",
            "Bogo",
            "Borongan",
            "Butuan",
            "Cabadbaran",
            "Cabanatuan",
            "Cabuyao",
            "Cadiz",
            "Cagayan de Oro",
            "Calamba",
            "Calapan",
            "Calbayog",
            "Caloocan",
            "Candon",
            "Canlaon",
            "Carcar",
            "Catbalogan",
            "Cauayan",
            "Cavite City",
            "Cebu City",
            "Cotabato City",
            "Dagupan",
            "Danao",
            "Dapitan",
            "Dasmariñas",
            "Davao City",
            "Digos",
            "Dipolog",
            "Dumaguete",
            "El Salvador",
            "Escalante",
            "Gapan",
            "General Santos",
            "General Trias",
            "Gingoog",
            "Guihulngan",
            "Himamaylan",
            "Ilagan",
            "Iligan",
            "Iloilo City",
            "Imus",
            "Iriga",
            "Isabela",
            "Kabankalan",
            "Kidapawan",
            "Koronadal",
            "La Carlota",
            "Lamitan",
            "Laoag",
            "Lapu-Lapu",
            "Las Piñas",
            "Legazpi",
            "Ligao",
            "Lipa",
            "Lucena",
            "Maasin",
            "Mabalacat",
            "Makati City",
            "Malabon",
            "Malaybalay",
            "Malolos",
            "Mandaluyong",
            "Mandaue",
            "Manila",
            "Marawi",
            "Marikina City",
            "Masbate City",
            "Mati",
            "Meycauayan",
            "Muñoz",
            "Muntinlupa",
            "Naga",
            "Navotas",
            "Olongapo",
            "Ormoc",
            "Oroquieta",
            "Ozamiz",
            "Pagadian",
            "Palayan",
            "Panabo",
            "Parañaque",
            "Pasay",
            "Pasig",
            "Passi",
            "Puerto Princesa",
            "Quezon City",
            "Roxas",
            "Rizal",
            "Sagay",
            "Samal",
            "San Carlos",
            "San Fernando",
            "San Jose",
            "San Jose del Monte",
            "San Juan",
            "San Pablo",
            "San Pedro",
            "Santa Rosa",
            "Santiago",
            "Silay",
            "Sipalay",
            "Sorsogon City",
            "Surigao City",
            "Tabaco",
            "Tabuk",
            "Tacloban",
            "Tacurong",
            "Tagaytay",
            "Tagbilaran",
            "Taguig",
            "Tagum",
            "Talisay",
            "Talisay",
            "Tanauan",
            "Tandag",
            "Tangub",
            "Tanjay",
            "Tarlac City",
            "Tayabas",
            "Toledo",
            "Trece Martires",
            "Tuguegarao",
            "Urdaneta",
            "Valencia",
            "Valenzuela",
            "Victorias",
            "Vigan",
            "Zamboanga City"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button logoutButton = findViewById(R.id.logoutbtn);
        Button showButton = findViewById(R.id.show);
       // Button scanButton = findViewById(R.id.scan);
        // Button searchLoc = findViewById(R.id.searchLoc);
        // Button searchProf = findViewById(R.id.searchProf);
        WebView webView = findViewById(R.id.webs);



        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Redirect to login2.java
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changeButtonColor(showButton);
                //changeButtonColor(scanButton);
                //previousButton = showButton;

                String url = "file:///android_asset/medic.html";

                WebView webView = (WebView) findViewById(R.id.webs);

                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);

                webView.loadUrl(url);
            }
        });

    // Specilization List
        btnType = findViewById(R.id.type);
        btnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpecialtyListDialog();
            }
        });

     // Location list
        btnLocation = findViewById(R.id.location);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCityListDialog();
            }
        });
    // Days list
        btnDays = findViewById(R.id.day);
        selectedDays = new StringBuilder();
        btnDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDaysOfWeekDialog();
            }
        });


        // Search Based oN Filter
        btnSearchFinal = findViewById(R.id.search);
        finalParam = selectedSpecialty + ","+ selectedCity+","+selectedDays;
        btnSearchFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFilter(finalParam);
            }
        });
    }


    private void changeButtonColor(Button button) {
        if (previousButton != null && previousButton != button) {
            previousButton.setTextColor(Color.BLACK);
            previousButton.setBackgroundColor(Color.WHITE);
        }
        button.setTextColor(Color.WHITE);
        button.setBackgroundColor(Color.RED);
    }


    private void showSpecialtyListDialog() {
        final String[] specialties = {
                "Immunology",
                "Surgeon",
                "Pediatricians",
                "Radiologists",
                "Cardiologist",
                "Ophthalmologist",
                "Neurosurgery",
                "Dermatologist",
                "Orthopedics",
                "Oncology",
                "Family Physicians",
                "Hematologists",
                "Obstetricians",
                "Plastic Surgeons",
                "Psychiatrist",
                "Urologist",
                "Dentist"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select Specialty");
        builder.setItems(specialties, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedSpecialty = specialties[which];
                btnType.setText(selectedSpecialty);
                Toast.makeText(MainActivity.this, "Selected Specialty: " + selectedSpecialty, Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }


    private void showCityListDialog() {
        Arrays.sort(cities);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select City");
        builder.setItems(cities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedCity = cities[which];
                btnLocation.setText(selectedCity);
                Toast.makeText(MainActivity.this, "Selected City: " + selectedCity, Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void showDaysOfWeekDialog() {
        final ArrayList<String> selectedDaysList = new ArrayList<>();

        View checkBoxView = View.inflate(this, R.layout.dialog_days_of_week, null);

        CheckBox cbMonday = checkBoxView.findViewById(R.id.cbMonday);
        CheckBox cbTuesday = checkBoxView.findViewById(R.id.cbTuesday);
        CheckBox cbWednesday = checkBoxView.findViewById(R.id.cbWednesday);
        CheckBox cbThursday = checkBoxView.findViewById(R.id.cbThursday);
        CheckBox cbFriday = checkBoxView.findViewById(R.id.cbFriday);
        CheckBox cbSaturday = checkBoxView.findViewById(R.id.cbSaturday);
        CheckBox cbSunday = checkBoxView.findViewById(R.id.cbSunday);
        CheckBox cbOnLeave = checkBoxView.findViewById(R.id.cbOnLeave);

        // Restore previously selected days
        String[] previouslySelectedDays = selectedDays.toString().split(",");
        for (String day : previouslySelectedDays) {
            if (day.equals("Monday")) {
                cbMonday.setChecked(true);
                selectedDaysList.add("Monday");
            } else if (day.equals("Tuesday")) {
                cbTuesday.setChecked(true);
                selectedDaysList.add("Tuesday");
            } else if (day.equals("Wednesday")) {
                cbWednesday.setChecked(true);
                selectedDaysList.add("Wednesday");
            } else if (day.equals("Thursday")) {
                cbThursday.setChecked(true);
                selectedDaysList.add("Thursday");
            } else if (day.equals("Friday")) {
                cbFriday.setChecked(true);
                selectedDaysList.add("Friday");
            } else if (day.equals("Saturday")) {
                cbSaturday.setChecked(true);
                selectedDaysList.add("Saturday");
            } else if (day.equals("Sunday")) {
                cbSunday.setChecked(true);
                selectedDaysList.add("Sunday");
            } else if (day.equals("On Leave")) {
                cbOnLeave.setChecked(true);
                selectedDaysList.add("On Leave");
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select Days of the Week");
        builder.setView(checkBoxView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedDays.setLength(0); // Clear the string builder

                for (String day : selectedDaysList) {
                    selectedDays.append(day).append(", ");
                }

                if (selectedDays.length() > 0) {
                    // Remove trailing comma and space
                    selectedDays.setLength(selectedDays.length() - 2);

                    btnDays.setText(selectedDays.toString());
                    Toast.makeText(MainActivity.this, "Selected Days: " + selectedDays.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    btnDays.setText("Select Days");
                    Toast.makeText(MainActivity.this, "No days selected.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();

        cbMonday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedDaysList(isChecked, "Monday", selectedDaysList);
            }
        });

        cbTuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedDaysList(isChecked, "Tuesday", selectedDaysList);
            }
        });

        cbWednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedDaysList(isChecked, "Wednesday", selectedDaysList);
            }
        });

        cbThursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedDaysList(isChecked, "Thursday", selectedDaysList);
            }
        });

        cbFriday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedDaysList(isChecked, "Friday", selectedDaysList);
            }
        });

        cbSaturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedDaysList(isChecked, "Saturday", selectedDaysList);
            }
        });

        cbSunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedDaysList(isChecked, "Sunday", selectedDaysList);
            }
        });

        cbOnLeave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedDaysList(isChecked, "On Leave", selectedDaysList);
            }
        });
    }

    private void updateSelectedDaysList(boolean isChecked, String day, ArrayList<String> selectedDaysList) {
        if (isChecked) {
            selectedDaysList.add(day);
        } else {
            selectedDaysList.remove(day);
        }
    }


    private void updateSelectedStatusList(boolean isChecked, String day, ArrayList<String> selectedStatusList) {
        if (isChecked) {
            selectedStatusList.add(day);
        } else {
            selectedStatusList.remove(day);
        }
    }

    public String param1 = "", param2 = "", param3 = "";

    private void searchFilter(String paramser) {

        param1 = btnType.getText().toString();
        param2 = btnLocation.getText().toString();
        param3 = btnDays.getText().toString();

        if (param1.contains("type")) {param1 = "";}
        if (param2.contains("location")) {param2 = "";}
        if (param3.contains("avail day")) {param3 = "";}

        //Toast.makeText(MainActivity.this, "paramser="+param1+"&params2="+param2+ "&params3=" +param3, Toast.LENGTH_LONG).show();
        String url = "file:///android_asset/search.html?paramser="+param1+"&params2="+param2+ "&params3=" +param3;

        WebView webView = (WebView) findViewById(R.id.webs);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl(url);
    }

}
