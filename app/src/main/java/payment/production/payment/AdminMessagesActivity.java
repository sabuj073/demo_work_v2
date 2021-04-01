package payment.production.payment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminMessagesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<PaymentModel> modelList;
    ProgressBar progressBar;
    ImageView logo;
    TextView message_counter,report_counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_messages);
        getSupportActionBar().hide();


        recyclerView = findViewById(R.id.message_recylerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = findViewById(R.id.admin_messageProgressbar);
        logo = findViewById(R.id.logo);
        update_message();

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leave_page();
            }
        });

        modelList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().child("Payments")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Payment payment = snapshot.getValue(Payment.class);
                            String key = snapshot.getKey();
                            modelList.add(new PaymentModel(payment.number,payment.amount,payment.transection_type,payment.payment_type,payment.from_number,payment.from_email,payment.from_name,payment.from_id,key));
                        }
                        adapter = new PaymentAdapter(AdminMessagesActivity.this,modelList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    public void leave_page(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Are you sure?");
        dialog.setMessage("You are going to leave this page!!!!");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(),Admin_Interface_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            private void startActivity(Intent intent) {
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

    public void update_message(){
        FirebaseDatabase.getInstance().getReference().child("Counter").child("Message")
                .setValue("0");
    }

    @Override
    public void onBackPressed() {
        Log.e("Backpresses","True");
        Intent intent = new Intent(getApplicationContext(),Admin_Interface_Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}