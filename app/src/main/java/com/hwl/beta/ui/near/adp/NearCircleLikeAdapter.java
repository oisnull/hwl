//package com.hwl.beta.ui.near.adp;
//
//import android.content.Context;
//import android.databinding.DataBindingUtil;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.hwl.beta.R;
//import com.hwl.beta.databinding.NearLikeItemBinding;
//import com.hwl.beta.db.entity.NearCircleLike;
//import com.hwl.beta.ui.near.holder.NearCircleLikeViewHolder;
//
//import java.util.List;
//
///**
// * Created by Administrator on 2018/4/15.
// */
//
//public class NearCircleLikeAdapter extends RecyclerView.Adapter<NearCircleLikeViewHolder> {
//
//    LayoutInflater inflater;
//    List<NearCircleLike> likes;
//    ILikeItemListener itemListener;
//
//    public NearCircleLikeAdapter(Context context, List<NearCircleLike> likes, ILikeItemListener itemListener) {
//        this.likes = likes;
//        inflater = LayoutInflater.from(context);
//        this.itemListener = itemListener;
//    }
//
//    @Override
//    public NearCircleLikeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new NearCircleLikeViewHolder((NearLikeItemBinding) DataBindingUtil.inflate(inflater, R.layout.near_like_item, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(NearCircleLikeViewHolder holder, int position) {
//        final NearCircleLike like = likes.get(position);
//        holder.setItemBinding(like);
//        holder.getItemBinding().getRoot().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                itemListener.onLikeUserClick(like);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return likes.size();
//    }
//
//    public interface ILikeItemListener {
//        void onLikeUserClick(NearCircleLike like);
//    }
//}
