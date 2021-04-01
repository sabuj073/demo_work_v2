package payment.production.payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

public class EditMyselfActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedpreferences;
    String name_text, email_text, number_text, uid_text, balance_text,password;
    ProgressBar progressBar;
    Button confirm_btn;
    ImageView logo;
    TextInputEditText create_client_user_name_text,create_client_uid_text,create_phone_number_text,create_balance_text,create_email_text,create_password_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_myself);
        getSupportActionBar().hide();
        sharedpreferences = getSharedPreferences("payment.production.payment", Context.MODE_PRIVATE);
        name_text = sharedpreferences.getString("uname","");
        uid_text= sharedpreferences.getString("uid","");
        balance_text = sharedpreferences.getString("balance","");
        email_text = sharedpreferences.getString("email","");
        number_text = sharedpreferences.getString("phone","");
        password = sharedpreferences.getString("password","");

        progressBar = findViewById(R.id.create_client_progress_bar);
        create_client_user_name_text  = findViewById(R.id.create_client_user_name_text);
        create_client_uid_text = findViewById(R.id.create_client_uid_text);
        create_phone_number_text = findViewById(R.id.create_phone_number_text);
        create_balance_text  = findViewById(R.id.create_balance_text);
        create_email_text = findViewById(R.id.create_email_text);
        confirm_btn = findViewById(R.id.create_client_confirm_btn);
        create_password_text = findViewById(R.id.create_password_text);
        confirm_btn.setOnClickListener(this);

        create_client_user_name_text.setText(name_text);
        create_client_uid_text.setText(uid_text);
        create_phone_number_text.setText(number_text);
        create_email_text.setText(email_text);

        logo = findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leave_page();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_client_confirm_btn:
                updateClient();
                break;
        }
    }

    private void updateClient() {

        String client_email = create_email_text.getText().toString().trim();
        String client_pass = create_password_text.getText().toString().trim();



            progressBar.setVisibility(View.VISIBLE);
            if(client_pass.equals("")){
                client_pass = password;
            }
            if(client_email.equals("")){
                client_email=email_text;
            }
            User user = new User(name_text, uid_text, number_text, balance_text, client_email,"user",client_pass);
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(encodeUserEmail(number_text))
                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Profile Updated successfully", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(getApplicationContext(),UserActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed To Update Try Again", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
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
}