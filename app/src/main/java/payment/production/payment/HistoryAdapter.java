package payment.production.payment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    Context context;
    private List<PaymentModel> reportModels;

    public HistoryAdapter(Context context, List<PaymentModel> reportModels) {
        this.context = context;
        this.reportModels = reportModels;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_item_history,parent,false);
        HistoryAdapter.ViewHolder viewHolder = new HistoryAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        String from_number = reportModels.get(position).getFrom_number();
        if(from_number.equals("")){
            holder.number.setText(reportModels.get(position).getNumber());
            holder.amount_holder.setText(String.format("-%s", reportModels.get(position).getAmount()));
        }else{
            holder.number.setText(reportModels.get(position).getFrom_number());
            holder.amount_holder.setText(String.format("+%s", reportModels.get(position).getAmount()));
        }


        holder.payment_type_holder.setText(reportModels.get(position).getTransection_type());
        holder.receiver_type_holder.setText(reportModels.get(position).getPayment_type());

    }

    @Override
    public int getItemCount() {
        return reportModels.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        TextView number,amount_holder,payment_type_holder,receiver_type_holder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number_holder);
            amount_holder = itemView.findViewById(R.id.amount_holder);
            payment_type_holder = itemView.findViewById(R.id.payment_type_holder);
            receiver_type_holder = itemView.findViewById(R.id.receiver_type_holder);

        }
    }
}
