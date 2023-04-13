package com.milagroso.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.milagroso.ehealth.R;
import com.milagroso.models.Patient;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<Patient> {
    public ListAdapter(@NonNull Context context, ArrayList<Patient> patients) {
        super(context, R.layout.list_item, patients);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Patient patient = getItem(position);

        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(patient.getNombres().toString());
        return view;
    }
}
