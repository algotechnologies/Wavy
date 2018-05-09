package com.wavy.app.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wavy.app.restdata.Pair;
import com.wavy.app.util.Constants;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class DataFetcher {

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build();

    public void run(String requestMethod, String url, String jsonString, Callback callback) throws DataFetcherException {

        if (!requestMethod.equals("GET") && !requestMethod.equals("DELETE")) {
            throw new DataFetcherException(Constants.REQUEST_METHOD_ERROR_MSG);
        }

        Gson gson = new Gson();
        Request request;

        if (requestMethod.equals("DELETE")) {
            Type type = new TypeToken<List<Pair>>() {}.getType();
            List<Pair<String,String>> params = gson.fromJson(jsonString, type);

            request = new Request.Builder()
                    .url(url + params.get(0).getValue())
                    .delete()
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .build();
        }

        Call call = client.newCall(request);
        call.enqueue(callback);

    }

}