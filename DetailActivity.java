package com.mobile.covid_apps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.mobile.covid_apps.Adapter.CountryAdapter;
import com.mobile.covid_apps.Model.AllCountriesModel.CountryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    TextView cases, death, recovered, globalState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        cases = (TextView) findViewById(R.id.cases);
        death = (TextView) findViewById(R.id.death);
        recovered = (TextView) findViewById(R.id.recovered);
        globalState = (TextView) findViewById(R.id.globalStates);

        Intent intent = getIntent();
        String countryName = intent.getStringExtra("countryName");

        globalState.setText(countryName);

        ApiCall(countryName);
    }

    private void ApiCall(String countryName) {
        String URL = "https://corona.lmao.ninja/v2/countries/" + countryName;

        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    cases.setText(jsonObject.getString("cases"));
                    death.setText(jsonObject.getString("deaths"));
                    recovered.setText(jsonObject.getString("recovered"));
                    //Log.d("cases", jsonObject.getString("cases"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}