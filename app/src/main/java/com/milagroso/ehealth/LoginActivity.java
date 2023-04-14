package com.milagroso.ehealth;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.milagroso.models.Doctor;
import com.milagroso.models.Illness;
import com.milagroso.models.Patient;

import java.util.ArrayList;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {

    private String EXTRA_MESSAGE = "Bienvenido al portal médico";
    private FirebaseFirestore db;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        context = this.getApplicationContext();
    }

    public void onClick(View view) {
        EditText inputDocumentId = (EditText) findViewById(R.id.document);
        String documentId = inputDocumentId.getText().toString();
        EditText inputPassword = (EditText) findViewById(R.id.password);
        String password = inputPassword.getText().toString();
        Spinner rol = (Spinner) findViewById(R.id.rol);
        String option = rol.getSelectedItem().toString();

        //Intent intent = new Intent(this, PatientInfoActivity.class);
        //startActivity(intent);
        switch (option) {
            case "Paciente":
                getPatientFromDB(documentId, password);
                break;
            case "Doctor":
                getDoctorFromDB(documentId, password);
                break;
            default:
                Toast.makeText(context, "Seleccione un rol valido", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void getPatientFromDB(String documentId, String password) {
        db.collection("Pacientes")
                .whereEqualTo("documento", documentId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        if (queryDocumentSnapshots.size() != 0) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            String dbPassword = (String) document.get("password");
                            if (password.equals(dbPassword)) {
                                Patient patient = new Patient(
                                        (String) document.get("apellidos"),
                                        (String) document.get("direccion"),
                                        (String) document.get("documentoId"),
                                        (Integer) document.get("edad"),
                                        (String) document.get("email"),
                                        (ArrayList<Illness>) document.get("enfermedades"),
                                        (String) document.get("genero"),
                                        (String) document.get("nombres"),
                                        (String) document.get("telefono")
                                );
                                Toast.makeText(context, "!Bienvenido " + patient.getNombres() + "¡", Toast.LENGTH_SHORT).show();
                                //TODO:Make intent for patientactivity
                            } else {
                                Toast.makeText(context, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "No hay usuarios", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });

    }

    public void getDoctorFromDB(String documentId, String password) {
        db.collection("Medicos")
                .whereEqualTo("documento", documentId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        if (queryDocumentSnapshots.size() != 0) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            String dbPassword = (String) document.get("password");
                            if (password.equals(dbPassword)) {
                                Doctor doctor = new Doctor(document.get("nombres").toString(),
                                        document.get("apellidos").toString(),
                                        document.get("documento").toString(),
                                        (ArrayList<Patient>) Patient.arrayHMToArrayPatient((ArrayList<HashMap>) document.get("pacientes")),
                                        Integer.parseInt(document.get("edad").toString()),
                                        document.get("especialidad").toString(),
                                        document.get("fecha_nacimiento").toString(),
                                        document.get("password").toString(),
                                        document.get("email").toString());
                                Toast.makeText(context, "!Bienvenido " + doctor.getNombres() + "¡", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(this, DoctorViewActivity.class);
                                intent.putExtra("DOCTOR", doctor);
                                startActivity(intent);
                            } else {
                                Toast.makeText(context, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "No hay usuarios", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }
}