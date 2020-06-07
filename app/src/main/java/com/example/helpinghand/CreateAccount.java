package com.example.helpinghand;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    public void doCreateAccount (View view) {

        final RadioGroup radioGroup;
        final RadioButton radioButton;

        EditText nameText = findViewById(R.id.createAccountNameTextId);
        final String name = nameText.toString();
        nameText.setText("");

        EditText surnameText = findViewById(R.id.createAccountSurnameTextId);
        final String surname = surnameText.toString();
        surnameText.setText("");

        EditText emailText = findViewById(R.id.createAccountEmailTextId);
        final String email = emailText.toString();
        emailText.setText("");

        EditText phoneNumberText = findViewById(R.id.createAccountPhoneId);
        final String phoneNumber = phoneNumberText.toString();
        phoneNumberText.setText("");

    /*    EditText passwordText = findViewById(R.id.loginActivityPassword);
        String passwordStringLiteral = passwordText.toString();
        String securePassword = getSecurePassword(passwordStringLiteral, salt);
*/

        radioGroup = findViewById(R.id.radioGroup);
        radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2241186/signup.php").newBuilder();

        if(radioButton.getText() == "volunteer"){
            urlBuilder.addQueryParameter("type", "V");
        } else{
            urlBuilder.addQueryParameter("type", "P");
        }

        if(name.isEmpty()){
            nameText.setError("First name required!");
        }else{
            urlBuilder.addQueryParameter("name", name);
        }

        if(surname.isEmpty()){
            surnameText.setError("Surname is required!");
        }else{
            urlBuilder.addQueryParameter("surname", surname);
        }

        if(email.isEmpty()){
            emailText.setError("Email is required!");
        }else{
            urlBuilder.addQueryParameter("email", email);
        }

        if(phoneNumber.isEmpty()){
            phoneNumberText.setError("Phone number is required!");
        }else{
            urlBuilder.addQueryParameter("contact", phoneNumber);
        }

        /*   if(securePassword.isEmpty()){
            passwordText.setError("Password is required!");
        }else{
            urlBuilder.addQueryParameter("password", securePassword);
        }
        */

        urlBuilder.addQueryParameter("address", null);
        urlBuilder.addQueryParameter("image", null);
        urlBuilder.addQueryParameter("password", null);

         String queryurl = urlBuilder.build().toString();
         Request request = new Request.Builder().url(queryurl).build();
         OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                 if(response.isSuccessful()) {
                    final String result = response.body().toString();
                    CreateAccount.this.runOnUiThread(new Runnable() {
                    @Override
                        public void run() {
                            if( result.equals("NULL")) {
                                Toast.makeText(CreateAccount.this, "Account already exists, please sign up!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(radioButton.getText().equals("volunteer")){
                                    if( !(name.isEmpty() && surname.isEmpty() && email.isEmpty() && phoneNumber.isEmpty())){
                                        Intent volunteerIntent = new Intent(CreateAccount.this, VolunteerHomePage.class);
                                        startActivity(volunteerIntent);
                                    }else{
                                        Toast.makeText(CreateAccount.this, "Incomplete sign up form!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else{
                                    if( !(name.isEmpty() && surname.isEmpty() && email.isEmpty() && phoneNumber.isEmpty())) {
                                        Intent patientIntent = new Intent(CreateAccount.this, PatientHomePage.class);
                                        startActivity(patientIntent);

                                    }else{
                                        Toast.makeText(CreateAccount.this, "Please complete sign up form!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                 }
            }
        });
    }

    public void doLoginIntent(View view){
        Intent loginIntent = new Intent(CreateAccount.this, LoginActivity.class);
        startActivity(loginIntent);
    }

}
