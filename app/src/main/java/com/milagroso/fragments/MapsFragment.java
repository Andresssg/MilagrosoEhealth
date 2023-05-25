package com.milagroso.fragments;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.PolyUtil;
import com.milagroso.ehealth.BuildConfig;
import com.milagroso.ehealth.R;
import com.milagroso.ehealth.patient.PatientViewActivity;
import com.milagroso.models.Doctor;
import com.milagroso.models.Patient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsFragment extends Fragment {

    private Doctor doctor;
    private FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private double latitude = 0;
    private double longitude = 0;
    private Polyline currentRoute = null;
    String apiKey = BuildConfig.MAPS_API_KEY;

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
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(colombia, 10));
            UiSettings uiSettings = googleMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);
            db = FirebaseFirestore.getInstance();
            Intent intent = getActivity().getIntent();
            Doctor doctor = (Doctor) intent.getSerializableExtra("DOCTOR");
            Patient patient = (Patient) intent.getSerializableExtra("PATIENT");
            ArrayList centros = (ArrayList) intent.getSerializableExtra("CENTROS");
            System.out.println("********************************************************");
            System.out.println(centros);
            if (doctor != null) {
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
            } else {
                latitude = PatientViewActivity.latitude;
                longitude = PatientViewActivity.longitude;
                LatLng infoLocation = new LatLng(latitude, longitude);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(infoLocation, 16));
                getCentrosFromDB(googleMap);
                googleMap.addMarker(new MarkerOptions().position(infoLocation).title("Yo"));
            }
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

    private String obtenerURLRuta(LatLng origen, LatLng destino) {
        String str_origen = "origin=" + origen.latitude + "," + origen.longitude;
        String str_destino = "destination=" + destino.latitude + "," + destino.longitude;

        String parameters = str_origen + "&" + str_destino + "&key=" + apiKey;
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters;
        return url;
    }

    private void trazarRuta(String url, GoogleMap mMap) {
        if (currentRoute != null)
            currentRoute.remove();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonRespuesta = new JSONObject(response);
                            JSONArray pasosRuta = jsonRespuesta.getJSONArray("routes")
                                    .getJSONObject(0)
                                    .getJSONArray("legs")
                                    .getJSONObject(0)
                                    .getJSONArray("steps");

                            List<LatLng> puntosRuta = new ArrayList<>();

                            for (int i = 0; i < pasosRuta.length(); i++) {
                                String puntosCodificados = pasosRuta.getJSONObject(i).getJSONObject("polyline").getString("points");
                                List<LatLng> puntosDecodificados = PolyUtil.decode(puntosCodificados);
                                puntosRuta.addAll(puntosDecodificados);
                            }

                            PolylineOptions opcionesRuta = new PolylineOptions()
                                    .addAll(puntosRuta)
                                    .color(Color.BLUE)
                                    .width(10);

                            Polyline polyline = mMap.addPolyline(opcionesRuta);
                            currentRoute = polyline;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error en caso de que ocurra
                    }
                });

        queue.add(stringRequest);
    }

    public void getCentrosFromDB(GoogleMap mMap) {
        ArrayList centros = new ArrayList<>();
        db.collection("Centros")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                centros.add(document.getData());
                                GeoPoint location = (GeoPoint) document.get("location");
                                String name = (String) document.get("name");
                                LatLng infoLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(infoLocation).title(name));
                                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(@NonNull Marker marker) {
                                        LatLng position = marker.getPosition();
                                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                                .target(position)
                                                .zoom(16)
                                                .build();
                                        String title = marker.getTitle();
                                        Toast.makeText(getActivity().getApplicationContext(), title, Toast.LENGTH_SHORT).show();
                                        LatLng current = new LatLng(latitude, longitude);
                                        String url = obtenerURLRuta(current, position);
                                        trazarRuta(url, mMap);
                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        return true;
                                    }
                                });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}

