package com.milagroso.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.milagroso.ehealth.R;
import com.milagroso.models.Doctor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsFragment extends Fragment {

    private Doctor doctor;
    private FirebaseFirestore db;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng colombia = new LatLng(4.5709, -74.2973);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(colombia, 4));
            UiSettings uiSettings = googleMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);
            db = FirebaseFirestore.getInstance();
            Doctor doctor = (Doctor) getActivity().getIntent().getSerializableExtra("DOCTOR");
            ArrayList patientsIds = doctor.getPatientsIds();
            db.collection("Reportes")
                    .whereIn("idpaciente", patientsIds)
                    .whereEqualTo("emergencia", true)
                    .orderBy("fecha", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Map<String, DocumentSnapshot> locations = new HashMap<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String idPaciente = document.getString("idpaciente");
                                if (!locations.containsKey(idPaciente)) {
                                    locations.put(idPaciente, document);
                                }
                            }
                            for (Map.Entry<String, DocumentSnapshot> entry : locations.entrySet()) {
                                DocumentSnapshot lastReport = entry.getValue();
                                GeoPoint location = (GeoPoint) lastReport.get("ubicacion");
                                String patientName = doctor.searchPatient(entry.getKey());
                                LatLng infoLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                googleMap.addMarker(new MarkerOptions().position(infoLocation).title(patientName));
                                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(@NonNull Marker marker) {
                                        LatLng position = marker.getPosition();
                                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                                .target(position)
                                                .zoom(12f)
                                                .build();
                                        String title = marker.getTitle();
                                        Toast.makeText(getActivity().getApplicationContext(), title, Toast.LENGTH_SHORT).show();

                                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        return true;
                                    }
                                });
                            }

                        } else {
                            System.out.println("Error: " + task.getException());
                        }
                    });
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void getLocations() {
    }
}