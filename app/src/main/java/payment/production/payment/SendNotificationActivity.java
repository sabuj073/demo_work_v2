    package payment.production.payment;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.RecyclerView;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.ProgressBar;
    import android.widget.Spinner;
    import android.widget.Toast;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.android.material.textfield.TextInputEditText;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.Query;
    import com.google.firebase.database.ValueEventListener;

    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;
    import java.util.Locale;

    public class SendNotificationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
        private Spinner spinner;
        List<UserModel> userModelList;
        List<String> names;
        RecyclerView recyclerView;
        TextInputEditText report_text;
        RecyclerView.Adapter adapter;
        ProgressBar progressBar;
        RecyclerView.LayoutManager layoutManager;
        String date,time;
        Button report_send;
        int number;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_send_notification);
            getSupportActionBar().hide();
            spinner = (Spinner)findViewById(R.id.spinner);
            userModelList = new ArrayList<>();
            names= new ArrayList<String>();
            report_send = findViewById(R.id.send_report_btn);
            report_text = findViewById(R.id.report_text);
            progressBar = findViewById(R.id.progress_notification);
            date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            time = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(new Date());

            DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference().child("Users");
            Query query = databaseReference.orderByChild("type").equalTo("user");
            progressBar.setVisibility(View.VISIBLE);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        userModelList.add(new UserModel(user.uname,user.uid,user.phone,user.balance,user.email,user.type,user.password));
                        names.add(user.uname);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,names);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    progressBar.setVisibility(View.GONE);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            spinner.setOnItemSelectedListener(this);

            report_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String report = report_text.getText().toString().trim();
                    if(report.equals("")){
                        report_text.setError("Non empty required");
                        report_text.requestFocus();
                    }else{
                        progressBar.setVisibility(View.VISIBLE);
                        Notification report_class = new Notification(report,userModelList.get(number).getEmail(),userModelList.get(number).getUname(),userModelList.get(number).getUid(),userModelList.get(number).getPhone(),date,time,"new");
                        FirebaseDatabase.getInstance().getReference("Notifications")
                                .push()
                                .setValue(report_class).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    update_counter(userModelList.get(number).getPhone());
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(),"Notification Sent",Toast.LENGTH_SHORT).show();
                                    report_text.setText("");
                                    Intent intent = new Intent(getApplicationContext(),Admin_Interface_Activity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                }
            });




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
                                        Integer count = 0;
                                            if(snapshot.getValue()==null){
                                                count = 1;
                                            }else {
                                                count = Integer.parseInt(snapshot.getValue().toString());
                                                count++;
                                            }
                                            FirebaseDatabase.getInstance().getReference().child("NotificationCounter").child(removespecial(number))
                                                    .setValue(count);
                                        }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }else{
                        FirebaseDatabase.getInstance().getReference("NotificationCounter")
                                .child(removespecial(number))
                                .setValue(1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        }

        private String removespecial(String str) {
            str = str.replaceAll("[-+^]*", "");
            return str;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            number = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }


    }