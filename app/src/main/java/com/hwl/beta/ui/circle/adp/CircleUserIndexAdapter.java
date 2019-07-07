package com.hwl.beta.ui.circle.adp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwl.beta.R;
import com.hwl.beta.databinding.CircleUserHeadItemBinding;
import com.hwl.beta.databinding.CircleUserIndexItemBinding;
import com.hwl.beta.databinding.CircleUserItemNullBinding;
import com.hwl.beta.db.DBConstant;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.ui.circle.action.ICircleUserItemListener;
import com.hwl.beta.ui.circle.holder.CircleUserHeadItemViewHolder;
import com.hwl.beta.ui.circle.holder.CircleUserIndexItemViewHolder;
import com.hwl.beta.ui.circle.holder.CircleUserItemNullViewHolder;
import com.hwl.beta.ui.user.bean.ImageViewBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CircleUserIndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Circle> circles;
    private ICircleUserItemListener itemListener;
    private LayoutInflater inflater;
    private int prevYear;
    private int prevMonth;
    private int prevDay;
    private String prevShowDate;

    public CircleUserIndexAdapter(Context context, ICircleUserItemListener itemListener) {
        this.context = context;
        this.circles = new ArrayList<>();
        this.itemListener = itemListener;
        inflater = LayoutInflater.from(context);
        prevYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    public void addInfos(List<Circle> infos) {
        if (infos == null || infos.size() <= 0) return;

        this.circles.addAll(infos);
        notifyDataSetChanged();
    }

    public void updateInfos(List<Circle> infos) {
        if (infos == null || infos.size() <= 0) return;

        if (getItemCount() <= 0) {
            circles.addAll(infos);
        } else {
            removeEmptyInfo();
            circles.removeAll(infos);
            circles.addAll(infos);
        }
        sortInfos(circles);
        notifyDataSetChanged();
    }

    private void sortInfos(List<Circle> infos) {
        if (infos == null || infos.size() <= 0) return;
        Collections.sort(infos, new Comparator<Circle>() {
            public int compare(Circle arg0, Circle arg1) {
                if (arg0.getCircleId() == 0 || arg1.getCircleId() == 0) return 0;
                return (int) (arg1.getCircleId() - arg0.getCircleId());
            }
        });
    }

    private void removeEmptyInfo() {
        for (int i = 0; i < circles.size(); i++) {
            if (circles.get(i).getItemType() == DBConstant.CIRCLE_ITEM_NULL) {
                circles.remove(i);
                break;
            }
        }
    }

    public void setEmptyInfo() {
        removeEmptyInfo();
        circles.add(getCircleItemPosition(), new Circle(DBConstant.CIRCLE_ITEM_NULL));
        notifyDataSetChanged();
    }

    private int getCircleItemPosition() {
        for (int i = 0; i < circles.size(); i++) {
            if (circles.get(i).getCircleId() > 0)
                return i;
        }
        return circles.size();
    }

    public void updateHead(Friend info) {
        if (info == null) return;
        if (circles.get(0).getItemType() != DBConstant.CIRCLE_ITEM_HEAD) return;
        boolean isChanged = false;
        if (circles.get(0).getCircleBackImage() != info.getCircleBackImage()) {
            circles.get(0).setCircleBackImage(info.getCircleBackImage());
            isChanged = true;
        }
        if (circles.get(0).getLifeNotes() != info.getLifeNotes()) {
            circles.get(0).setLifeNotes(info.getLifeNotes());
            isChanged = true;
        }

        if (isChanged)
            notifyItemChanged(0);
    }

    public long getMinId() {
        if (getItemCount() <= 0) {
            return 0;
        }
        return this.circles.get(getItemCount() - 1).getCircleId();
    }

    public List<Circle> getInfos() {
        return this.circles;
    }

    @Override
    public int getItemViewType(int position) {
        switch (circles.get(position).getItemType()) {
            case DBConstant.CIRCLE_ITEM_NULL:
            default:
                return 0;
            case DBConstant.CIRCLE_ITEM_HEAD:
                return 1;
            case DBConstant.CIRCLE_ITEM_DATA:
                return 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
            default:
                return new CircleUserItemNullViewHolder((CircleUserItemNullBinding) DataBindingUtil.inflate(inflater, R.layout.circle_user_item_null, parent, false));
            case 1:
                return new CircleUserHeadItemViewHolder((CircleUserHeadItemBinding) DataBindingUtil.inflate(inflater, R.layout.circle_user_head_item, parent, false));
            case 2:
                return new CircleUserIndexItemViewHolder(context,
                        (CircleUserIndexItemBinding) DataBindingUtil.inflate(inflater,
                                R.layout.circle_user_index_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Circle info = circles.get(position);
        if (holder instanceof CircleUserItemNullViewHolder) {
            CircleUserItemNullViewHolder viewHolder = (CircleUserItemNullViewHolder) holder;
            viewHolder.setItemBinding(itemListener,
                    (Calendar.getInstance().get(Calendar.MONTH) + 1) + "月",
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "");
        } else if (holder instanceof CircleUserHeadItemViewHolder) {
            CircleUserHeadItemViewHolder viewHolder = (CircleUserHeadItemViewHolder) holder;
            viewHolder.setItemBinding(itemListener, new ImageViewBean(info.getPublishUserImage(),
                    info.getCircleBackImage()), info.getPublishUserName(), info.getLifeNotes());
            prevShowDate = "";
            prevMonth = 0;
            prevDay = 0;
        } else if (holder instanceof CircleUserIndexItemViewHolder) {
            CircleUserIndexItemViewHolder viewHolder = (CircleUserIndexItemViewHolder) holder;
            String timeYear = "";
            String timeMonth = "";
            String timeDay = "";
            if (info.getPublishTime() != null && !info.getShowDate().equals(prevShowDate)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(info.getPublishTime());
                if (calendar.get(Calendar.YEAR) != prevYear) {
                    timeYear = calendar.get(Calendar.YEAR) + "年";
                }
                if ((calendar.get(Calendar.MONTH) + 1) != prevMonth || calendar.get(Calendar.DAY_OF_MONTH) != prevDay) {
                    timeMonth = (calendar.get(Calendar.MONTH) + 1) + "月";
                    timeDay = calendar.get(Calendar.DAY_OF_MONTH) + "";
                }

                prevShowDate = info.getShowDate();
                Calendar prev = Calendar.getInstance();
                prev.setTime(info.getPublishTime());
                prevYear = prev.get(Calendar.YEAR);
                prevMonth = prev.get(Calendar.MONTH) + 1;
                prevDay = prev.get(Calendar.DAY_OF_MONTH);

                if (position != 1 || timeMonth.equals("")) {
                    RecyclerView.LayoutParams params =
                            (RecyclerView.LayoutParams) viewHolder.getItemBinding().llCircleContainer.getLayoutParams();
                    params.topMargin = 1;
                    viewHolder.getItemBinding().llCircleContainer.setLayoutParams(params);
                }
            }
            viewHolder.setItemBinding(timeYear, timeMonth, timeDay, info, itemListener);
        }
    }

    @Override
    public int getItemCount() {
        return circles.size();
    }
}
