//package com.hwl.beta.ui.near.holder;
//
//import androidx.recyclerview.widget.RecyclerView.ViewHolder;
//
//import com.hwl.beta.databinding.NearLikeItemBinding;
//import com.hwl.beta.db.entity.NearCircleLike;
//import com.hwl.beta.ui.user.bean.ImageViewBean;
//
///**
// * Created by Administrator on 2018/4/15.
// */
//
//public class NearCircleLikeViewHolder extends ViewHolder {
//    private NearLikeItemBinding itemBinding;
//
//    public NearCircleLikeViewHolder(NearLikeItemBinding itemBinding) {
//        super(itemBinding.getRoot());
//        this.itemBinding = itemBinding;
//    }
//
//    public void setItemBinding(NearCircleLike like) {
//        this.itemBinding.setImage(new ImageViewBean(like.getLikeUserImage()));
//    }
//
//    public NearLikeItemBinding getItemBinding() {
//        return this.itemBinding;
//    }
//}
