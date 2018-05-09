package com.wavy.app.activity;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
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

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtFullName;
    private TextView txtPhone;
    private TextView txtEmail;
    private ImageButton btnDelete;
    private CircleImageView userDP;
    private User user;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (User) getIntent().getSerializableExtra("USER");

        initViews();
        displayData(user);
        initListeners();

    }

    private void initViews() {
        txtFullName = (TextView) findViewById(R.id.txt_full_name);
        txtPhone = (TextView) findViewById(R.id.txt_phone);
        txtEmail = (TextView) findViewById(R.id.txt_email);
        btnDelete = (ImageButton) findViewById(R.id.btn_delete);
        userDP = (CircleImageView) findViewById(R.id.dp);
    }

    private void initListeners() {
        btnDelete.setOnClickListener(this);
    }

    private void deleteUserPhoneNumber() {
        if (!Helper.isNetworkAvailable(this)) {
            Helper.displayMessage(this, Constants.NO_INTERNET_MSG);
            return;
        }

        showProgress();

        DataFetcher dataFetcher = new DataFetcher();

        List<Pair<String, String>> params = new ArrayList<Pair<String, String>>();
        params.add(new Pair<String, String>("id", user.getId()));

        Gson gson = new Gson();
        String paramsJson = gson.toJson(params);

        Log.d(Constants.TAG, paramsJson);

        try {
            dataFetcher.run("DELETE", Constants.DELETE_USER, paramsJson, new DeleteUserCallback());
        } catch (DataFetcherException e) {
            Log.d(Constants.TAG, "DataFetcherException: ", e);
        }
    }

    private void displayData(User user) {
        String fullName = user.getFirstName() + " " + user.getLastName();

        txtFullName.setText(fullName);
        txtPhone.setText(user.getPhoneNumber());
        txtEmail.setText(user.getEmail());

        Picasso.with(this)
                .load(user.getProfilePicture())
                .resize(512, 512)
                .centerCrop()
                .placeholder(R.drawable.default_dp)
                .error(R.drawable.default_dp)
                .into(userDP);
    }

    private void showProgress() {
        progressDialog = new ProgressDialog(UserProfileActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage(Constants.DELETING_PHONE_MSG);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (txtPhone.getText().toString().trim().isEmpty()) {
            Helper.displayMessage(UserProfileActivity.this, Constants.PHONE_DELETED_MSG);
        } else {
            deleteUserPhoneNumber();
        }
    }

    private class DeleteUserCallback implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {
            progressDialog.dismiss();
            Helper.displayMessage(UserProfileActivity.this, Constants.SOMETHING_WRONG_MSG);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            progressDialog.dismiss();

            if (response.isSuccessful()) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        txtPhone.setText("");
                    }
                });

                Helper.displayMessage(UserProfileActivity.this, Constants.DELETE_PHONE_SUCCESS_MSG);

            } else {
                Helper.displayMessage(UserProfileActivity.this, Constants.SOMETHING_WRONG_MSG);
            }
        }

    }

}
