package com.example.androidrestaurantbooking;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.AutofillService;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.androidrestaurantbooking.Common.Common;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int APP_REQUEST_CODE = 1234;
    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.txt_skip)
    TextView txt_skip;

    @OnClick(R.id.btn_login)
    void loginUser()
    {
      startActivityForResult(AuthUI.getInstance()
              .createSignInIntentBuilder()
              .setAvailableProviders(providers).build(),APP_REQUEST_CODE);
    }


    @OnClick(R.id.txt_skip)
    void skipLoginJustGoHome(){
        Intent intent = new Intent(this,HomeActivity.class);
        intent.putExtra(Common.IS_LOGIN,false);
        startActivity(intent);

    }
    @Override
    protected void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        if(authStateListener != null)
            firebaseAuth.removeAuthStateListener(authStateListener);
        super.onStop();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == APP_REQUEST_CODE){

            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK)
            {
                FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();

            }
            else {
                Toast.makeText(this,"Login Cancelled",Toast.LENGTH_SHORT).show();

            }


        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth1 -> {
            FirebaseUser user = firebaseAuth1.getCurrentUser();
            if (user != null) {
                checkUserFromFirebase(user);
            }
        };
        Dexter.withActivity(this)
                .withpermissions(new String[]{
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
        }).withListener(new MultiplePermissionListener(){

        @Override
        public void onPermissionsChecked (MultiplePermissionsReport report){
            FirebaseUser user =  firebaseAuth.getCurrentUser();
        }
            if(user != null)//if already logged


                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra(Common.IS_LOGIN, true);
                startActivity(intent);
                finish();
            } else {
                setContentView(R.layout.activity_main);
                ButterKnife.bind(MainActivity.this);

            }

        }

    private void checkUserFromFirebase(FirebaseUser user) {

    }
}