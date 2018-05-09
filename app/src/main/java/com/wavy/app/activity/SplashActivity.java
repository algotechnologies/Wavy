package com.wavy.app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.wavy.app.R;
import com.wavy.app.service.DataFetcher;
import com.wavy.app.restdata.Pair;
import com.wavy.app.restdata.User;
import com.wavy.app.service.DataFetcherException;
import com.wavy.app.util.Constants;
import com.wavy.app.util.Helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadData();
    }

    /**
     * Calling User's data from server
     */
    private void loadData() {

        if (!Helper.isNetworkAvailable(this)) {
            Helper.displayMessage(this, Constants.NO_INTERNET_MSG);
            return;
        }

        DataFetcher dataFetcher = new DataFetcher();

        List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();

        Gson gson = new Gson();
        String paramsJson = gson.toJson(params);

        try {
            dataFetcher.run("GET", Constants.GET_USER, paramsJson, new GetUserCallback());
        } catch (DataFetcherException e) {
            Log.d(Constants.TAG, "DataFetcherException: ", e);
        }

    }

    /**
     * Go to next screen
     */
    private void openNextActivity(String jsonString) {

        User user = new Gson().fromJson(jsonString, User.class);

        Intent intent = new Intent(SplashActivity.this, UserProfileActivity.class);
        intent.putExtra("USER", user);
        startActivity(intent);

        finish();

    }

    private class GetUserCallback implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {
            Helper.displayMessage(SplashActivity.this, Constants.SOMETHING_WRONG_MSG);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            if (response.isSuccessful()) {
                String responseStr = response.body().string();
                openNextActivity(responseStr);

            } else {
                Helper.displayMessage(SplashActivity.this, Constants.SOMETHING_WRONG_MSG);
            }
        }

    }

}
