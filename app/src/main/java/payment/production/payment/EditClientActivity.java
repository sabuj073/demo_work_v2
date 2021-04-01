    package payment.production.payment;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;

    import android.content.DialogInterface;
    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.util.Patterns;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.ProgressBar;
    import android.widget.Toast;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.android.material.textfield.TextInputEditText;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.FirebaseDatabase;

    public class EditClientActivity extends AppCompatActivity implements View.OnClickListener {

        String name_text, email_text, number_text, uid_text, balance_text,password;
        ProgressBar progressBar;
        Button confirm_btn;
        ImageView logo;

        private FirebaseAuth mAuth;
        TextInputEditText create_client_user_name_text,create_client_uid_text,create_phone_number_text,create_balance_text,create_email_text,create_password_text;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_client);
            getSupportActionBar().hide();
            mAuth = FirebaseAuth.getInstance();
            progressBar = findViewById(R.id.create_client_progress_bar);

            logo = findViewById(R.id.logo);
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leave_page();
                }
            });

            create_client_user_name_text  = findViewById(R.id.create_client_user_name_text);
             create_client_uid_text = findViewById(R.id.create_client_uid_text);
            create_phone_number_text = findViewById(R.id.create_phone_number_text);
            create_balance_text  = findViewById(R.id.create_balance_text);
            create_email_text = findViewById(R.id.create_email_text);
            confirm_btn = findViewById(R.id.create_client_confirm_btn);
            create_password_text = findViewById(R.id.create_password_text);
            confirm_btn.setOnClickListener(this);

            Bundle data = getIntent().getExtras();
            if(data!=null)
            {
                name_text =data.getString("uname");
                email_text =data.getString("email");
                number_text =data.getString("number");
                uid_text =data.getString("uid");
                balance_text =data.getString("balance");
                password =data.getString("password");
                Log.e("Data", name_text);
            }
            create_client_user_name_text.setText(name_text);
            create_client_uid_text.setText(uid_text);
            create_phone_number_text.setText(number_text);
            create_balance_text.setText(balance_text);
            create_email_text.setText(email_text);

        }

        private void updateClient() {
            String client_name = create_client_user_name_text.getText().toString().trim();
            String client_id = create_client_uid_text.getText().toString().trim();
            String client_phone = create_phone_number_text.getText().toString().trim();
            String client_balance = create_balance_text.getText().toString().trim();
            String client_email = create_email_text.getText().toString().trim();
            String client_pass = create_password_text.getText().toString().trim();

            if(client_name.isEmpty()){
                create_client_user_name_text.setError("Name is required");
                create_client_user_name_text.requestFocus();
            }else if(client_id.isEmpty()){
                create_client_uid_text.setError("User Id is required");
                create_client_uid_text.requestFocus();
            }else if(client_balance.isEmpty()){
                create_balance_text.setError("Balance is required");
                create_balance_text.requestFocus();
            }else {

                progressBar.setVisibility(View.VISIBLE);
                if(client_pass.equals("")){
                    client_pass = password;
                }
                User user = new User(client_name, client_id, client_phone, client_balance, client_email,"user",client_pass);
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(encodeUserEmail(client_phone))
                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Client has been Updated successfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(getApplicationContext(),ClientListActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed To Update Try Again", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

            }


        }

        static String encodeUserEmail(String userEmail) {
            return userEmail.replace(".", ",");
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.create_client_confirm_btn:
                    updateClient();
                    break;
            }
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