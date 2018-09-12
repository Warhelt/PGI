package com.example.mariusz.pgi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email)
    EditText loginEditText;
    @BindView(R.id.password)
    EditText passwordEditText;

    private ProgressDialog loginProgress;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }


    @OnClick(R.id.btn_login)
    public void onLoginClick(){
        String email = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            loginProgress.setTitle("Logowanie");
            loginProgress.setMessage("Proszę poczekać, aż sprawdzimy Twoje dane uwierzytelniające.");
            loginProgress.setCanceledOnTouchOutside(false);
            loginProgress.show();
            loginUser(email, password);
        }else{
            Toast.makeText(LoginActivity.this, "Nazwa użytkownika lub hasło jest puste lub nieprawidłowe" , Toast.LENGTH_LONG).show();
        }
    }


    private void loginUser(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loginProgress.dismiss();
                    String current_user_id = mAuth.getCurrentUser().getUid();
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        }
                    });
                } else {
                    loginProgress.hide();
                    String task_result = task.getException().getMessage().toString();
                    Toast.makeText(LoginActivity.this, "Login : " + email + " haslo: "+ password, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @OnClick(R.id.btn_register)
    public void register(){
        Intent registerActivity = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerActivity);
    }


}
