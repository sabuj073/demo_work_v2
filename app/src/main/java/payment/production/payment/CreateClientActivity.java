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
    import android.util.Patterns;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.ProgressBar;
    import android.widget.Toast;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.android.material.textfield.TextInputEditText;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.Query;
    import com.google.firebase.database.ValueEventListener;

    import java.util.Map;

    public class CreateClientActivity extends AppCompatActivity implements View.OnClickListener {

        TextInputEditText uname,uid,phone,balance,email,password;
        ProgressBar progressBar;
        Button confirm_btn;
        SharedPreferences sharedpreferences;
        ImageView logo;

        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_client);
            getSupportActionBar().hide();

            logo = findViewById(R.id.logo);
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leave_page();
                }
            });

            mAuth = FirebaseAuth.getInstance();
            progressBar = findViewById(R.id.create_client_progress_bar);
            uname = findViewById(R.id.create_client_user_name_text);
            uid = findViewById(R.id.create_client_uid_text);
            phone = findViewById(R.id.create_phone_number_text);
            balance = findViewById(R.id.create_balance_text);
            email = findViewById(R.id.create_email_text);
            password = findViewById(R.id.create_password_text);
            confirm_btn = findViewById(R.id.create_client_confirm_btn);
            confirm_btn.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.create_client_confirm_btn:
                    createClient();
                    break;
            }
        }

        private void createClient() {
            String client_name = uname.getText().toString().trim();
            String client_id = uid.getText().toString().trim();
            String client_phone = phone.getText().toString().trim();
            String client_balance = balance.getText().toString().trim();
            String client_email = email.getText().toString().trim();
            String client_password = password.getText().toString().trim();

            if(client_name.isEmpty()){
                uname.setError("Name is required");
                uname.requestFocus();
            }else if(client_id.isEmpty()){
                uid.setError("User Id is required");
                uid.requestFocus();
            }else if(client_balance.isEmpty()){
                balance.setError("Balance is required");
                balance.requestFocus();
            }else if(client_password.isEmpty()){
                password.setError("Password is required");
                password.requestFocus();
            }else if(client_password.length() < 6){
                password.setError("Min password length should be 6 characters ");
                password.requestFocus();
            }else {

                progressBar.setVisibility(View.VISIBLE);
                User user = new User(client_name, client_id, client_phone, client_balance, client_email,"user",client_password);
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(encodeUserEmail(client_phone))
                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Client has been created successfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(getApplicationContext(),Admin_Interface_Activity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed To Register Try Again", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
            }


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


    }