package com.hwl.beta.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hwl.beta.R;

/**
 * Created by Administrator on 2018/1/27.
 */

public class AddFriendDialogFragment extends DialogFragment {

    TextView tvTitle,tvRemark;
    Button btnBack,btnAdd;
    private View.OnClickListener onAddClickListener;
    private String title,remark;

    public void setOnClickListener(View.OnClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_add_friend, container, false);
        tvTitle = view.findViewById(R.id.tv_title);
        tvRemark = view.findViewById(R.id.tv_remark);
        btnAdd = view.findViewById(R.id.btn_add);
        btnBack = view.findViewById(R.id.btn_back);

        btnAdd.setOnClickListener(onAddClickListener);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void onResume() {
        tvRemark.setText(remark);
        tvTitle.setText(title);
        super.onResume();
    }

    public void dismiss() {
        super.dismiss();
    }
}
