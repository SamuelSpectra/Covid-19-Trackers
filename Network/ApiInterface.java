package com.mobile.covid_apps.Network;

import com.mobile.covid_apps.Model.GlobalResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("all")
    Call<GlobalResponse> GlobalResponse();
}
