package com.exam.pm2examengrupo1.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {

    // !!! CAMBIA ESTA URL por la dirección real de tu API Rest !!!
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    // Nota: 10.0.2.2 es la IP para acceder a localhost desde el emulador de Android.

    private static Retrofit retrofit;

    private static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ContactApi getApiService() {
        return getClient().create(ContactApi.class);
    }
}
