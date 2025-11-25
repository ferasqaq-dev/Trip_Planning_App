package com.example.assignment1;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import java.util.Calendar;
import java.util.UUID;

public class AddTripActivity extends AppCompatActivity {

    private EditText destinationEditText;
    private Button startDateButton;
    private Button endDateButton;
    private RadioGroup tripTypeRadioGroup;
    private CheckBox packedCheckBox;
    private SwitchCompat insuranceSwitch;
    private Button saveTripButton;
    private TextView titleTextView;

    private String startDate = "";
    private String endDate = "";
    private TripStorage storage;
    private String tripId;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        storage = new TripStorage(this);

        destinationEditText = findViewById(R.id.destinationEditText);
        startDateButton = findViewById(R.id.startDateButton);
        endDateButton = findViewById(R.id.endDateButton);
        tripTypeRadioGroup = findViewById(R.id.tripTypeRadioGroup);
        packedCheckBox = findViewById(R.id.packedCheckBox);
        insuranceSwitch = findViewById(R.id.insuranceSwitch);
        saveTripButton = findViewById(R.id.saveTripButton);
        titleTextView = findViewById(R.id.titleTextView);

        tripId = getIntent().getStringExtra("TRIP_ID");
        if (tripId != null) {
            isEditMode = true;
            titleTextView.setText("Edit Trip");
            saveTripButton.setText("Update Trip");
            loadTripData();
        } else {
            tripId = UUID.randomUUID().toString();
        }

        startDateButton.setOnClickListener(v -> showDatePicker(true));
        endDateButton.setOnClickListener(v -> showDatePicker(false));
        saveTripButton.setOnClickListener(v -> saveTrip());
    }

    private void loadTripData() {
        Trip trip = storage.getTripById(tripId);
        if (trip != null) {
            destinationEditText.setText(trip.getDestination());
            startDate = trip.getStartDate();
            endDate = trip.getEndDate();
            startDateButton.setText(startDate);
            endDateButton.setText(endDate);

            if (trip.getTripType().equals("Business")) {
                tripTypeRadioGroup.check(R.id.businessRadio);
            } else if (trip.getTripType().equals("Leisure")) {
                tripTypeRadioGroup.check(R.id.leisureRadio);
            } else {
                tripTypeRadioGroup.check(R.id.adventureRadio);
            }

            packedCheckBox.setChecked(trip.isPacked());
            insuranceSwitch.setChecked(trip.hasInsurance());
        }
    }

    private void showDatePicker(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    if (isStartDate) {
                        startDate = date;
                        startDateButton.setText(date);
                    } else {
                        endDate = date;
                        endDateButton.setText(date);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveTrip() {
        String destination = destinationEditText.getText().toString().trim();

        if (destination.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedTypeId = tripTypeRadioGroup.getCheckedRadioButtonId();
        if (selectedTypeId == -1) {
            Toast.makeText(this, "Please select trip type", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadio = findViewById(selectedTypeId);
        String tripType = selectedRadio.getText().toString();
        boolean isPacked = packedCheckBox.isChecked();
        boolean hasInsurance = insuranceSwitch.isChecked();

        Trip trip = new Trip(tripId, destination, startDate, endDate,
                tripType, isPacked, hasInsurance);
        storage.saveTrip(trip);

        Toast.makeText(this, isEditMode ? "Trip updated" : "Trip saved",
                Toast.LENGTH_SHORT).show();
        finish();
    }
}