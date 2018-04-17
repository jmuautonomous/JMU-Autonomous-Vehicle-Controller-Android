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

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;
import static com.JMU.AutonomousVehicleApp.MainActivity.globalPreferenceName;


/**
 * A simple {@link Fragment} subclass.
 */
public class SensorsFragment extends Fragment {


    public SensorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timer timer = new Timer();
        timer.schedule(new UpdateSensorData(), 0, 5000);

        return inflater.inflate(R.layout.fragment_sensors, container, false);
    }

    class UpdateSensorData extends TimerTask {
        @Override
        public void run() {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(globalPreferenceName, MODE_PRIVATE);
            String URL = sharedPreferences.getString("URL","http://10.0.0.218:8080/") + "cardata";

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());

            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    URL,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response", response.toString());
                            try {
                                JSONArray jsonArray = response.getJSONArray("Cardata");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject locations = jsonArray.getJSONObject(i);
                                    int battery = locations.getInt("battery");
                                    int elevation = locations.getInt("elevation");
                                    Double latitude = locations.getDouble("lat");
                                    Double longitude = locations.getDouble("lon");
                                    String velodyne = locations.getString("velodyne");
                                    String lightware = locations.getString("lightware");
                                    String rplidar = locations.getString("rplidar");
                                    String camera = locations.getString("camera");
                                    int velocity = locations.getInt("velocity");
                                    String timestamp = locations.getString("timestamp");
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
        }
    }
}
