package com.example.assignment1;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class EditTripActivity extends AppCompatActivity {
    private static final String TAG = "EditTripActivity";
    private EditText etDestination, etBudget, etStartDate, etEndDate;
    private RadioGroup rgTripType;
    private Switch switchBooked;
    private CheckBox cbPacking;
    private Button btnUpdate;
    private TripStorage tripStorage;
    private Trip currentTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);
        Log.d(TAG, "onCreate called");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Trip");

        initViews();
        loadTripData();
        setupDatePickers();
        setupUpdateButton();
    }

    private void initViews() {
        etDestination = findViewById(R.id.etDestination);
        etBudget = findViewById(R.id.etBudget);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        rgTripType = findViewById(R.id.rgTripType);
        switchBooked = findViewById(R.id.switchBooked);
        cbPacking = findViewById(R.id.cbPacking);
        btnUpdate = findViewById(R.id.btnUpdate);
        tripStorage = new TripStorage(this);
    }

    private void loadTripData() {
        currentTrip = (Trip) getIntent().getSerializableExtra("trip");
        if (currentTrip == null) {
            Toast.makeText(this, "Error loading trip", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etDestination.setText(currentTrip.getDestination());
        //etBudget.setText(String.valueOf(currentTrip.getBudget()));
        etStartDate.setText(currentTrip.getStartDate());
        etEndDate.setText(currentTrip.getEndDate());
        //switchBooked.setChecked(currentTrip.isBooked());
        cbPacking.setChecked(currentTrip.isPacked());

        // Set trip type radio button
        String tripType = currentTrip.getTripType();
        if (tripType.equals("Adventure")) {
            rgTripType.check(R.id.rbAdventure);
        } else if (tripType.equals("Relaxation")) {
            rgTripType.check(R.id.rbRelaxation);
        } else if (tripType.equals("Business")) {
            rgTripType.check(R.id.rbBusiness);
        } else if (tripType.equals("Family")) {
            rgTripType.check(R.id.rbFamily);
        }
    }

    private void setupDatePickers() {
        etStartDate.setOnClickListener(v -> showDatePicker(true));
        etEndDate.setOnClickListener(v -> showDatePicker(false));
    }

    private void showDatePicker(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    if (isStartDate) {
                        etStartDate.setText(date);
                    } else {
                        etEndDate.setText(date);
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void setupUpdateButton() {
        btnUpdate.setOnClickListener(v -> updateTrip());
    }

    private void updateTrip() {
        String destination = etDestination.getText().toString().trim();
        String budgetStr = etBudget.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();
        String endDate = etEndDate.getText().toString().trim();

        if (destination.isEmpty()) {
            etDestination.setError("Destination is required");
            return;
        }

        if (budgetStr.isEmpty()) {
            etBudget.setError("Budget is required");
            return;
        }

        if (startDate.isEmpty()) {
            Toast.makeText(this, "Please select start date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endDate.isEmpty()) {
            Toast.makeText(this, "Please select end date", Toast.LENGTH_SHORT).show();
            return;
        }

        double budget;
        try {
            budget = Double.parseDouble(budgetStr);
        } catch (NumberFormatException e) {
            etBudget.setError("Invalid budget");
            return;
        }

        int selectedTypeId = rgTripType.getCheckedRadioButtonId();
        if (selectedTypeId == -1) {
            Toast.makeText(this, "Please select trip type", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedType = findViewById(selectedTypeId);
        String tripType = selectedType.getText().toString();
        boolean isBooked = switchBooked.isChecked();
        boolean packingCompleted = cbPacking.isChecked();

        currentTrip.setDestination(destination);
        currentTrip.setStartDate(startDate);
        currentTrip.setEndDate(endDate);
        //currentTrip.setBudget(budget);
        currentTrip.setTripType(tripType);
        //currentTrip.setBooked(isBooked);
        currentTrip.setPacked(packingCompleted);

        tripStorage.saveTrip(currentTrip);
        Toast.makeText(this, "Trip updated successfully!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Trip updated: " + currentTrip.toString());
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}