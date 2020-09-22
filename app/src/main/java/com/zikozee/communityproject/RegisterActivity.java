package com.zikozee.communityproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.zikozee.communityproject.models.User;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    //widgets
    private EditText mEmail, mPassword, mConfirmPassword;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        mConfirmPassword = findViewById(R.id.input_confirm_password);
        Button mRegister = findViewById(R.id.btn_register);
        mProgressBar = findViewById(R.id.progressBar);

        mRegister.setOnClickListener(view -> {
            Log.d(TAG, "onClick: attempting to register.");

            //check for null valued EditText fields
            if (!isEmpty(mEmail.getText().toString())
                    && !isEmpty(mPassword.getText().toString())
                    && !isEmpty(mConfirmPassword.getText().toString())) {

                //check if user has a company email address
                if (isValidDomain(mEmail.getText().toString())) {

                    //check if passwords match
                    if (doStringsMatch(mPassword.getText().toString(), mConfirmPassword.getText().toString())) {

                        //Initiate registration task
                        registerNewEmail(mEmail.getText().toString(), mPassword.getText().toString());
                    } else {
                        Toast.makeText(RegisterActivity.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Please Register with valid Email", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(RegisterActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
            }
        });

        hideSoftKeyboard();
    }

    /**
     * Register a new email and password to Firebase Authentication
     *
     * @param email
     * @param password
     */
    public void registerNewEmail(String email, String password) {
        showProgressbarDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: AuthState: " + mAuth.getCurrentUser().getUid());

                            //send email verification
                            sendVerificationEmail();

                            //insert some Default Data >>>>>>>>>>>> Customer will update later
                            User user = new User.Builder()
                                    .name(email.substring(0, email.indexOf("@")))
                                    .phone("+234xxxxxxxxxx")
                                    .profile_image("")
                                    .user_id(mAuth.getCurrentUser().getUid())
                                    .build();

                            Log.d(TAG, "database status" + mDatabase.getReference().toString());
                            mDatabase.getReference()
                                    .child("transportUsers")
                                    .child(mAuth.getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(saveTask -> {
                                       if(saveTask.isSuccessful()){
                                           mAuth.signOut();
                                           //redirect the user to Login Screen
                                           redirectLoginScreen();
                                       }else{
                                           new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                   .setTitleText("Error Creating user")
                                                   .setContentText("Contact Admin")
                                                   .show();
                                       }
                                    }).addOnFailureListener(fail -> {
                                mAuth.signOut();

                                //redirect the user to the login screen
                                Toast.makeText(RegisterActivity.this, "something went wrong.", Toast.LENGTH_SHORT).show();
                            });
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Unable to Register",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, task.getException().getLocalizedMessage());
                        }
                        hideDialog();
                    }
                });
    }

    /**
     * sends an email verification link to the user
     */
    private void sendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Sent Verification Email", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Couldn't Verification Send Email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        Log.d(TAG, "user status" + user.getUid());

    }

    private boolean doStringsMatch(String s1, String s2) {
        return s1.equals(s2);
    }

    private boolean isEmpty(String string) {
        return string.equals("");
    }

    private void showProgressbarDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Redirects the user to the login screen
     */
    private void redirectLoginScreen() {
        Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void hideDialog() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Returns True if the user's email contains '@tabian.ca'
     *
     * @param email
     * @return
     */
    private boolean isValidDomain(String email) {
        Log.d(TAG, "isValidDomain: verifying email has correct domain: " + email);
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}