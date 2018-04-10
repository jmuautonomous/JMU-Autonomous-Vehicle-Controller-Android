package com.JMU.AutonomousVehicleApp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoToFragment extends Fragment {

    Button click;

    public GoToFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*click = getView().findViewById(R.id.button1);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData process = new fetchData();
                process.execute();
            }
        });*/

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_go_to, container, false);
    }
}
