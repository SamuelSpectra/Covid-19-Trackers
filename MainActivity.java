package com.mobile.covid_apps;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mobile.covid_apps.Model.GlobalResponse;
import com.mobile.covid_apps.Network.ApiClientPrivate;
import com.mobile.covid_apps.Network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Animation rotateAnimation;
    ImageView earth;
    ImageButton exit_button;
    TextView cases, death, recovered;
    Button countryTracker;
    private PieChart pieChart;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        earth = (ImageView) findViewById(R.id.earth);
        exit_button = (ImageButton) findViewById(R.id.exit_btn);

        cases = (TextView) findViewById(R.id.global_cases);
        death = (TextView) findViewById(R.id.global_death);
        recovered = (TextView) findViewById(R.id.global_recover);

        pieChart = (PieChart) findViewById(R.id.pieChart);

        countryTracker = (Button) findViewById(R.id.countryTracker);

        countryTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AllCountriesActivity.class));
            }
        });

        rotateAnimation();

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.earth)
                        .setTitle("EXIT")
                        .setMessage("Apakah Anda Ingin Keluar Dari Aplikasi?")
                        .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });

        apiCall();
        setupPieChart();
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Global Cases");
        pieChart.setCenterTextSize(20);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void apiCall() {

        ApiInterface apiInterface = null;
        apiInterface = ApiClientPrivate.getApiClient().create(ApiInterface.class);

        Call<GlobalResponse> call = apiInterface.GlobalResponse();
        call.enqueue(new Callback<GlobalResponse>() {
            @Override
            public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response) {
                if (response.isSuccessful()) {

                    String stCase = String.valueOf(response.body().getCases());
                    String stDeaths = String.valueOf(response.body().getDeaths());
                    String stRecovered = String.valueOf(response.body().getRecovered());

                    cases.setText(stCase);
                    death.setText(stDeaths);
                    recovered.setText(stRecovered);

                    int caseInt = Integer.parseInt(stCase);
                    int deathInt = Integer.parseInt(stDeaths);
                    int recoveredInt = Integer.parseInt(stRecovered);

                    ArrayList<PieEntry> entries = new ArrayList<>();
                    entries.add(new PieEntry(caseInt,"Global Cases"));
                    entries.add(new PieEntry(deathInt,"Global Death"));
                    entries.add(new PieEntry(recoveredInt,"Global Recovered"));

                    ArrayList<Integer> colors = new ArrayList<>();
                    for (int color: ColorTemplate.MATERIAL_COLORS) {
                        colors.add(color);
                    }

                    for (int color: ColorTemplate.VORDIPLOM_COLORS) {
                        colors.add(color);
                    }

                    PieDataSet dataSet = new PieDataSet(entries, "");
                    dataSet.setColors(colors);

                    PieData data = new PieData(dataSet);
                    data.setDrawValues(true);
                    data.setValueFormatter(new PercentFormatter(pieChart));
                    data.setValueTextSize(10f);
                    data.setValueTextColor(Color.BLACK);

                    pieChart.setData(data);
                    pieChart.invalidate();
                }
            }

            @Override
            public void onFailure(Call<GlobalResponse> call, Throwable t) {

            }
        });
    }

    private void rotateAnimation() {
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        earth.startAnimation(rotateAnimation);
    }
}