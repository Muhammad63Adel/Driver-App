package com.example.driver;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;



public class check_qr extends AppCompatActivity {

    String user_name ="";
    String user_email="";
    String latitude ;
    String longitude ;
    TextView result_text;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_qr);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        user_name = bundle.getString("user_name");
        user_email = bundle.getString("user_email");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessageDatabaseReference = mFirebaseDatabase.getReference().child("targets");

    }

    public void read_qr(View view){

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode, data);
        if (result != null){
            if(result.getContents() == null){
                Toast.makeText(this, "you cancelled the scanning ",Toast.LENGTH_SHORT).show();

            }else{

                String str = result.getContents();
                try {
                    JSONObject jObj = new JSONObject(str);
                     latitude = jObj.getString("latitude");
                     longitude = jObj.getString("longitude");

                    Toast.makeText(check_qr.this, "latitude = " + latitude +"\n"+"longitude = " + longitude, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(check_qr.this, " the data doesn't belong to the order ", Toast.LENGTH_SHORT).show();
                }


                Intent i = new Intent(check_qr.this,google_maps.class);
                if (latitude != null && longitude != null) {
                    Driver driver = new Driver(user_email,user_name);
                    target target = new target(latitude,longitude,driver);
                    mMessageDatabaseReference.push().setValue(target);
                    i.putExtra("latitude", latitude);
                    i.putExtra("longitude", longitude);
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(check_qr.this, " No location founded in the QR code ", Toast.LENGTH_SHORT).show();
                }
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }





}
