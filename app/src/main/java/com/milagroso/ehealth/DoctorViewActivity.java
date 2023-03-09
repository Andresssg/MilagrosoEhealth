package com.milagroso.ehealth;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.milagroso.adapters.ListAdapter;
import com.milagroso.ehealth.ui.main.SectionsPagerAdapter;
import com.milagroso.ehealth.databinding.ActivityDoctorViewBinding;
import com.milagroso.models.Doctor;
import com.milagroso.models.Patient;

import java.util.ArrayList;

public class DoctorViewActivity extends AppCompatActivity {

    private ActivityDoctorViewBinding binding;
    private ListAdapter listAdapter;
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