package com.milagroso.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Doctor implements Serializable {

    private String nombres;
    private String apellidos;
    private String documentId;
    private ArrayList<Patient> pacientes;
    private int age;
    private String speciality;
    private String birthDate;
    private String password;
    private String email;

    public Doctor() {
    }

    public Doctor(String nombres, String apellidos, String documentId, ArrayList<Patient> pacientes, int age, String speciality, String birthDate, String password, String email) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.documentId = documentId;
        this.pacientes = pacientes;
        this.age = age;
        this.speciality = speciality;
        this.birthDate = birthDate;
        this.password = password;
        this.email = email;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDocumentId() {
        return documentId;
    }

    public ArrayList<Patient> getPacientes() {
        return pacientes;
    }

    public int getAge() {
        return age;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", documentId='" + documentId + '\'' +
                ", pacientes=" + pacientes +
                ", age=" + age +
                ", speciality='" + speciality + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
