package payment.production.payment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ForeignRechargeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    TextView textView,balance;
    SharedPreferences sharedpreferences;

    TextInputEditText phone_number,amount,payment_phone_operator,payment_amount_operator,user_payment_type_text;

    RadioGroup user_payment_send_money_cashout,user_payment_personal_agent;
    String payment_number,payment_amount,transection_type,payment_type,balance_data,client_email,number_string,name,user_id;
    ImageView gp,bl,airtel,teletalk,robi,foreignRecharge;
    TextView gp_text,bl_text,airtel_text,teletalk_text,robi_text,foreignRechargeText;
    Button confirm;
    ProgressBar payment_progressbar;
    Spinner spinner;
    private FirebaseAuth mAuth;
    String type,countryname,countrycode,operator,recharge_type;
    ImageView logo;

    List<Country> countryList;
    List<String> names;
    int getposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreign_recharge);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        sharedpreferences = getSharedPreferences("payment.production.payment", Context.MODE_PRIVATE);
        balance_data = sharedpreferences.getString("balance","");
        client_email = sharedpreferences.getString("email","");
        number_string = sharedpreferences.getString("phone","");
        name= sharedpreferences.getString("uname","");
        user_id= sharedpreferences.getString("uid","");
        transection_type="";
        spinner = (Spinner)findViewById(R.id.spinner);

        countryList = new ArrayList<>();
        names= new ArrayList<String>();

        textView = findViewById(R.id.textView2);
        balance = findViewById(R.id.user_balance);

        phone_number = findViewById(R.id.payment_phone);
        amount = findViewById(R.id.payment_amount);
        confirm = findViewById(R.id.user_payment_confirm);
        payment_progressbar = findViewById(R.id.payment_progressbar);
        foreignRecharge = findViewById(R.id.foreignRecharge);
        foreignRechargeText = findViewById(R.id.foreignRechargeText);
        payment_phone_operator = findViewById(R.id.payment_phone_operator);
        payment_amount_operator = findViewById(R.id.payment_amount_operator);
        user_payment_type_text = findViewById(R.id.user_payment_type_text);
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
            type =data.getString("Type");
            textView.setText(type);
            phone_number.setText(data.getString("number"));
            amount.setText(data.getString("amount"));
        }

        try {
            JSONObject jsonObject = new JSONObject(JsonDataFromAsset());
            JSONArray jsonArray = jsonObject.getJSONArray("countries");
            for (int i = 0 ; i<jsonArray.length() ; i++){
                JSONObject countries = jsonArray.getJSONObject(i);
                countryList.add(new Country(countries.getString("name"),countries.getString("code")));
                names.add(countries.getString("name"));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,names);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String JsonDataFromAsset() {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("countries");
            int sizeoffile = inputStream.available();
            byte[] bufferData = new byte[sizeoffile];
            inputStream.read(bufferData);
            inputStream.close();
            json = new String(bufferData,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.user_payment_confirm:
                payment_number = phone_number.getText().toString().trim();
                payment_amount = amount.getText().toString().trim();
                countryname = countryList.get(getposition).getName();
                countrycode = countryList.get(getposition).getCode();
                operator = payment_amount_operator.getText().toString().trim();
                recharge_type = user_payment_type_text.getText().toString().trim();

                payment_type = "Mobile Recharge";
                //payment_type
                if(operator.equals("")){
                    payment_amount_operator.setError("Please provide your operator name");
                }else if (recharge_type.equals("")){
                    user_payment_type_text.setError("Fill this field");
                }else if(payment_amount.equals("")){
                    amount.setError("Please fill this field.");
                }else if(payment_number.equals("")){
                    phone_number.setError("Please fill this field");
                }
                else {
                    payment_progressbar.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(balance_data) > Integer.parseInt(payment_amount)) {

                        Intent confirm = new Intent(getApplicationContext(), UserPaymentConfirmActivity.class);
                        confirm.putExtra("number", countrycode+" "+payment_number);
                        confirm.putExtra("amount", payment_amount);
                        confirm.putExtra("payment_type", operator);
                        confirm.putExtra("transection_type", recharge_type);
                        confirm.putExtra("type", type+", "+countryname);
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        payment_phone_operator.setText(countryList.get(position).getCode());
        getposition = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}