package com.milagroso.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Doctor implements Serializable {

    private String nombres;
    private String apellidos;
    private String documentId;
    private ArrayList<Patient> pacientes;

    public Doctor() {
    }

    public Doctor(String documentId, String nombres, String apellidos, ArrayList<Patient> pacientes) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.documentId = documentId;
        this.pacientes = pacientes;
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

    @Override
    public String toString() {
        return "Doctor{" +
                "nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", documentId='" + documentId + '\'' +
                ", pacientes=" + pacientes +
                '}';
    }
}
