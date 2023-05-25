package com.milagroso.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.milagroso.ehealth.R;
import com.milagroso.ehealth.patient.PatientViewActivity;
import com.milagroso.models.Patient;

import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VitalSignsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VitalSignsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseFirestore db;
    private Context context;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private double latitude = 0;

    private double longitude = 0;

    public VitalSignsFragment() {}

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VitalSignsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VitalSignsFragment newInstance(String param1, String param2) {
        VitalSignsFragment fragment = new VitalSignsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = FirebaseFirestore.getInstance();
        context = this.getActivity().getApplicationContext();
    }

    public void ubication() {
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                Location location = locationResult.getLastLocation();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                PatientViewActivity.latitude = latitude;
                PatientViewActivity.longitude = longitude;
            }
        };
        // Verifica si se tienen permisos de ubicación
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // Si se tienen permisos, solicita la ubicación
            fusedLocationProviderClient.requestLocationUpdates(
                    createLocationRequest(),
                    locationCallback,
                    Looper.getMainLooper());
        } else {
            // Si no se tienen permisos, solicítalos
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        ubication();

        View view = inflater.inflate(R.layout.fragment_vital_signs, container, false);
        EditText inputPulse = (EditText) view.findViewById(R.id.pulse);
        EditText inputBreathingFrecuency = (EditText) view.findViewById(R.id.breathingFrecuency);
        EditText inputBodyTemperature = (EditText) view.findViewById(R.id.bodyTemperature);
        EditText inputBloodPressure = (EditText) view.findViewById(R.id.bloodPressure);

        Button button = view.findViewById(R.id.vitalSign_button);
        Button emergencyButton = view.findViewById(R.id.emergency_button);
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(getContext());
                alerta.setMessage("Está seguro de reportar una emergencia?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Patient patient = (Patient) getActivity().getIntent().getSerializableExtra("PATIENT");
                                String pulseText = inputPulse.getText().toString();
                                String BreathingFrecuencyText = inputBreathingFrecuency.getText().toString();
                                String bodyTemperatureText = inputBodyTemperature.getText().toString();
                                String BloodPressureText = inputBloodPressure.getText().toString();

                                HashMap<String, Object> report = new HashMap<>();
                                HashMap<String, String> vitalSigns = new HashMap<>();

                                vitalSigns.put("pulso", pulseText);
                                vitalSigns.put("frecuencia respiratoria", BreathingFrecuencyText);
                                vitalSigns.put("temperatura corporal", bodyTemperatureText);
                                vitalSigns.put("presion arterial", BloodPressureText);

                                report.put("emergencia", true);
                                report.put("fecha", new Timestamp(new Date()));
                                report.put("idpaciente", patient.getDocumentId());
                                report.put("signos vitales", vitalSigns);
                                report.put("ubicacion", new GeoPoint(latitude, longitude));

                                addReport(report);

                                inputBloodPressure.setText("");
                                inputBodyTemperature.setText("");
                                inputPulse.setText("");
                                inputBreathingFrecuency.setText("");
                                dialogInterface.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Salida");
                titulo.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Patient patient = (Patient) getActivity().getIntent().getSerializableExtra("PATIENT");
                String pulseText = inputPulse.getText().toString();
                String BreathingFrecuencyText = inputBreathingFrecuency.getText().toString();
                String bodyTemperatureText = inputBodyTemperature.getText().toString();
                String BloodPressureText = inputBloodPressure.getText().toString();

                HashMap<String, Object> report = new HashMap<>();
                HashMap<String, String> vitalSigns = new HashMap<>();

                vitalSigns.put("pulso", pulseText);
                vitalSigns.put("frecuencia respiratoria", BreathingFrecuencyText);
                vitalSigns.put("temperatura corporal", bodyTemperatureText);
                vitalSigns.put("presion arterial", BloodPressureText);

                report.put("emergencia", false);
                report.put("fecha", new Timestamp(new Date()));
                report.put("idpaciente", patient.getDocumentId());
                report.put("signos vitales", vitalSigns);

                addReport(report);

                inputBloodPressure.setText("");
                inputBodyTemperature.setText("");
                inputPulse.setText("");
                inputBreathingFrecuency.setText("");
            }
        });
        return view;
    }

    public void addReport(HashMap<String, Object> report) {
        db.collection("Reportes").add(report).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context, "Se guardó su reporte correctamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "No se guardó su reporte. Intentelo nuevamente", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}