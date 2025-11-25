package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TripDetailsActivity extends AppCompatActivity {

    private TextView destinationTextView;
    private TextView startDateTextView;
    private TextView endDateTextView;
    private TextView tripTypeTextView;
    private TextView packedTextView;
    private TextView insuranceTextView;
    private Button editButton;
    private String tripId;
    private TripStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        storage = new TripStorage(this);
        tripId = getIntent().getStringExtra("TRIP_ID");

        destinationTextView = findViewById(R.id.destinationTextView);
        startDateTextView = findViewById(R.id.startDateTextView);
        endDateTextView = findViewById(R.id.endDateTextView);
        tripTypeTextView = findViewById(R.id.tripTypeTextView);
        packedTextView = findViewById(R.id.packedTextView);
        insuranceTextView = findViewById(R.id.insuranceTextView);
        editButton = findViewById(R.id.editButton);

        loadTripDetails();

        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(TripDetailsActivity.this, AddTripActivity.class);
            intent.putExtra("TRIP_ID", tripId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTripDetails();
    }

    private void loadTripDetails() {
        Trip trip = storage.getTripById(tripId);
        if (trip != null) {
            destinationTextView.setText(trip.getDestination());
            startDateTextView.setText(trip.getStartDate());
            endDateTextView.setText(trip.getEndDate());
            tripTypeTextView.setText(trip.getTripType());
            packedTextView.setText("Packing: " + (trip.isPacked() ? "Complete" : "Incomplete"));
            insuranceTextView.setText("Insurance: " + (trip.hasInsurance() ? "Yes" : "No"));
        }
    }
}