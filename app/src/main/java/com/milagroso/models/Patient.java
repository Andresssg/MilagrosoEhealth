package com.milagroso.models;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Patient implements Serializable {
    private String apellidos;
    private String direccion;
    private String documentId;
    private int edad;
    private String email;
    private ArrayList<Illness> enfermedades;
    private String genero;
    private String nombres;
    private String telefono;

    public Patient() {
        this.apellidos = "";
        this.direccion = "";
        this.documentId = "";
        this.edad = 0;
        this.email = "";
        this.enfermedades = new ArrayList<>();
        this.genero = "";
        this.nombres = "";
        this.telefono = "";
    }

    public Patient(String apellidos, String direccion
            , String documentId, int edad, String email
            , ArrayList<Illness> enfermedades, String genero
            , String nombres, String telefono) {
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.documentId = documentId;
        this.edad = edad;
        this.email = email;
        this.enfermedades = enfermedades;
        this.genero = genero;
        this.nombres = nombres;
        this.telefono = telefono;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getDocumentId() {
        return documentId;
    }

    public int getEdad() {
        return edad;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Illness> getEnfermedades() {
        return enfermedades;
    }

    public String getGenero() {
        return genero;
    }

    public String getNombres() {
        return nombres;
    }

    public String getTelefono() {
        return telefono;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "apellidos='" + apellidos + '\'' +
                ", direccion='" + direccion + '\'' +
                ", documentId='" + documentId + '\'' +
                ", edad=" + edad +
                ", email='" + email + '\'' +
                ", enfermedades=" + printIllnesses() +
                ", genero='" + genero + '\'' +
                ", nombres='" + nombres + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }

    public String printIllnesses() {
        String illnesses = "";
        for (Illness illness : enfermedades) {
            illnesses += illness.toString() + "\n";
        }
        return illnesses;
    }

    public static Patient hashMapToPatient(HashMap patientHM) {
        String apellidos = (String) patientHM.get("apellidos");
        String direccion = (String) patientHM.get("direccion");
        String documento = (String) patientHM.get("documento");
        String edadStr = patientHM.get("edad").toString();
        int edad = Integer.parseInt(edadStr);
        String email = (String) patientHM.get("email");
        ArrayList<Illness> enfermedades = Illness.arrayHMToArrayIllness((ArrayList<HashMap>) patientHM.get("enfermedades"));
        String genero = (String) patientHM.get("genero");
        String nombres = (String) patientHM.get("nombres");
        String telefono = (String) patientHM.get("telefono");
        return new Patient(apellidos, direccion, documento, edad, email, enfermedades, genero, nombres, telefono);
    }

    public static ArrayList<Patient> arrayHMToArrayPatient(ArrayList<HashMap> arraylistHM) {

        ArrayList<Patient> patients = new ArrayList<>();
        for (HashMap hm : arraylistHM) {
            patients.add(hashMapToPatient(hm));
        }
        return patients;
    }
}
