package com.JMU.AutonomousVehicleApp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
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
        timer.schedule(new UpdateSensorData(), 0, 15000);

        return inflater.inflate(R.layout.fragment_sensors, container, false);
    }

    class UpdateSensorData extends TimerTask {
        @Override
        public void run() {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(globalPreferenceName, MODE_PRIVATE);

            //getting global URL from shared preferences. If this fails the default value will be http://10.0.0.218:8080/
            String URL = sharedPreferences.getString("URL","http://10.0.0.218:8080/") + "cardata";

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());

            //creating json object request to GET data from api
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

                                //looping through items to display car sensor data
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

                                    DecimalFormat decpl = new DecimalFormat("#.00000");

                                    TextView sensor1text;
                                    sensor1text = getView().findViewById(R.id.sensorText1);
                                    sensor1text.setText(Integer.toString(battery) + "V");

                                    TextView sensor2text;
                                    sensor2text = getView().findViewById(R.id.sensorText2);
                                    sensor2text.setText(decpl.format(latitude) + ", " + decpl.format(longitude));

                                    TextView sensor3text;
                                    sensor3text = getView().findViewById(R.id.sensorText3);
                                    sensor3text.setText(velodyne);

                                    TextView sensor4text;
                                    sensor4text = getView().findViewById(R.id.sensorText4);
                                    sensor4text.setText(lightware);

                                    TextView sensor5text;
                                    sensor5text = getView().findViewById(R.id.sensorText5);
                                    sensor5text.setText(rplidar);

                                    TextView sensor6text;
                                    sensor6text = getView().findViewById(R.id.sensorText6);
                                    sensor6text.setText(camera);
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
