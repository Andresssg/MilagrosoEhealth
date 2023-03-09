package com.milagroso.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Illness implements Serializable {

    private String nombre;
    private HashMap<String, Boolean> sintomas;

    public Illness() {
    }

    public Illness(String name, HashMap<String, Boolean> sintomas) {
        this.nombre = name;
        this.sintomas = sintomas;
    }

    public String getNombre() {
        return nombre;
    }

    public HashMap<String, Boolean> getSintomas() {
        return sintomas;
    }

    @Override
    public String toString() {

        return "- Enfermedad: " + nombre + printSintomas();
    }

    private String printSintomas() {
        String sintomasText = "";
        for (String sintoma : sintomas.keySet()) {
            sintomasText += "\n\t· Sintoma: " + sintoma.replace("_"," ") + " - Presente: " + sintomas.get(sintoma);
        }
        return sintomasText;
    }

    public static ArrayList<Illness> arrayHMToArrayIllness(ArrayList<HashMap> arraylistHM) {

        ArrayList<Illness> illnesses = new ArrayList<>();
        for (HashMap hm : arraylistHM) {
            String nombre = (String) hm.get("nombre");
            HashMap sintomas = (HashMap<String, Boolean>) hm.get("sintomas");
            illnesses.add(new Illness(nombre, sintomas));
        }
        return illnesses;
    }
}
