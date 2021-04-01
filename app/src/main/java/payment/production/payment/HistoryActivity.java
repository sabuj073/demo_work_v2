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

        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.List;

        public class HistoryActivity extends AppCompatActivity {

            RecyclerView recyclerView;
            RecyclerView.Adapter adapter;
            RecyclerView.LayoutManager layoutManager;
            List<PaymentModel> modelList;
            ProgressBar progressBar;
            String email_text,number_text;
            ImageView logo;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_history);
                getSupportActionBar().hide();

                recyclerView = findViewById(R.id.message_recylerView);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                progressBar = findViewById(R.id.admin_messageProgressbar);

                logo = findViewById(R.id.logo);
                logo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        leave_page();
                    }
                });

                Bundle data = getIntent().getExtras();
                if(data!=null)
                {
                    email_text =data.getString("email");
                    number_text =data.getString("number");
                }

                modelList = new ArrayList<>();
                progressBar.setVisibility(View.VISIBLE);
                FirebaseDatabase.getInstance().getReference().child("Payments")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Payment payment = snapshot.getValue(Payment.class);
                                    if(payment.from_number.equals(number_text)) {
                                        modelList.add(new PaymentModel(payment.number, payment.amount, payment.transection_type, payment.payment_type, "", payment.from_email, payment.from_name, payment.from_id,""));
                                    }else if(payment.number.equals(number_text)){
                                        modelList.add(new PaymentModel(payment.number, payment.amount, payment.transection_type, payment.payment_type, payment.from_number, payment.from_email, payment.from_name, payment.from_id,""));
                                    }
                                }
                                adapter = new HistoryAdapter(getApplicationContext(),modelList);
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
            }
