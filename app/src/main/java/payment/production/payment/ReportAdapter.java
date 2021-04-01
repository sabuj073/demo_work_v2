package payment.production.payment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    Context context;
    private List<ReportModel> reportModels;

    public ReportAdapter(Context context, List<ReportModel> reportModels) {
        this.context = context;
        this.reportModels = reportModels;
    }

    @NonNull
    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.ViewHolder holder, int position) {
        String name_text = reportModels.get(position).getName();
        String email_text = reportModels.get(position).getEmail();
        String number_text = reportModels.get(position).getNumber();
        String uid_text = reportModels.get(position).getUserid();
        String report_text = reportModels.get(position).getText();
        String key = reportModels.get(position).getKey();

        holder.setView(name_text,email_text,number_text,uid_text,report_text,key);

    }

    @Override
    public int getItemCount() {
        return reportModels.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        TextView name,email,number,userid,report;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_holder);
            email = itemView.findViewById(R.id.email_holder);
            number = itemView.findViewById(R.id.number_holder);
            userid = itemView.findViewById(R.id.user_id_holder);
            report = itemView.findViewById(R.id.report_holder);

        }

        public void setView(String name_text, String email_text, String number_text, String uid_text, String report_text, String key) {
            name.setText(name_text);
            email.setText(email_text);
            number.setText(number_text);
            userid.setText(uid_text);
            report.setText(report_text);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Are you sure?");
                    dialog.setMessage("You are going to delete this report");
                    dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            Query applesQuery = ref.child("Reports").child(key);

                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.e("Snap", String.valueOf(dataSnapshot));
                                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                                        userSnapshot.getRef().removeValue();
                                        update_counter();
                                        Toast.makeText(context,"Report Deleted Successfully",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context,AdminReportActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        context.startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                    dialog.setNegativeButton("Disimiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                    return false;
                }
            });
        }
    }

    public void update_counter(){
        FirebaseDatabase.getInstance().getReference().child("Counter").child("Report")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Integer count =  Integer.parseInt(snapshot.getValue().toString());
                        count--;
                        FirebaseDatabase.getInstance().getReference().child("Counter").child("Report")
                                .setValue(count.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}
