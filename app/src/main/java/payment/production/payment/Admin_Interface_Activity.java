package payment.production.payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_Interface_Activity extends AppCompatActivity implements View.OnClickListener {
    ImageView admin_create_client,admin_client_list,admin_messages,admin_report,logout;
    SharedPreferences sharedpreferences;
    ImageView logo;
    String type;
    TextView message_counter,report_counter;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__interface_);
        getSupportActionBar().hide();
        sharedpreferences = getSharedPreferences("payment.production.payment", Context.MODE_PRIVATE);
        type = sharedpreferences.getString("type","");



        admin_create_client = findViewById(R.id.admin_create_client);
        admin_client_list = findViewById(R.id.admin_client_list);
        admin_messages = findViewById(R.id.admin_messages);
        admin_report = findViewById(R.id.admin_report);
        logout = findViewById(R.id.admin_logout);
        logo = findViewById(R.id.logo);
        message_counter = findViewById(R.id.message_counter);
        report_counter = findViewById(R.id.report_counter);
        progressBar = findViewById(R.id.admin_progressbar);

        admin_create_client.setOnClickListener(this);
        admin_client_list.setOnClickListener(this);
        admin_messages.setOnClickListener(this);
        admin_report.setOnClickListener(this);
        logout.setOnClickListener(this);
        logo.setOnClickListener(this);
        update_value();
    }

    private void update_value() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().child("Counter").child("Report")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Integer data = Integer.parseInt(snapshot.getValue().toString());
                        if(data>0) {
                            if (data <= 5) {
                                report_counter.setText(data.toString());
                            } else {
                                report_counter.setText("5+");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        FirebaseDatabase.getInstance().getReference().child("Counter").child("Message")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Integer data = Integer.parseInt(snapshot.getValue().toString());
                        if(data>0) {
                            if (data <= 5) {
                                message_counter.setText(data.toString());
                            } else {
                                message_counter.setText("5+");
                            }

                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_create_client:
                Intent create_client = new Intent(getApplicationContext(),CreateClientActivity.class);
                startActivity(create_client);
                break;

            case R.id.admin_report:
                Intent report_intent = new Intent(getApplicationContext(),AdminReportActivity.class);
                startActivity(report_intent);
                finish();
                break;

            case R.id.admin_messages:
                Intent message_intent = new Intent(getApplicationContext(),AdminMessagesActivity.class);
                message_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(message_intent);
                break;

            case R.id.admin_client_list:
                Intent client_list = new Intent(getApplicationContext(),ClientListActivity.class);
                startActivity(client_list);
                break;

            case R.id.logo:
                leave_page();
                break;

            case R.id.admin_logout:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("You are going to logout");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("uname", "");
                        editor.putString("uid", "");
                        editor.putString("balance","");
                        editor.putString("phone", "");
                        editor.putString("type", "");
                        editor.putString("email", "");
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
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
                break;

        }
    }

    public void leave_page(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Are you sure?");
        dialog.setMessage("You are going to leave this page!!!!");
        dialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(),Admin_Interface_Activity.class);
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

    @Override
    protected void onRestart() {
        update_value();
        super.onRestart();
    }
}