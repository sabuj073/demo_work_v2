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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.snapshot.ChildKey;

public class UserPaymentActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView,balance;
    SharedPreferences sharedpreferences;

    TextInputEditText phone_number,amount;

    RadioGroup user_payment_send_money_cashout,user_payment_personal_agent;
    String payment_number,payment_amount,transection_type,payment_type,balance_data,client_email,number_string,name,user_id;
    Button confirm;
    ProgressBar payment_progressbar;
    private FirebaseAuth mAuth;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        sharedpreferences = getSharedPreferences("payment.production.payment", Context.MODE_PRIVATE);
        balance_data = sharedpreferences.getString("balance","");
        client_email = sharedpreferences.getString("email","");
        number_string = sharedpreferences.getString("phone","");
        name= sharedpreferences.getString("uname","");
        user_id= sharedpreferences.getString("uid","");

        textView = findViewById(R.id.textView2);
        balance = findViewById(R.id.user_balance);
        user_payment_send_money_cashout = findViewById(R.id.user_payment_send_money_cashout);
        user_payment_personal_agent = findViewById(R.id.user_payment_personal_agent);
        phone_number = findViewById(R.id.payment_phone);
        amount = findViewById(R.id.payment_amount);
        confirm = findViewById(R.id.user_payment_confirm);
        payment_progressbar = findViewById(R.id.payment_progressbar);
        balance.setText("\u09F3 "+balance_data);
        confirm.setOnClickListener(this);
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
            String type =data.getString("Type");
            textView.setText("Cash Out/ Send Money\n"+type);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_payment_confirm:
                payment_number = phone_number.getText().toString().trim();
                payment_amount = amount.getText().toString().trim();
                int temp_id = user_payment_send_money_cashout.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton)findViewById(temp_id);
                transection_type = radioButton.getText().toString().trim();
                int temp_id1 = user_payment_personal_agent.getCheckedRadioButtonId();
                radioButton = (RadioButton)findViewById(temp_id1);
                payment_type = radioButton.getText().toString().trim();
                payment_progressbar.setVisibility(View.VISIBLE);
                if(Integer.parseInt(balance_data)> Integer.parseInt(payment_amount)) {

                    Intent confirm = new Intent(getApplicationContext(),UserPaymentConfirmActivity.class);
                    confirm.putExtra("number",payment_number);
                    confirm.putExtra("amount",payment_amount);
                    confirm.putExtra("payment_type",payment_type);
                    confirm.putExtra("transection_type",transection_type);
                    payment_progressbar.setVisibility(View.INVISIBLE);
                    startActivity(confirm);

                }else{
                    amount.setError("Insufficient Balance");
                    amount.requestFocus();
                    payment_progressbar.setVisibility(View.INVISIBLE);
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

}