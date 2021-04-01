package payment.production.payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserPaymentConfirmActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedpreferences;
    String payment_number,payment_amount,transection_type,payment_type,balance_data,client_email,number_string,name,user_id,password,type;
    TextInputEditText confirm_password;
    TextView number_holder,amount_holder,payment_type_holder,receiver_type_holder,textView,balance,service_holder;
    Button user_payment_confirm;
    ProgressBar progressBar;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment_confirm);
        getSupportActionBar().hide();
        sharedpreferences = getSharedPreferences("payment.production.payment", Context.MODE_PRIVATE);

        balance_data = sharedpreferences.getString("balance","");
        client_email = sharedpreferences.getString("email","");
        number_string = sharedpreferences.getString("phone","");
        name= sharedpreferences.getString("uname","");
        user_id= sharedpreferences.getString("uid","");
        password = sharedpreferences.getString("password","");

        confirm_password = findViewById(R.id.confirm_password_text);
        number_holder = findViewById(R.id.number_holder);
        amount_holder = findViewById(R.id.amount_holder);
        payment_type_holder = findViewById(R.id.payment_type_holder);
        receiver_type_holder = findViewById(R.id.receiver_type_holder);
        textView = findViewById(R.id.textView2);
        balance = findViewById(R.id.user_balance);
        service_holder = findViewById(R.id.service_holder);
        user_payment_confirm = findViewById(R.id.user_payment_confirm);
        progressBar = findViewById(R.id.payment_progressbar);
        user_payment_confirm.setOnClickListener(this);
        balance.setText("\u09F3 "+balance_data);

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
            payment_number =data.getString("number");
            payment_amount =data.getString("amount");
            payment_type = data.getString("payment_type");
            transection_type = data.getString("transection_type");
            type = data.getString("type");
            number_holder.setText(payment_number);
            amount_holder.setText(payment_amount);
            payment_type_holder.setText(payment_type);
            receiver_type_holder.setText(transection_type);
            service_holder.setText(type);
        }
    }

    private void update(String payment_amount) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        balance_data = String.valueOf(Integer.parseInt(balance_data)-Integer.parseInt(payment_amount));
        editor.putString("balance", balance_data);
        balance.setText("\u09F3 "+balance_data);
        FirebaseDatabase.getInstance().getReference("Users")
                .child(encodeUserEmail(number_string))
                .child("balance")
                .setValue(balance_data);

    }

    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_payment_confirm:
                progressBar.setVisibility(View.VISIBLE);
                String confirm = confirm_password.getText().toString();
                if(confirm.equals(password)){
                    Payment payment = new Payment(payment_number, payment_amount, transection_type, payment_type,number_string,client_email,name,user_id,type);

                    FirebaseDatabase.getInstance().getReference("Payments")
                            .push()
                            .setValue(payment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                update_counter();
                                update(payment_amount);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(UserPaymentConfirmActivity.this);
                                progressBar.setVisibility(View.INVISIBLE);
                                dialog.setTitle("Congratz");
                                dialog.setMessage("Payment Successful");
                                dialog.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getApplicationContext(),UserActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                AlertDialog alertDialog = dialog.create();
                                alertDialog.show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(getApplicationContext(),"Invalid Password",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
        }
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
        FirebaseDatabase.getInstance().getReference().child("Counter").child("Message")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Integer count =  Integer.parseInt(snapshot.getValue().toString());
                        count++;
                        FirebaseDatabase.getInstance().getReference().child("Counter").child("Message")
                                .setValue(count.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}