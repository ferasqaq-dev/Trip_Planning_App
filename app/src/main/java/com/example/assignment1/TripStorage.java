package com.example.assignment1;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TripStorage {
    private static final String PREFS_NAME = "TripPrefs";
    private static final String TRIPS_KEY = "trips_list";
    private SharedPreferences prefs;

    public TripStorage(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveTrip(Trip trip) {
        Set<String> trips = prefs.getStringSet(TRIPS_KEY, new HashSet<>());
        Set<String> newTrips = new HashSet<>(trips);

        String tripData = trip.getId() + "|" + trip.getDestination() + "|" +
                trip.getStartDate() + "|" + trip.getEndDate() + "|" +
                trip.getTripType() + "|" + trip.isPacked() + "|" +
                trip.hasInsurance();

        newTrips.removeIf(t -> t.startsWith(trip.getId() + "|"));
        newTrips.add(tripData);

        prefs.edit().putStringSet(TRIPS_KEY, newTrips).apply();
    }

    public List<Trip> getAllTrips() {
        Set<String> trips = prefs.getStringSet(TRIPS_KEY, new HashSet<>());
        List<Trip> tripList = new ArrayList<>();

        for (String tripData : trips) {
            String[] parts = tripData.split("\\|");
            if (parts.length == 7) {
                Trip trip = new Trip(
                        parts[0], parts[1], parts[2], parts[3], parts[4],
                        Boolean.parseBoolean(parts[5]),
                        Boolean.parseBoolean(parts[6])
                );
                tripList.add(trip);
            }
        }
        return tripList;
    }

    public void deleteTrip(String tripId) {
        Set<String> trips = prefs.getStringSet(TRIPS_KEY, new HashSet<>());
        Set<String> newTrips = new HashSet<>(trips);
        newTrips.removeIf(t -> t.startsWith(tripId + "|"));
        prefs.edit().putStringSet(TRIPS_KEY, newTrips).apply();
    }

    public Trip getTripById(String id) {
        List<Trip> trips = getAllTrips();
        for (Trip trip : trips) {
            if (trip.getId().equals(id)) {
                return trip;
            }
        }
        return null;
    }
}