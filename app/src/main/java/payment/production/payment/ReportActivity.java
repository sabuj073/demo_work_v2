package payment.production.payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReportActivity extends AppCompatActivity {

    TextInputEditText report_text;
    Button report_send;
    String client_email,report,name,user_id,number;
    SharedPreferences sharedpreferences;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportActionBar().hide();

        sharedpreferences = getSharedPreferences("payment.production.payment", Context.MODE_PRIVATE);
        client_email = sharedpreferences.getString("email","");
        name= sharedpreferences.getString("uname","");
        user_id= sharedpreferences.getString("uid","");
        number= sharedpreferences.getString("phone","");

        report_text = findViewById(R.id.report_text);
        report_send = findViewById(R.id.send_report_btn);
        progressBar = findViewById(R.id.report_progressbar);
        mAuth = FirebaseAuth.getInstance();

        logo = findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leave_page();
            }
        });

        report_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report = report_text.getText().toString().trim();
                if(report.equals("")){
                    report_text.setError("Non empty required");
                    report_text.requestFocus();
                }else{
                    //progressBar.setVisibility(View.VISIBLE);
                    Report report_class = new Report(report,client_email,name,user_id,number);
                    FirebaseDatabase.getInstance().getReference("Reports")
                            .push()
                            .setValue(report_class).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                update_counter();
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(),"Report Sent",Toast.LENGTH_SHORT).show();
                                report_text.setText("");
                                Intent intent = new Intent(getApplicationContext(),UserActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }

    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
    public void leave_page(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Are you sure?");
        dialog.setMessage("You are going to leave this page!!!!");
        dialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(),UserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    public void update_counter(){
        FirebaseDatabase.getInstance().getReference().child("Counter").child("Report")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Integer count =  Integer.parseInt(snapshot.getValue().toString());
                        count++;
                        FirebaseDatabase.getInstance().getReference().child("Counter").child("Report")
                                .setValue(count.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}