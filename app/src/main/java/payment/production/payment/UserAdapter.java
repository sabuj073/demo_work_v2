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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    private final List<UserModel> userModelList;

    public UserAdapter(Context context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
    }


    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_item_user,parent,false);
        UserAdapter.ViewHolder viewHolder = new UserAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        String name = userModelList.get(position).getUname();
        String email = userModelList.get(position).getEmail();
        String number = userModelList.get(position).getPhone();
        String uid = userModelList.get(position).getUid();
        String balance = userModelList.get(position).getBalance();
        String password = userModelList.get(position).getPassword();

        holder.setView(name,email,number,uid,balance,position,password);
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        TextView name,email,number,userid,balance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_holder);
            email = itemView.findViewById(R.id.email_holder);
            number = itemView.findViewById(R.id.number_holder);
            userid = itemView.findViewById(R.id.user_id_holder);
            balance = itemView.findViewById(R.id.balance_holder);
        }

        private void  setView(String name_text,String email_text,String number_text,String uid_text,String balance_text,int position,String password_text){
            name.setText(name_text);
            email.setText(email_text);
            number.setText(number_text);
            userid.setText(uid_text);
            balance.setText(balance_text);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {


                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Are you sure?");
                    dialog.setMessage("Deleting this account will result in completely removing this account" +
                            " from the system and he/she won't be able to access the app");
                    dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            Query applesQuery = ref.child("Users").orderByChild("phone").equalTo(number_text);

                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                                        userSnapshot.getRef().removeValue();
                                        Toast.makeText(context,"User Deleted Successfully",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(itemView.getContext(),ClientListActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        context.startActivity(intent);
                                        ((Activity)context).finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                    dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();




                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(itemView.getContext(),EditClientActivity.class);
                        intent.putExtra("uname",name_text);
                        intent.putExtra("uid",uid_text);
                        intent.putExtra("email",email_text);
                        intent.putExtra("number",number_text);
                        intent.putExtra("email",email_text);
                        intent.putExtra("balance",balance_text);
                        intent.putExtra("password",password_text);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }
            });
        }
    }
}
