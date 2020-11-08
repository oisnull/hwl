package com.hwl.beta.ui.circle.adp;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwl.beta.R;
import com.hwl.beta.databinding.CircleMessageItemBinding;
import com.hwl.beta.db.entity.CircleMessage;
import com.hwl.beta.ui.circle.holder.CircleMessageItemViewHolder;

import java.util.List;

public class CircleMessageAdapter extends RecyclerView.Adapter<CircleMessageItemViewHolder> {
    private List<CircleMessage> messages;
    private IMessageItemListener itemListener;
    private LayoutInflater inflater;

    public CircleMessageAdapter(Context context, List<CircleMessage> messages, IMessageItemListener itemListener) {
        this.messages = messages;
        this.itemListener = itemListener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CircleMessageItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CircleMessageItemViewHolder((CircleMessageItemBinding) DataBindingUtil.inflate(inflater, R.layout.circle_message_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CircleMessageItemViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final CircleMessage message= messages.get(position);
        holder.setItemBinding(message);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onItemClick(v,message,position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemListener.onItemLongClick(v,message,position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public interface IMessageItemListener{
        void onItemClick(View v, CircleMessage message, int position);

        void onItemLongClick(View v, CircleMessage message, int position);
    }
}
