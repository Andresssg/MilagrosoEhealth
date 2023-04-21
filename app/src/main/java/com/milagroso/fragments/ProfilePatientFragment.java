package com.milagroso.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.milagroso.ehealth.R;
import com.milagroso.models.Doctor;
import com.milagroso.models.Patient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilePatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePatientFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfilePatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilePatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilePatientFragment newInstance(String param1, String param2) {
        ProfilePatientFragment fragment = new ProfilePatientFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_patient, container, false);
        Patient patient = (Patient) getActivity().getIntent().getSerializableExtra("PATIENT");


        TextView fullname = (TextView) view.findViewById(R.id.fullName_text);
        TextView direccion = (TextView) view.findViewById(R.id.address_text);
        TextView documento = (TextView) view.findViewById(R.id.documentp_text);
        TextView edad = (TextView) view.findViewById(R.id.age_text);
        TextView email = (TextView) view.findViewById(R.id.email_text);
        TextView enfermedades = (TextView) view.findViewById(R.id.illness_text);
        TextView genero = (TextView) view.findViewById(R.id.gender_text);
        TextView telefono = (TextView) view.findViewById(R.id.phone_text);

        fullname.setText(patient.getNombres() + " " + patient.getApellidos());
        direccion.setText(patient.getDireccion());
        documento.setText(patient.getDocumentId());
        edad.setText(Integer.toString(patient.getEdad()));
        email.setText(patient.getEmail());
        enfermedades.setText(patient.printIllnesses());
        genero.setText(patient.getGenero());
        telefono.setText(patient.getTelefono());

        return view;
    }
}