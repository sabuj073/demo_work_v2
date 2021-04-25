package payment.production.payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Admin_Interface_Activity extends AppCompatActivity implements View.OnClickListener {
    ImageView admin_create_client,admin_client_list,admin_messages,admin_report,logout;
    SharedPreferences sharedpreferences;
    ImageView logo,sendnotify;
    String type;
    TextView message_counter,report_counter;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__interface_);
        getSupportActionBar().hide();
        sharedpreferences = getSharedPreferences("payment.production.payment", Context.MODE_PRIVATE);
        type = sharedpreferences.getString("type","");



        admin_create_client = findViewById(R.id.admin_create_client);
        admin_client_list = findViewById(R.id.admin_client_list);
        admin_messages = findViewById(R.id.admin_messages);
        admin_report = findViewById(R.id.admin_report);
        logout = findViewById(R.id.admin_logout);
        logo = findViewById(R.id.logo);
        message_counter = findViewById(R.id.message_counter);
        report_counter = findViewById(R.id.report_counter);
        progressBar = findViewById(R.id.admin_progressbar);
        sendnotify = findViewById(R.id.send_notify);

        admin_create_client.setOnClickListener(this);
        admin_client_list.setOnClickListener(this);
        admin_messages.setOnClickListener(this);
        admin_report.setOnClickListener(this);
        logout.setOnClickListener(this);
        logo.setOnClickListener(this);
        sendnotify.setOnClickListener(this);
        update_value();
    }

    private void update_value() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().child("Counter").child("Report")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Integer data = Integer.parseInt(snapshot.getValue().toString());
                        if(data>0) {
                            show_notification("Incoming Report","New Report From Client");
                            if (data <= 5) {
                                report_counter.setText(data.toString());
                            } else {
                                report_counter.setText("5+");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        FirebaseDatabase.getInstance().getReference().child("Counter").child("Message")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Integer data = Integer.parseInt(snapshot.getValue().toString());
                        if(data>0) {
                            show_notification("Incoming Message","New Message From Client");
                            if (data <= 5) {
                                message_counter.setText(data.toString());
                            } else {
                                message_counter.setText("5+");
                            }

                        }
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_notify:
                Intent send_notify = new Intent(getApplicationContext(),SendNotificationActivity.class);
                send_notify.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(send_notify);
                break;
            case R.id.admin_create_client:
                Intent create_client = new Intent(getApplicationContext(),CreateClientActivity.class);
                startActivity(create_client);
                break;

            case R.id.admin_report:
                Intent report_intent = new Intent(getApplicationContext(),AdminReportActivity.class);
                startActivity(report_intent);
                finish();
                break;

            case R.id.admin_messages:
                Intent message_intent = new Intent(getApplicationContext(),AdminMessagesActivity.class);
                message_intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(message_intent);
                break;

            case R.id.admin_client_list:
                Intent client_list = new Intent(getApplicationContext(),ClientListActivity.class);
                startActivity(client_list);
                break;

            case R.id.logo:
                leave_page();
                break;

            case R.id.admin_logout:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("You are going to logout");
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

    @Override
    protected void onRestart() {
        update_value();
        super.onRestart();
    }

    public  void  show_notification(String title,String message){
        createNotificationChannel();
        Intent intent = new Intent(this, Admin_Interface_Activity.class);
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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("sabuj", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}