package payment.production.payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserSectionNotificationMainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<ReportModel> reportList;
    ProgressBar progressBar;
    ImageView logo;
    String number_text;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_section_notification_main);
        getSupportActionBar().hide();
        //update_report();

        recyclerView = findViewById(R.id.recylerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = findViewById(R.id.admin_report_progress_bar);
        logo = findViewById(R.id.logo);
        sharedpreferences = getSharedPreferences("payment.production.payment", Context.MODE_PRIVATE);
        number_text = sharedpreferences.getString("phone","");
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leave_page();
            }
        });

        reportList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().child("Notifications")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Notification report = snapshot.getValue(Notification.class);
                            String key = snapshot.getKey();
                            if(report.getNumber().equals(number_text) && report.getStatus().equals("new")) {
                                reportList.add(new ReportModel(report.text, report.email, report.name, report.userid, report.number, key, report.date, report.time));
                            }

                        }
                        adapter = new NotificationAdapter(UserSectionNotificationMainActivity.this, reportList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        update_counter(number_text);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void leave_page() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Are you sure?");
        dialog.setMessage("You are going to leave this page!!!!");
        dialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
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

    public void update_report(String number) {
        /*FirebaseDatabase.getInstance().getReference().child("Counter").child("Report")
                .setValue("0");*/
        /*DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Notification");

        ref.orderByChild("number").equalTo(number).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        String key=datas.getKey();
                        ref.child(key).child("status").setValue("old");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });*/
    }

    private void update_counter(String number)
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("NotificationCounter")) {
                    FirebaseDatabase.getInstance().getReference().child("NotificationCounter").child(removespecial(number))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    FirebaseDatabase.getInstance().getReference().child("NotificationCounter").child(removespecial(number))
                                            .setValue(0);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    @Override
    public void onBackPressed() {
        Log.e("Backpresses", "True");
        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    private String removespecial(String str) {
        str = str.replaceAll("[-+^]*", "");
        return str;
    }
}