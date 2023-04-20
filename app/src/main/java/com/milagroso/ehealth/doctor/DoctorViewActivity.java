package com.milagroso.ehealth.doctor;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.milagroso.ehealth.doctor.ui.main.SectionsPagerAdapter;
import com.milagroso.ehealth.databinding.ActivityDoctorViewBinding;
import com.milagroso.models.Doctor;

public class DoctorViewActivity extends AppCompatActivity {

    private ActivityDoctorViewBinding binding;
    private Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDoctorViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        doctor = (Doctor) getIntent().getSerializableExtra("DOCTOR");
    }

    public Doctor getDoctor() {
        return doctor;
    }
}