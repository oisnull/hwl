//package com.hwl.beta.ui.near.adp;
//
//import android.content.Context;
//import android.databinding.DataBindingUtil;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.hwl.beta.R;
//import com.hwl.beta.databinding.NearMessageItemBinding;
//import com.hwl.beta.db.entity.NearCircleMessage;
//import com.hwl.beta.ui.near.holder.NearMessageItemViewHolder;
//
//import java.util.List;
//
//public class NearMessageAdapter extends RecyclerView.Adapter<NearMessageItemViewHolder> {
//    private List<NearCircleMessage> messages;
//    private INearMessageItemListener itemListener;
//    private LayoutInflater inflater;
//
//    public NearMessageAdapter(Context context, List<NearCircleMessage> messages, INearMessageItemListener itemListener) {
//        this.messages = messages;
//        this.itemListener = itemListener;
//        inflater = LayoutInflater.from(context);
//    }
//
//    @NonNull
//    @Override
//    public NearMessageItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new NearMessageItemViewHolder((NearMessageItemBinding) DataBindingUtil.inflate(inflater, R.layout.near_message_item, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull NearMessageItemViewHolder holder, final int position) {
//        final NearCircleMessage message= messages.get(position);
//        holder.setItemBinding(message);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                itemListener.onItemClick(v,message,position);
//            }
//        });
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                itemListener.onItemLongClick(v,message,position);
//                return true;
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return messages.size();
//    }
//
//    public interface INearMessageItemListener{
//        void onItemClick(View v, NearCircleMessage message, int position);
//
//        void onItemLongClick(View v, NearCircleMessage message, int position);
//    }
//}
