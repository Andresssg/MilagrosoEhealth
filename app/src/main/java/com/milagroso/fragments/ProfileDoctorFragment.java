package com.milagroso.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.milagroso.ehealth.R;
import com.milagroso.models.Doctor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileDoctorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileDoctorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileDoctorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileDoctorFragment newInstance(String param1, String param2) {
        ProfileDoctorFragment fragment = new ProfileDoctorFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Doctor doctor = (Doctor) getActivity().getIntent().getSerializableExtra("DOCTOR");

        TextView name = (TextView) view.findViewById(R.id.fullName_text);
        TextView speciality = (TextView) view.findViewById(R.id.speciality_text);
        TextView age = (TextView) view.findViewById(R.id.age_text);
        TextView birthDate = (TextView) view.findViewById(R.id.birth_text);
        TextView email = (TextView) view.findViewById(R.id.email_text);
        TextView password = (TextView) view.findViewById(R.id.password_text);

        name.setText(doctor.getNombres() + " " + doctor.getApellidos());
        speciality.setText(doctor.getSpeciality());
        age.setText(Integer.toString(doctor.getAge()));
        birthDate.setText(doctor.getBirthDate());
        email.setText(doctor.getEmail());
        password.setText(doctor.getPassword());

        ImageView visibilityOn = view.findViewById(R.id.visibility_on);
        ImageView visibilityOff = view.findViewById(R.id.visibility_off);
        visibilityOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setInputType(View.AUTOFILL_TYPE_TEXT);
                visibilityOn.setVisibility(View.INVISIBLE);
                visibilityOff.setVisibility(View.VISIBLE);
            }
        });

        visibilityOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //129 TO SET INPUT TYPE TO PASSWORD
                password.setInputType(129);
                visibilityOff.setVisibility(View.INVISIBLE);
                visibilityOn.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
}