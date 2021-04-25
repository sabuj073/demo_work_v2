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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class UserRechargeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView,balance;
    SharedPreferences sharedpreferences;

    TextInputEditText phone_number,amount;

    RadioGroup user_payment_send_money_cashout,user_payment_personal_agent;
    String payment_number,payment_amount,transection_type,payment_type,balance_data,client_email,number_string,name,user_id;
    ImageView gp,bl,airtel,teletalk,robi,foreignRecharge;
    TextView gp_text,bl_text,airtel_text,teletalk_text,robi_text,foreignRechargeText;
    Button confirm;
    ProgressBar payment_progressbar;
    private FirebaseAuth mAuth;
    String type;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recharge);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        sharedpreferences = getSharedPreferences("payment.production.payment", Context.MODE_PRIVATE);
        balance_data = sharedpreferences.getString("balance","");
        client_email = sharedpreferences.getString("email","");
        number_string = sharedpreferences.getString("phone","");
        name= sharedpreferences.getString("uname","");
        user_id= sharedpreferences.getString("uid","");
        transection_type="";

        textView = findViewById(R.id.textView2);
        balance = findViewById(R.id.user_balance);
        gp = findViewById(R.id.gp);
        gp_text = findViewById(R.id.gp_text);
        bl = findViewById(R.id.bl);
        bl_text = findViewById(R.id.bl_text);
        airtel = findViewById(R.id.airtel);
        airtel_text = findViewById(R.id.airtel_text);
        teletalk = findViewById(R.id.teletalk);
        teletalk_text = findViewById(R.id.teletalk_text);
        robi = findViewById(R.id.robi);
        robi_text = findViewById(R.id.robi_text);
        user_payment_send_money_cashout = findViewById(R.id.user_payment_send_money_cashout);
        user_payment_personal_agent = findViewById(R.id.user_payment_personal_agent);
        phone_number = findViewById(R.id.payment_phone);
        amount = findViewById(R.id.payment_amount);
        confirm = findViewById(R.id.user_payment_confirm);
        payment_progressbar = findViewById(R.id.payment_progressbar);
        foreignRecharge = findViewById(R.id.foreignRecharge);
        foreignRechargeText = findViewById(R.id.foreignRechargeText);
        balance.setText("\u09F3 "+balance_data);
        confirm.setOnClickListener(this);
        gp.setOnClickListener(this);
        gp_text.setOnClickListener(this);
        bl.setOnClickListener(this);
        gp_text.setOnClickListener(this);
        airtel.setOnClickListener(this);
        airtel_text.setOnClickListener(this);
        teletalk.setOnClickListener(this);
        teletalk_text.setOnClickListener(this);
        robi.setOnClickListener(this);
        robi_text.setOnClickListener(this);
        logo = findViewById(R.id.logo);
        foreignRecharge.setOnClickListener(this);
        foreignRechargeText.setOnClickListener(this);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leave_page();
            }
        });

        Bundle data = getIntent().getExtras();
        if(data!=null)
        {
            type =data.getString("Type");
            textView.setText(type);
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.robi:

            case R.id.robi_text:
                transection_type = "Robi";
                gp.animate().alpha((float) 0.3);
                bl.animate().alpha((float) 0.3);
                airtel.animate().alpha((float) 0.3);
                teletalk.animate().alpha((float) 0.3);
                robi.animate().alpha((float) 0.3);
                robi.animate().alpha(1);
                break;

            case R.id.gp:

            case R.id.gp_text:
                transection_type = "Grameenphone";
                gp.animate().alpha((float) 0.3);
                bl.animate().alpha((float) 0.3);
                airtel.animate().alpha((float) 0.3);
                teletalk.animate().alpha((float) 0.3);
                robi.animate().alpha((float) 0.3);
                gp.animate().alpha(1);
                break;

            case R.id.bl:

            case R.id.bl_text:
                transection_type = "Banglalink";
                gp.animate().alpha((float) 0.3);
                bl.animate().alpha((float) 0.3);
                airtel.animate().alpha((float) 0.3);
                teletalk.animate().alpha((float) 0.3);
                robi.animate().alpha((float) 0.3);
                bl.animate().alpha(1);
                break;

            case R.id.airtel:

            case R.id.airtel_text:
                transection_type = "Airtel";
                gp.animate().alpha((float) 0.3);
                bl.animate().alpha((float) 0.3);
                airtel.animate().alpha((float) 0.3);
                teletalk.animate().alpha((float) 0.3);
                robi.animate().alpha((float) 0.3);
                airtel.animate().alpha(1);
                break;

            case R.id.teletalk:

            case R.id.teletalk_text:
                transection_type = "Teletalk";
                gp.animate().alpha((float) 0.3);
                bl.animate().alpha((float) 0.3);
                airtel.animate().alpha((float) 0.3);
                teletalk.animate().alpha((float) 0.3);
                robi.animate().alpha((float) 0.3);
                teletalk.animate().alpha(1);
                break;

            case R.id.foreignRecharge:

            case R.id.foreignRechargeText:
                Intent recharge = new Intent(getApplicationContext(),ForeignRechargeActivity.class);
                payment_number = phone_number.getText().toString().trim();
                payment_amount = amount.getText().toString().trim();
                recharge.putExtra("Type","Foreign Recharge");
                recharge.putExtra("number",payment_number);
                recharge.putExtra("amount",payment_amount);
                startActivity(recharge);
                finish();
                break;

            case R.id.user_payment_confirm:
                payment_number = phone_number.getText().toString().trim();
                payment_amount = amount.getText().toString().trim();

                payment_type = "Mobile Recharge";
                //payment_type
                if(transection_type.equals("")){
                    Toast.makeText(getApplicationContext(),"Please select a operator",Toast.LENGTH_SHORT).show();
                }else if(payment_amount.equals("")){
                    amount.setError("Please fill this field.");
                }else if(payment_number.equals("")){
                    phone_number.setError("Please fill this field");
                }else {
                    payment_progressbar.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(balance_data) > Integer.parseInt(payment_amount)) {

                        Intent confirm = new Intent(getApplicationContext(), UserPaymentConfirmActivity.class);
                        confirm.putExtra("number", payment_number);
                        confirm.putExtra("amount", payment_amount);
                        confirm.putExtra("payment_type", transection_type);
                        confirm.putExtra("transection_type", payment_type);
                        confirm.putExtra("type", type);
                        payment_progressbar.setVisibility(View.INVISIBLE);
                        startActivity(confirm);

                    } else {
                        amount.setError("Insufficient Balance");
                        amount.requestFocus();
                        payment_progressbar.setVisibility(View.INVISIBLE);
                    }
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