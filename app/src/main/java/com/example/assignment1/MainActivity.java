package com.example.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TripAdapter.OnTripClickListener {

    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private TripStorage storage;
    private List<Trip> allTrips;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storage = new TripStorage(this);
        searchEditText = findViewById(R.id.searchEditText);
        recyclerView = findViewById(R.id.tripsRecyclerView);
        Button addTripButton = findViewById(R.id.addTripButton);
        Button searchButton = findViewById(R.id.searchButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        allTrips = new ArrayList<>();
        adapter = new TripAdapter(allTrips, this);
        recyclerView.setAdapter(adapter);

        addTripButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
            startActivity(intent);
        });

        searchButton.setOnClickListener(v -> performSearch());

        loadTrips();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTrips();
    }

    private void loadTrips() {
        allTrips = storage.getAllTrips();
        adapter.updateTrips(allTrips);
    }

    private void performSearch() {
        String query = searchEditText.getText().toString().toLowerCase().trim();
        if (query.isEmpty()) {
            loadTrips();
            return;
        }

        List<Trip> filteredTrips = new ArrayList<>();
        for (Trip trip : allTrips) {
            if (trip.getDestination().toLowerCase().contains(query)) {
                filteredTrips.add(trip);
            }
        }
        adapter.updateTrips(filteredTrips);

        if (filteredTrips.isEmpty()) {
            Toast.makeText(this, "No trips found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTripClick(Trip trip) {
        Intent intent = new Intent(MainActivity.this, TripDetailsActivity.class);
        intent.putExtra("TRIP_ID", trip.getId());
        startActivity(intent);
    }

    @Override
    public void onTripLongClick(Trip trip) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Trip")
                .setMessage("Delete " + trip.getDestination() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    storage.deleteTrip(trip.getId());
                    loadTrips();
                    Toast.makeText(this, "Trip deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}