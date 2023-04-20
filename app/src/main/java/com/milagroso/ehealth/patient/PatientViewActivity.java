package com.milagroso.ehealth.patient;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import com.milagroso.ehealth.patient.ui.main.SectionsPagerAdapter;
import com.milagroso.ehealth.databinding.ActivityPatientViewBinding;
import com.milagroso.models.Patient;

public class PatientViewActivity extends AppCompatActivity {

    private ActivityPatientViewBinding binding;
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPatientViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        patient = (Patient) getIntent().getSerializableExtra("PATIENT");
        System.out.println(patient.toString());

    }

    public Patient getPatient() {
        return patient;
    }
}