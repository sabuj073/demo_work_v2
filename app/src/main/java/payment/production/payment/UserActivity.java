    package payment.production.payment;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.app.NotificationCompat;

    import android.app.NotificationChannel;
    import android.app.NotificationManager;
    import android.app.PendingIntent;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Build;
    import android.os.Bundle;
    import android.os.Vibrator;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.ProgressBar;
    import android.widget.TextView;

    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.Query;
    import com.google.firebase.database.ValueEventListener;

    public class UserActivity extends AppCompatActivity implements View.OnClickListener {
        ImageView recharge_icon_user,bkash_icon_user,rocket_icon_user,nagad_icon_user,uchas_icon_user,mcash_icon_user,report_icon_user,tr_history_icon;
        TextView username,userid,userbalance,availalable_balance,usernotificationcounter;
        String name,id,balance,email_text,number_text;
        ImageView logout,userpartnotification;
        SharedPreferences sharedpreferences;
        ProgressBar progressBar;
        ImageView logo;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user);
            getSupportActionBar().hide();

            username = findViewById(R.id.user_name);
            userid = findViewById(R.id.user_id);
            userbalance  = findViewById(R.id.user_balance);
            sharedpreferences = getSharedPreferences("payment.production.payment", Context.MODE_PRIVATE);
            name = sharedpreferences.getString("uname","");
            id= sharedpreferences.getString("uid","");
            balance = sharedpreferences.getString("balance","");
            email_text = sharedpreferences.getString("email","");
            number_text = sharedpreferences.getString("phone","");

            username.setText(name);
            userid.setText(id);
            userbalance.setText("\u09F3 "+balance);

            recharge_icon_user = findViewById(R.id.recharge_icon_user);
            bkash_icon_user = findViewById(R.id.bkash_icon_user);
            rocket_icon_user = findViewById(R.id.rocket_icon_user);
            nagad_icon_user = findViewById(R.id.nagad_icon_user);
            uchas_icon_user = findViewById(R.id.uchas_icon_user);
            mcash_icon_user = findViewById(R.id.mcash_icon_user);
            logout = findViewById(R.id.logout_btn);
            report_icon_user = findViewById(R.id.report_icon_user);
            tr_history_icon = findViewById(R.id.tr_history_icon);
            availalable_balance = findViewById(R.id.textView7);
            progressBar = findViewById(R.id.user_activity_progressbar);
            userpartnotification = findViewById(R.id.userpartnotification);
            usernotificationcounter = findViewById(R.id.usernotificationcounter);

            checkNotifications(number_text);

            recharge_icon_user.setOnClickListener(this);
            bkash_icon_user.setOnClickListener(this);
            rocket_icon_user.setOnClickListener(this);
            nagad_icon_user.setOnClickListener(this);
            uchas_icon_user.setOnClickListener(this);
            mcash_icon_user.setOnClickListener(this);
            logout.setOnClickListener(this);
            report_icon_user.setOnClickListener(this);
            tr_history_icon.setOnClickListener(this);
            userbalance.setOnClickListener(this);
            availalable_balance.setOnClickListener(this);
            username.setOnClickListener(this);
            userid.setOnClickListener(this);
            userpartnotification.setOnClickListener(this);
            updateBalance();

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
                case R.id.recharge_icon_user:
                    Intent recharge = new Intent(getApplicationContext(),UserRechargeActivity.class);
                    recharge.putExtra("Type","Mobile Recharge");
                    startActivity(recharge);
                    break;

                case R.id.bkash_icon_user:
                    Intent bkash = new Intent(getApplicationContext(),UserPaymentActivity.class);
                    bkash.putExtra("Type","Bkash");
                    startActivity(bkash);
                    break;

                case R.id.userpartnotification:
                    Intent notification = new Intent(getApplicationContext(),UserSectionNotificationMainActivity.class);
                    startActivity(notification);
                    break;

                case R.id.rocket_icon_user:
                    Intent rocket = new Intent(getApplicationContext(),UserPaymentActivity.class);
                    rocket.putExtra("Type","Rocket");
                    startActivity(rocket);
                    break;

                case R.id.nagad_icon_user:
                    Intent nagad = new Intent(getApplicationContext(),UserPaymentActivity.class);
                    nagad.putExtra("Type","Nagad");
                    startActivity(nagad);
                    break;

                case R.id.uchas_icon_user:
                    Intent ucash = new Intent(getApplicationContext(),UserPaymentActivity.class);
                    ucash.putExtra("Type","Ucash");
                    startActivity(ucash);
                    break;

                case R.id.mcash_icon_user:
                    Intent mcash = new Intent(getApplicationContext(),UserPaymentActivity.class);
                    mcash.putExtra("Type","Mcash");
                    startActivity(mcash);
                    break;

                case R.id.report_icon_user:
                    Intent report = new Intent(getApplicationContext(),ReportActivity.class);
                    startActivity(report);
                    break;

                case R.id.tr_history_icon:
                    Intent tr_history = new Intent(getApplicationContext(),HistoryActivity.class);
                    tr_history.putExtra("email",email_text);
                    tr_history.putExtra("number",number_text);
                    startActivity(tr_history);
                    break;

                case R.id.user_balance:

                case R.id.textView7:
                    updateBalance();
                    break;

                case R.id.user_name:

                case R.id.user_id:
                    Intent edit_intent = new Intent(getApplicationContext(),EditMyselfActivity.class);
                    startActivity(edit_intent);
                    break;


                case R.id.logout_btn:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("Are you sure?");
                    dialog.setMessage("You are goin to logout");
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("uname", "");
                            editor.putString("uid", "");
                            editor.putString("balance","");
                            editor.putString("phone", "");
                            editor.putString("type", "");
                            editor.putString("email", "");
                            editor.commit();
                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
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

                    break;

            }
        }

        public void updateBalance(){
            String name = sharedpreferences.getString("uname","");
            String email_string = sharedpreferences.getString("email","");
            String type = sharedpreferences.getString("type","");
            String email_encode = encodeUserEmail(number_text);
                progressBar.setVisibility(View.VISIBLE);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                Query checkUser = reference.orderByChild("phone").equalTo(number_text);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String balance = snapshot.child(email_encode).child("balance").getValue(String.class);
                        String uname = snapshot.child(email_encode).child("uname").getValue(String.class);
                        String uid = snapshot.child(email_encode).child("uid").getValue(String.class);
                        String phone = snapshot.child(email_encode).child("phone").getValue(String.class);
                        String type = snapshot.child(email_encode).child("type").getValue(String.class);
                        String password = snapshot.child(email_encode).child("password").getValue(String.class);
                        String email = snapshot.child(email_encode).child("email").getValue(String.class);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("uname", uname);
                        editor.putString("uid", uid);
                        editor.putString("balance", balance);
                        editor.putString("phone", phone);
                        editor.putString("type", type);
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.commit();
                        userbalance.setText("\u09F3 "+balance);
                        progressBar.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.INVISIBLE);
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

        private void checkNotifications(String number)
        {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild("NotificationCounter")) {
                        FirebaseDatabase.getInstance().getReference().child("NotificationCounter").child(removespecial(number))
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Integer count = 0;
                                        if(snapshot.getValue()==null){
                                            count = 1;
                                            usernotificationcounter.setVisibility(View.INVISIBLE);
                                        }else {
                                            count = Integer.parseInt(snapshot.getValue().toString());
                                            if(count>0) {
                                                try {
                                                    usernotificationcounter.setVisibility(View.VISIBLE);
                                                    usernotificationcounter.setText(count.toString());
                                                    show_notification("New Notification","You have a new notification from admin");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
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

        private void createNotificationChannel() {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = getString(R.string.channel_name);
                String description = getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("useractivity", name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }

        public  void  show_notification(String title,String message){
            createNotificationChannel();
            Intent intent = new Intent(this, UserActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(this, "sabuj")
                    .setSmallIcon(R.drawable.unnamed)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000})
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);


            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0,mbuilder.build());
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
        }
    }