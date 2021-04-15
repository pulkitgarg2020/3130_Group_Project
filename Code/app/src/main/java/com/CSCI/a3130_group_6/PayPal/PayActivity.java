package com.CSCI.a3130_group_6.PayPal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;

import androidx.annotation.Nullable;

import com.CSCI.a3130_group_6.HelperClases.Config;
import com.CSCI.a3130_group_6.R;
import com.CSCI.a3130_group_6.Registration.RegistrationForEmployers;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;


import android.widget.Toast;

import java.math.BigDecimal;

public class PayActivity extends AppCompatActivity {

    private static final int PAYPAL_REQUEST_CODE = 555;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Config.PAYPAL_CLIENT_ID);

    Button btnPayNow;
    String employeeName, amount, key, employerName, wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);
        Intent intentData = getIntent();
        employeeName = intentData.getStringExtra("name");
        employerName = intentData.getStringExtra("employerName");
        amount = intentData.getStringExtra("amount");
        key = intentData.getStringExtra("key");
        wallet = intentData.getStringExtra("wallet");

        //set client id to proper individual
        //config.;

        //Starting the PayPal Service

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        btnPayNow = findViewById(R.id.btnPayNow);

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment();
            }
        });



    }

    private void processPayment() {
        // set amount to the pay of task
        //amount =0; //edtAmount.getText().toString();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),"CAD",
                "Purchase Goods",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
        switchToEmployer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString();
                       System.out.println("Payment Success Retrieving payment object" +paymentDetails);
                        //Log.d("Details",paymentDetails);
                        startActivity(new Intent(this,PaymentStatus.class)
                                .putExtra("PaymentDetails",paymentDetails)
                                .putExtra("Amount",amount).putExtra("employeeName", employeeName).putExtra("key", key).putExtra("employerName", employerName).putExtra("wallet", wallet));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }

    public void switchToEmployer() {
        Intent employer = new Intent(this, PayActivity.class);
        startActivity(employer);
    }


}