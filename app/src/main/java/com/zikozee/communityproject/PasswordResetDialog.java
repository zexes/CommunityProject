package com.zikozee.communityproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PasswordResetDialog extends DialogFragment {

    private static final String TAG = "PasswordResetDialog";

    //widgets
    private EditText mEmail;

    //vars
    private Context mContext;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_resetpassword, container, false);
        mEmail = view.findViewById(R.id.email_password_reset);
        mContext = getActivity();
        mAuth = FirebaseAuth.getInstance();

        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        TextView confirmDialog = view.findViewById(R.id.dialogConfirm);
        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(mEmail.getText().toString())){
                    Log.d(TAG, "onClick: attempting to send reset link to: " + mEmail.getText().toString());
                    sendPasswordResetEmail(mEmail.getText().toString());
                    getDialog().dismiss();
                }

            }
        });

        return view;
    }

    /**
     * Send a password reset link to the email provided
     * @param email
     */
    public void sendPasswordResetEmail(String email){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Password Reset Email sent.");
                            new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Reset Successful")
                                    .setContentText( "Password Reset Link Sent to provided email")
                                    .show();
                        }else{
                            Log.d(TAG, "onComplete: No user associated with that email.");
                            new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Invalid Email")
                                    .setContentText( "No User is Associated with that Email")
                                    .show();

                        }
                    }
                });
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