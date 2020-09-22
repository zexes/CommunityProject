package com.zikozee.communityproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ResendVerificationDialog extends DialogFragment {

    private static final String TAG = "ResendVerificatnDialog";

    //widgets
    private EditText mConfirmPassword, mConfirmEmail;

    //vars
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_resend_verification, container, false);
        mConfirmPassword = view.findViewById(R.id.confirm_password);
        mConfirmEmail = view.findViewById(R.id.confirm_email);
        mContext = getActivity();
        mAuth = FirebaseAuth.getInstance();

        FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);


        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        TextView confirmDialog = view.findViewById(R.id.dialogConfirm);
        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to resend verification email.");

                if(!isEmpty(mConfirmEmail.getText().toString())
                        && !isEmpty(mConfirmPassword.getText().toString())){

                    //temporarily authenticate and resend verification email
                    authenticateAndResendEmail(mConfirmEmail.getText().toString(),
                            mConfirmPassword.getText().toString());
                }else{
                    Toast.makeText(mContext, "all fields must be filled out", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Cancel button for closing the dialog
        TextView cancelDialog = (TextView) view.findViewById(R.id.dialogCancel);
        cancelDialog.setOnClickListener(v -> getDialog().dismiss());

        return view;
    }


    /**
     * reauthenticate so we can send a verification email again
     * @param email
     * @param password
     */
    private void authenticateAndResendEmail(String email, String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(email.trim(), password.trim());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: re-authentication successful.");
                            sendVerificationEmail();
                            mAuth.signOut();
                            getDialog().dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Invalid Credentials")
                        .setContentText( "Reset your password and try again")
                        .show();
                getDialog().dismiss();
            }
        });
    }

    /**
     * sends an email verification link to the user
     */
    public void sendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Re-sent Verification Email")
                                        .show();
                            }
                            else{
                                new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("something went wrong")
                                        .setContentText( "couldn't send email")
                                        .show();
                            }
                        }
                    });
        }

    }

    /**
     * Return true if the @param is null
     * @param string
     * @return
     */
    private boolean isEmpty(String string){
        return string.equals("");
    }


}

















