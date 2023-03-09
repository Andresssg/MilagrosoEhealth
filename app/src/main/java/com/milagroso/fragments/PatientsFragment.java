package com.milagroso.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.milagroso.adapters.ListAdapter;
import com.milagroso.ehealth.DoctorViewActivity;
import com.milagroso.ehealth.PatientInfoActivity;
import com.milagroso.ehealth.R;
import com.milagroso.models.Doctor;
import com.milagroso.models.Illness;
import com.milagroso.models.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientsFragment extends Fragment {

    Doctor doctor;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patients, container, false);
        listView = view.findViewById(R.id.listview);
        DoctorViewActivity doctorViewActivity = (DoctorViewActivity) getActivity();
        doctor = doctorViewActivity.getDoctor();
        ListAdapter listAdapter = new ListAdapter(getContext(), doctor.getPacientes());

        listView.setAdapter(listAdapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), PatientInfoActivity.class);
                intent.putExtra("paciente", doctor.getPacientes().get(i));
                startActivity(intent);
            }
        });

        Button allBtn = (Button) view.findViewById(R.id.all_btn);
        Button diabetesBtn = (Button) view.findViewById(R.id.diabetes_btn);
        Button hipertensionBtn = (Button) view.findViewById(R.id.hipertension_btn);
        Button cardiacaBtn = (Button) view.findViewById(R.id.cardiaca_btn);

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterAllIllnesses();
            }
        });

        diabetesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDiabetes();
            }
        });

        hipertensionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterHipertension();
            }
        });

        cardiacaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterCardiaca();
            }
        });

        return view;

    }

    public void filterAllIllnesses() {
        System.out.println("All");
        ListAdapter listAdapter = new ListAdapter(getContext(), doctor.getPacientes());
        listView.setAdapter(listAdapter);
    }

    public void filterDiabetes() {
        System.out.println("Diabetes");
        ArrayList<Patient> diabeticos = new ArrayList<>();
        for (Patient patient : doctor.getPacientes()) {
            for (Illness illness : patient.getEnfermedades()) {
                if (illness.getNombre().equalsIgnoreCase("diabetes")) {
                    diabeticos.add(patient);
                }
            }
        }
        ListAdapter listAdapter = new ListAdapter(getContext(), diabeticos);
        listView.setAdapter(listAdapter);
    }

    public void filterHipertension() {
        System.out.println("Hipertension");
        ArrayList<Patient> hipertensos = new ArrayList<>();
        for (Patient patient : doctor.getPacientes()) {
            for (Illness illness : patient.getEnfermedades()) {
                if (illness.getNombre().equalsIgnoreCase("hipertension")) {
                    hipertensos.add(patient);
                }
            }
        }
        ListAdapter listAdapter = new ListAdapter(getContext(), hipertensos);
        listView.setAdapter(listAdapter);
    }

    public void filterCardiaca() {
        System.out.println("Cardiaca");
        ArrayList<Patient> cardiacos = new ArrayList<>();
        for (Patient patient : doctor.getPacientes()) {
            for (Illness illness : patient.getEnfermedades()) {
                if (illness.getNombre().equalsIgnoreCase("cardiaca")) {
                    cardiacos.add(patient);
                }
            }
        }
        ListAdapter listAdapter = new ListAdapter(getContext(), cardiacos);
        listView.setAdapter(listAdapter);
    }

}