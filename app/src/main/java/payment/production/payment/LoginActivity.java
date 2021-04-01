package payment.production.payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button login_button;
    TextInputEditText email,password;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        sharedpreferences = getSharedPreferences("payment.production.payment", Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.login_email_text);
        password = findViewById(R.id.login_password_text);
        login_button = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.login_progressbar);
        login_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email_string,password_string;

        email_string = email.getText().toString().trim();
        String email_encode = encodeUserEmail(email_string);
        password_string = password.getText().toString().trim();

        if(email_string.isEmpty()){
            email.setError("Phone Number is Required");
            email.requestFocus();
        }else if(password_string.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
        }else if(password_string.length()<6){
            password.setError("Min password length is 6 characters");
            password.requestFocus();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            Query checkUser = reference.orderByChild("phone").equalTo(email_string);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                         String temp_password = snapshot.child(email_encode).child("password").getValue(String.class);
                        if(temp_password.equals(password_string)) {
                            String balance = snapshot.child(email_encode).child("balance").getValue(String.class);
                            String uname = snapshot.child(email_encode).child("uname").getValue(String.class);
                            String uid = snapshot.child(email_encode).child("uid").getValue(String.class);
                            String phone = snapshot.child(email_encode).child("phone").getValue(String.class);
                            String type = snapshot.child(email_encode).child("type").getValue(String.class);
                            String password = snapshot.child(email_encode).child("password").getValue(String.class);
                            String email  = snapshot.child(email_encode).child("email").getValue(String.class);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("uname", uname);
                            editor.putString("uid", uid);
                            editor.putString("balance", balance);
                            editor.putString("phone", phone);
                            editor.putString("type", type);
                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.commit();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            if (type.equals("user")) {
                                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), Admin_Interface_Activity.class);
                                startActivity(intent);
                                finish();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
}