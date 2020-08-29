package com.hwl.beta.ui.dialog;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hwl.beta.R;

/**
 * Created by Administrator on 2018/1/20.
 */

public class LocationDialogFragment extends DialogFragment {

    TextView tvTitle,tvContent;
    Button btnBack;
    Button btnResetLocation;
    private View.OnClickListener onResetClickListener;
    private String title,content;

    public void setResetLocationClickListener(View.OnClickListener onResetClickListener) {
        this.onResetClickListener = onResetClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_location, container, false);
        btnBack = view.findViewById(R.id.btn_back);
        tvContent = view.findViewById(R.id.tv_content);
        tvTitle = view.findViewById(R.id.tv_title);
        btnResetLocation = view.findViewById(R.id.btn_reset_location);
        btnResetLocation.setOnClickListener(onResetClickListener);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    public void setContentShow(String content) {
        this.content = content;
    }
    public void setTitleShow(String title) {
        this.title = title;
    }

    @Override
    public void onResume() {
        tvContent.setText(content);
        tvTitle.setText(title);
        super.onResume();
    }

    public void dismiss() {
        super.dismiss();
    }
}
