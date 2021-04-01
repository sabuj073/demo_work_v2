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

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    Context context;
    private List<PaymentModel> reportModels;

    public PaymentAdapter(Context context, List<PaymentModel> reportModels) {
        this.context = context;
        this.reportModels = reportModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_item_message,parent,false);
        PaymentAdapter.ViewHolder viewHolder = new PaymentAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.ViewHolder holder, int position) {
        String from_name = reportModels.get(position).getFrom_name();
        String from_email = reportModels.get(position).getFrom_email();
        String from_number = reportModels.get(position).getFrom_number();
        String from_id = reportModels.get(position).getFrom_id();
        String tonumber = reportModels.get(position).getNumber();
        String amount = reportModels.get(position).getAmount();
        String paymenttpe  = reportModels.get(position).getTransection_type();
        String receivertype = reportModels.get(position).getPayment_type();
        String key = reportModels.get(position).getKey();
        String service = reportModels.get(position).getService();

        holder.setView(from_name,from_email,from_number,from_id,tonumber,amount,paymenttpe,receivertype,key,service);
    }


    @Override
    public int getItemCount() {
        return reportModels.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        TextView name,email,number,userid,to_holder,amount_holder,payment_type_holder,receiver_type_holder,service_holder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_holder);
            email = itemView.findViewById(R.id.email_holder);
            number = itemView.findViewById(R.id.number_holder);
            userid = itemView.findViewById(R.id.user_id_holder);
            to_holder = itemView.findViewById(R.id.to_holder);
            amount_holder = itemView.findViewById(R.id.amount_holder);
            payment_type_holder = itemView.findViewById(R.id.payment_type_holder);
            receiver_type_holder = itemView.findViewById(R.id.receiver_type_holder);
            service_holder = itemView.findViewById(R.id.service_holder);

        }

        public void setView(String from_name, String from_email, String from_number, String from_id, String tonumber, String amount, String paymenttpe, String receivertype, String key,String service) {
            name.setText(from_name);
            email.setText(from_email);
            number.setText(from_number);
            userid.setText(from_id);
            to_holder.setText(tonumber);
            amount_holder.setText(amount);
            payment_type_holder.setText(paymenttpe);
            receiver_type_holder.setText(receivertype);
            service_holder.setText(service);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Are you sure?");
                    dialog.setMessage("You are going to delete this message");
                    dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            Query applesQuery = ref.child("Payments").child(key);

                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.e("Snap", String.valueOf(dataSnapshot));
                                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                                        userSnapshot.getRef().removeValue();
                                        update_counter();
                                        Toast.makeText(itemView.getContext(),"Message Deleted Successfully",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(context,AdminMessagesActivity.class);
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
        FirebaseDatabase.getInstance().getReference().child("Counter").child("Message")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Integer count =  Integer.parseInt(snapshot.getValue().toString());
                        count--;
                        FirebaseDatabase.getInstance().getReference().child("Counter").child("Message")
                                .setValue(count.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
