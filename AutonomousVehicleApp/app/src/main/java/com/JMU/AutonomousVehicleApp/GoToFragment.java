package com.JMU.AutonomousVehicleApp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;
import static com.JMU.AutonomousVehicleApp.MainActivity.globalPreferenceName;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoToFragment extends Fragment {

    public GoToFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(globalPreferenceName, MODE_PRIVATE);
        String URL = sharedPreferences.getString("URL","http://10.0.0.218:8080/") + "locations";

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("Locations");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject locations = jsonArray.getJSONObject(i);
                                String address = locations.getString("address");
                                final int id = locations.getInt("id");
                                double latitude = locations.getDouble("lat");
                                double longitude = locations.getDouble("long");
                                String name = locations.getString("name");

                                LinearLayout layout = getView().findViewById(R.id.goToButtons);
                                Button goToButton = new android.support.v7.widget.AppCompatButton(GoToFragment.this.getContext());
                                goToButton.setText(name);
                                goToButton.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                ));
                                goToButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        System.out.println("button " + id + "pressed");
                                        MapFragment fragment = new MapFragment();
                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.fram, fragment, "frag2");
                                        fragmentTransaction.commit();
                                    }
                                });
                                goToButton.setTag(id);
                                layout.addView(goToButton);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(objectRequest);


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_go_to, container, false);
    }
}
