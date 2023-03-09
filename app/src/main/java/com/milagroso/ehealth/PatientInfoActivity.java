package com.milagroso.ehealth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.milagroso.models.Patient;

public class PatientInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        Patient patient = (Patient) getIntent().getSerializableExtra("paciente");
        System.out.println(patient.toString());

        TextView fullname = (TextView) findViewById(R.id.fullName_text);
        TextView direccion1 = (TextView) findViewById(R.id.direccion_text);
        TextView direccion2 = (TextView) findViewById(R.id.address_text);
        TextView documento = (TextView) findViewById(R.id.document_text);
        TextView edad = (TextView) findViewById(R.id.age_text);
        TextView email = (TextView) findViewById(R.id.email_text);
        TextView enfermedades = (TextView) findViewById(R.id.illness_text);
        TextView genero = (TextView) findViewById(R.id.gender_text);
        TextView nombre = (TextView) findViewById(R.id.name_text);
        TextView telefono = (TextView) findViewById(R.id.phone_text);

        fullname.setText(patient.getNombres() + " " + patient.getApellidos());
        direccion1.setText(patient.getDireccion());
        direccion2.setText(patient.getDireccion());
        documento.setText(patient.getDocumentId());
        edad.setText(Integer.toString(patient.getEdad()));
        email.setText(patient.getEmail());
        enfermedades.setText(patient.printIllnesses());
        genero.setText(patient.getGenero());
        nombre.setText(patient.getNombres());
        telefono.setText(patient.getTelefono());


    }
}