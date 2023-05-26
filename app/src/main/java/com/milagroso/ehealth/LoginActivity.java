package com.milagroso.ehealth;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QuerySnapshot;
import com.milagroso.ehealth.doctor.DoctorViewActivity;
import com.milagroso.ehealth.patient.PatientViewActivity;
import com.milagroso.models.Doctor;
import com.milagroso.models.Illness;
import com.milagroso.models.Patient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
        createNotificationChannel();
        solicitarPermisos();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("0", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
                                        (String) document.get("documento"),
                                        Integer.parseInt(document.get("edad").toString()),
                                        (String) document.get("email"),
                                        Illness.arrayHMToArrayIllness((ArrayList<HashMap>) document.get("enfermedades")),
                                        (String) document.get("genero"),
                                        (String) document.get("nombres"),
                                        (String) document.get("telefono")
                                );
                                showNotification(patient.getNombres());

                                Intent intent = new Intent(this, PatientViewActivity.class);
                                intent.putExtra("PATIENT", patient);
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
                                        dbPassword,
                                        document.get("email").toString()
                                );
                                showNotification(doctor.getNombres());

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

    @SuppressLint("MissingPermission")
    public void showNotification(String nombre){
        Intent intentNotification = new Intent(this, LoginActivity.class);
        intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentNotification, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setContentTitle("Inicio de sesión")
                .setContentText("¡Bienvenido " + nombre + "!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.getNotification());
    }

    public void solicitarPermisos(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
        } else {
            // Si no se tienen permisos, solicítalos
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    1);
        }
    }
}