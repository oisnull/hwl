package com.hwl.beta.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hwl.beta.R;
import com.hwl.beta.utils.StringUtils;

/**
 * Created by Administrator on 2018/2/4.
 */

public class ReloginDialogFragment extends DialogFragment {

    Button btnRelogin;
    TextView tvContent;
    String hintContent;
    private View.OnClickListener onLoginClickListener;

    public void setReloginClickListener(View.OnClickListener onLoginClickListener) {
        this.onLoginClickListener = onLoginClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.relogin_dialog, container, false);
        btnRelogin = view.findViewById(R.id.btn_relogin);
        tvContent = view.findViewById(R.id.tv_content);
        btnRelogin.setOnClickListener(onLoginClickListener);
        return view;
    }

    public void setHintText(String text) {
        hintContent = text;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (StringUtils.isNotBlank(hintContent))
            tvContent.setText(hintContent);
    }

    public void dismiss() {
        super.dismiss();
    }
}
