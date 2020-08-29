package com.hwl.beta.ui.circle.adp;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hwl.beta.R;
import com.hwl.beta.databinding.CircleItemNullBinding;
import com.hwl.beta.databinding.CircleUserHeadItemBinding;
import com.hwl.beta.databinding.CircleUserIndexItemBinding;
import com.hwl.beta.databinding.CircleUserItemDefaultBinding;
import com.hwl.beta.db.DBConstant;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.circle.action.ICircleUserItemListener;
import com.hwl.beta.ui.circle.holder.CircleItemNullViewHolder;
import com.hwl.beta.ui.circle.holder.CircleUserHeadItemViewHolder;
import com.hwl.beta.ui.circle.holder.CircleUserIndexItemViewHolder;
import com.hwl.beta.ui.circle.holder.CircleUserItemDefaultViewHolder;
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
    private long currentUserId;
    private int currentYear;

    public CircleUserIndexAdapter(Context context,
                                  ICircleUserItemListener itemListener) {
        this(context, 0, itemListener);
    }

    public CircleUserIndexAdapter(Context context, long currentUserId,
                                  ICircleUserItemListener itemListener) {
        this.context = context;
        this.currentUserId = currentUserId;
        this.circles = new ArrayList<>();
        this.itemListener = itemListener;
        inflater = LayoutInflater.from(context);
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
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
        for (int i = 0; i < circles.size(); i++) {
            android.util.Log.d("CircleUserIndex", circles.get(i).getCircleId() + "");
        }
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
            case DBConstant.CIRCLE_ITEM_DEFAULT:
            default:
                return 0;
            case DBConstant.CIRCLE_ITEM_HEAD:
                return 1;
            case DBConstant.CIRCLE_ITEM_DATA:
                return 2;
            case DBConstant.CIRCLE_ITEM_NULL:
                return 3;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
            default:
                return new CircleUserItemDefaultViewHolder((CircleUserItemDefaultBinding) DataBindingUtil.inflate(inflater, R.layout.circle_user_item_default, parent, false));
            case 1:
                return new CircleUserHeadItemViewHolder((CircleUserHeadItemBinding) DataBindingUtil.inflate(inflater, R.layout.circle_user_head_item, parent, false));
            case 2:
                return new CircleUserIndexItemViewHolder(context,
                        (CircleUserIndexItemBinding) DataBindingUtil.inflate(inflater,
                                R.layout.circle_user_index_item, parent, false));
            case 3:
                return new CircleItemNullViewHolder((CircleItemNullBinding) DataBindingUtil.inflate(inflater, R.layout.circle_item_null, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Circle info = circles.get(position);
        if (holder instanceof CircleUserItemDefaultViewHolder) {
            CircleUserItemDefaultViewHolder viewHolder = (CircleUserItemDefaultViewHolder) holder;
            viewHolder.setItemBinding(itemListener,
                    (Calendar.getInstance().get(Calendar.MONTH) + 1) + "月",
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "");
        } else if (holder instanceof CircleUserHeadItemViewHolder) {
            CircleUserHeadItemViewHolder viewHolder = (CircleUserHeadItemViewHolder) holder;
            viewHolder.setItemBinding(itemListener, new ImageViewBean(info.getPublishUserImage(),
                    info.getCircleBackImage()), info.getPublishUserName(), info.getLifeNotes());
        } else if (holder instanceof CircleUserIndexItemViewHolder) {
            CircleUserIndexItemViewHolder viewHolder = (CircleUserIndexItemViewHolder) holder;
            if (info.getShortPublishDate().equals(getPrevShortDate(position))) {
                viewHolder.setItemBinding(null, null, null, info, itemListener);
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(info.getPublishTime());
                viewHolder.setItemBinding(getPrevYearStr(position, calendar),
                        (calendar.get(Calendar.MONTH) + 1) + "月",
                        calendar.get(Calendar.DAY_OF_MONTH) + "", info, itemListener);
            }
        } else if (holder instanceof CircleItemNullViewHolder) {
            CircleItemNullViewHolder viewHolder = (CircleItemNullViewHolder) holder;
            viewHolder.setItemBinding(currentUserId == UserSP.getUserId());
        }
    }

    private String getPrevShortDate(int position) {
        if (position <= 0) return null;
        Circle info = circles.get(position - 1);
        if (info == null) return null;
        return info.getShortPublishDate();
    }

    private String getPrevYearStr(int position, Calendar currCalendar) {
        if (currentYear == currCalendar.get(Calendar.YEAR)) return null;

        if (position <= 0) return null;
        Circle info = circles.get(position - 1);
        if (info == null || info.getPublishTime() == null) return null;
        Calendar prevCalendar = Calendar.getInstance();
        prevCalendar.setTime(info.getPublishTime());
        if (currCalendar.get(Calendar.YEAR) == prevCalendar.get(Calendar.YEAR))
            return null;
        return currCalendar.get(Calendar.YEAR) + "年";
    }

    @Override
    public int getItemCount() {
        return circles.size();
    }
}
