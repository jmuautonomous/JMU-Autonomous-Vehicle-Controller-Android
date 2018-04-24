package com.JMU.AutonomousVehicleApp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;
import static com.JMU.AutonomousVehicleApp.MainActivity.globalPreferenceName;
import static com.android.volley.Request.Method.POST;


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
                                final double dlatitude = locations.getDouble("lat");
                                final double dlongitude = locations.getDouble("long");
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

                                        JSONObject postData = new JSONObject();
                                        try {
                                            postData.put("lat", dlatitude);
                                            postData.put("long", dlongitude);
                                            postData.put("elevation", 0);

                                            new SendDeviceDetails().doInBackground("http://134.126.153.21:5000/goals", postData.toString());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                                Uri.parse("http://maps.google.com/maps?daddr=" + dlatitude + "," + dlongitude));
                                                /*Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));*/
                                        startActivity(intent);

                                        /*MapFragment fragment = new MapFragment();
                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.fram, fragment, "frag2");
                                        fragmentTransaction.commit();*/
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

    private class SendDeviceDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes("PostData=" + params[1]);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }
}
