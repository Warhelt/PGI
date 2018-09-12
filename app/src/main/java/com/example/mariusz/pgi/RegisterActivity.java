package com.example.mariusz.pgi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.reg_name)
    EditText regNameEditText;
    @BindView(R.id.reg_email)
    EditText regEmailEditText;
    @BindView(R.id.reg_password)
    EditText regPasswordEditText;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog regProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        regProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.reg_create_btn)
    public void createNewAccount(){
        String regName = regNameEditText.getText().toString();
        String regEmail = regEmailEditText.getText().toString();
        String regPassword = regPasswordEditText.getText().toString();

        if(!TextUtils.isEmpty(regName) || !TextUtils.isEmpty(regEmail) || !TextUtils.isEmpty(regPassword)){

            regProgress.setTitle("Rejestracja użytkownika");
            regProgress.setMessage("Twoje konto za moment zostanie utworzone");
            regProgress.setCanceledOnTouchOutside(false);
            regProgress.show();
            registerUser(regName, regEmail, regPassword);
        }
    }


    private void registerUser(final String regName, final String regEmail, final String regPassword) {
        mAuth.createUserWithEmailAndPassword(regEmail, regPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = currentUser.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    String device_token = FirebaseInstanceId.getInstance().getToken();
                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", regName);
                    userMap.put("device_token", device_token);

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                regProgress.dismiss();
                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                            }
                        }
                    });

                } else {
                    regProgress.hide();
                    if(regPassword.length()<5){
                        Toast.makeText(RegisterActivity.this, "Hasło jest za krótkie", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

}
