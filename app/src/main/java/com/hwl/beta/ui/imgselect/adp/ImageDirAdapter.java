package com.hwl.beta.ui.imgselect.adp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hwl.beta.R;
import com.hwl.beta.ui.imgselect.bean.ImageDirBean;

import java.util.ArrayList;
import java.util.List;


public class ImageDirAdapter extends BaseAdapter {

    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<ImageDirBean> imageFolders;
    private int lastSelected = 0;

    public ImageDirAdapter(Activity activity, List<ImageDirBean> folders) {
        mActivity = activity;
        if (folders != null && folders.size() > 0) imageFolders = folders;
        else imageFolders = new ArrayList<>();
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refreshData(List<ImageDirBean> folders) {
        if (folders != null && folders.size() > 0) imageFolders = folders;
        else imageFolders.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageFolders.size();
    }

    @Override
    public ImageDirBean getItem(int position) {
        return imageFolders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.imgselect_dir_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageDirBean dirBean = getItem(position);
        holder.tvDirName.setText(dirBean.getName());
        holder.tvImageCount.setText(dirBean.getImages().size() + "张");
        Glide.with(mActivity).load(dirBean.getFirstImage().getPath())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.ivDirImage);

        if (lastSelected == position) {
            holder.ivDirSelect.setVisibility(View.VISIBLE);
        } else {
            holder.ivDirSelect.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public void setSelectIndex(int position) {
        if (lastSelected == position) {
            return;
        }
        lastSelected = position;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return lastSelected;
    }

    private class ViewHolder {
        ImageView ivDirImage, ivDirSelect;
        TextView tvDirName, tvImageCount;

        public ViewHolder(View view) {
            ivDirImage = view.findViewById(R.id.iv_dir_image);
            tvDirName = view.findViewById(R.id.tv_dir_name);
            tvImageCount = view.findViewById(R.id.tv_image_count);
            ivDirSelect = view.findViewById(R.id.iv_dir_select);
        }
    }
}


//public class ImageDirAdapter extends RecyclerView.Adapter<ImageDirAdapter.ImageDirViewHolder> {
//
//    final Context context;
//    String currDirName;
//    List<ImageDirBean> imageDirList;
//    IImageDirItemEvent dirClickListener;
//
//    public ImageDirAdapter(Context context, List<ImageDirBean> imageDirList, String
// currDirName, IImageDirItemEvent dirClickListener) {
//        this.context = context;
//        this.imageDirList = imageDirList;
//        this.dirClickListener = dirClickListener;
//        this.currDirName = currDirName;
//    }
//
//    @Override
//    public ImageDirViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.image_dir_item, parent, false);
//        return new ImageDirViewHolder(view, dirClickListener);
//    }
//
//    @Override
//    public void onBindViewHolder(ImageDirViewHolder holder, int position) {
//        ImageDirBean dirBean = imageDirList.get(position);
//        Glide.with(context).load(dirBean.getImagePath()).into(holder.ivImageDir);
//        holder.tvDirName.setText(dirBean.getImageName());
//        holder.tvImageCount.setText(dirBean.getImageCount() + "张");
//        if (dirBean.getDir().equals(this.currDirName)) {
//            holder.ivImageSelect.setVisibility(View.VISIBLE);
//        } else {
//            holder.ivImageSelect.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return imageDirList.size();
//    }
//
//    class ImageDirViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView ivImageDir, ivImageSelect;
//        TextView tvDirName, tvImageCount;
//        IImageDirItemEvent dirClickListener;
//
//        public ImageDirViewHolder(View itemView, final IImageDirItemEvent dirClickListener) {
//            super(itemView);
//
//            ivImageDir = itemView.findViewById(R.id.iv_image_dir);
//            tvDirName = itemView.findViewById(R.id.tv_dir_name);
//            tvImageCount = itemView.findViewById(R.id.tv_image_count);
//            ivImageSelect = itemView.findViewById(R.id.iv_image_select);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (dirClickListener != null) {
//                        dirClickListener.onImageDirItemListener(v, getAdapterPosition());
//                    }
//                }
//            });
//        }
//    }
//
//    public interface IImageDirItemEvent {
//        void onImageDirItemListener(View view, int position);
//    }
//}
