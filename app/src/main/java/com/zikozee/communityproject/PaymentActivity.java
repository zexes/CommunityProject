package com.zikozee.communityproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.zikozee.communityproject.utility.email.SendMailTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = "PAYMENT_ACTIVITY";
    private double price;
    private int chargeAmount;
    private String vendorName;
    private String start;
    private String destCity;
    private String destState;
    private EditText cardText, monthText, yearText, cvvText;
    private FirebaseAuth auth;
    private final LocalDateTime dateTime = LocalDateTime.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        auth = FirebaseAuth.getInstance();

        //Initialize SDK
        PaystackSdk.initialize(getApplicationContext());

        Intent intent = getIntent();
        price = intent.getDoubleExtra("fare_price",0.0);
        vendorName = intent.getStringExtra("vendor");
        start = intent.getStringExtra("startLocation");
        destCity = intent.getStringExtra("destinationCity");
        destState = intent.getStringExtra("destinationState");
        chargeAmount = (int)price *100;// amount in kobo
        Toast.makeText(this, "This Price: " + price, Toast.LENGTH_SHORT).show();

        TextView fare = findViewById(R.id.fare_price_submit);
        fare.setText(String.valueOf(price));
        cardText = findViewById(R.id.card_number);
        cardFormatter(cardText);
        monthText = findViewById(R.id.expiry_month);
        yearText = findViewById(R.id.expiry_year);
        cvvText = findViewById(R.id.cvv);
        Button paymentButton = findViewById(R.id.pay_now);

        paymentButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        ValidateAndCharge();
    }

    public void ValidateAndCharge(){
        String cardNumber = cardText.getText().toString();
        String month = monthText.getText().toString();
        String year = yearText.getText().toString();
        String cvv = cvvText.getText().toString();

        Log.d(TAG, "cardnumber" + cardNumber);
        cardNumber = cardNumber.replace("-", "");
        if(cardNumber.equals("") || cardNumber.length()< 16 || cardNumber.length()> 18){
            Toast.makeText(PaymentActivity.this, "card number not valid", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "card number: " + cardNumber);
            return;
        }
        if(month.equals("") || month.length()< 2 || Integer.parseInt(month) > 12){
            Toast.makeText(PaymentActivity.this, "card not valid", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "month : " + month);
            return;
        }
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        Log.d(TAG, " month: "+currentMonth);
        if(year.equals("") || year.length()< 2 || Integer.parseInt(year) < (currentYear/100)){
            Toast.makeText(PaymentActivity.this, "year not valid", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "year : " + year);
            return;
        }

        if(Integer.parseInt(month)< currentMonth || Integer.parseInt(year) < (currentYear/100)){
            Toast.makeText(PaymentActivity.this, "check month and year", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "year and month respectively: " + year + ", " + month);
            return;
        }
        if(cvv.equals("") || cvv.length()< 3){
            Toast.makeText(PaymentActivity.this, "cvv not valid", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "cvv : " + cvv);

            return;
        }

        int expiryMonth = Integer.parseInt(month);
        int expiryYear = Integer.parseInt(year);

        Card card = new Card(cardNumber, expiryMonth, expiryYear, cvv);
        if(card.isValid()){
            performCharge(card);
        }else {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Input Error")
                    .setContentText("Card is not valid")
                    .show();
        }
    }

    public void performCharge(Card card){
        //create a Charge object
        Charge charge = new Charge();
        charge.setCard(card); //sets the card to charge
        charge.setCurrency("NGN");
        charge.setAmount(chargeAmount);
        charge.setEmail("ezekiel.eromosei@gmail.com");
//        charge.setAccessCode()

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        PaystackSdk.chargeCard(PaymentActivity.this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                pDialog.dismissWithAnimation();
                // This is called only after transaction is deemed successful.
                // Retrieve the transaction, and send its reference to your server
                // for verification.
                String fromEmail = "godlydeveloper84@gmail.com";
                String fromPassword = "Godly2##@";
                String toEmails = auth.getCurrentUser().getEmail();
                String emailSubject = "Transport Adviser Receipt";
                String emailBody = "<i>Dear Customer,</i><br><br>";
                emailBody += "Thank you for subscribing for your fare payment via our Platform. Below is the Receipt<br>";
                emailBody += "Vendor: <b>" + vendorName +"</b><br>";
                emailBody += "Fare Amount: <b>" + price +"</b><br>";
                emailBody += "Boarding Location: <b>" + start +"</b><br>";
                emailBody += "Destination State: <b>" + destState +"</b><br>";
                emailBody += "Destination City: <b>" + destCity +"</b><br>";
                emailBody += "Date: <b>" + dateTime.format(DateTimeFormatter.ofPattern("d:MMM:uuuu HH:mm:ss")) +"</b><br>";


                new SendMailTask(PaymentActivity.this).execute(fromEmail,
                        fromPassword, toEmails, emailSubject, emailBody);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SweetAlertDialog ss = new SweetAlertDialog(PaymentActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                ss.setTitleText("Successful!");
                ss.setContentText("Email Notification Sent");
                ss.setConfirmText("Back to vendor Screen");
                ss.setConfirmClickListener(sweetAlertDialog -> {
                            startActivity(new Intent(PaymentActivity.this, SignedInActivity.class));
                            finish();
                            ss.dismissWithAnimation();
                        });
                ss.show();
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                pDialog.dismissWithAnimation();
                // This is called only before requesting OTP.
                // Save reference so you may send to server. If
                // error occurs with OTP, you should still verify on server.
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                pDialog.dismissWithAnimation();
                //handle error here
                Log.d(TAG, "error: " + error.getLocalizedMessage());
                new SweetAlertDialog(PaymentActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error!!!")
                        .setContentText("Check Internet or Card Details")
                        .show();
            }
        });
    }

    private void cardFormatter(EditText editText){
        editText.addTextChangedListener(new TextWatcher() {

            private static final int TOTAL_SYMBOLS = 22; // size of pattern 0000-0000-0000-0000
            private static final int TOTAL_DIGITS = 18; // max numbers of digits in pattern: 0000 x 4
            private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
            private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
            private static final char DIVIDER = '-';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // noop
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // noop
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(0, s.length(), buildCorrectString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                }
            }

            private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                for (int i = 0; i < s.length(); i++) { // check that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect &= divider == s.charAt(i);
                    } else {
                        isCorrect &= Character.isDigit(s.charAt(i));
                    }
                }
                return isCorrect;
            }

            private String buildCorrectString(char[] digits, int dividerPosition, char divider) {
                final StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < digits.length; i++) {
                    if (digits[i] != 0) {
                        formatted.append(digits[i]);
                        if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                            formatted.append(divider);
                        }
                    }
                }

                return formatted.toString();
            }

            private char[] getDigitArray(final Editable s, final int size) {
                char[] digits = new char[size];
                int index = 0;
                for (int i = 0; i < s.length() && index < size; i++) {
                    char current = s.charAt(i);
                    if (Character.isDigit(current)) {
                        digits[index] = current;
                        index++;
                    }
                }
                return digits;
            }
        });
    }

}