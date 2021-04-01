package payment.production.payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {

    Handler handler;
    int progress = 0;
    ProgressBar progressBar;
    SharedPreferences sharedpreferences;
    String email_string,number_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        progressBar = findViewById(R.id.progressBar);
        setProgressBarValue(progress);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedpreferences = getSharedPreferences("payment.production.payment", Context.MODE_PRIVATE);
                String name = sharedpreferences.getString("uname","");
                email_string = sharedpreferences.getString("email","");
                String type = sharedpreferences.getString("type","");
                number_string = sharedpreferences.getString("phone","");
                String email_encode = encodeUserEmail(number_string);

                if(!number_string.equals("")){
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                    Query checkUser = reference.orderByChild("phone").equalTo(email_string);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    if(type.equals("user")) {
                        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(getApplicationContext(), Admin_Interface_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        },2000);
    }

    private void  setProgressBarValue(final int progress){
        progressBar.setProgress(progress);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setProgressBarValue(progress+50);
            }
        });
        thread.start();
    }
    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
}