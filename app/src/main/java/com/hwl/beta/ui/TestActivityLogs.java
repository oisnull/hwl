package com.hwl.beta.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwl.beta.R;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.CustLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class TestActivityLogs extends BaseActivity {

    Activity mActivity;
    TextView tvLogContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_logs);
        mActivity = this;

        initView();
    }

    private void initView() {
        LinearLayout llLogFiles = findViewById(R.id.ll_log_files);
        tvLogContent = findViewById(R.id.tv_log_content);
        List<String> files = CustLog.getLogFiles();
        for (String f : files) {
            TextView tv = new TextView(mActivity);
            tv.setText(f);
            tv.setTextSize(16);
            tv.setOnClickListener(v -> {
                loadLog(f);
            });
            llLogFiles.addView(tv);
        }

        findViewById(R.id.btn_clear).setOnClickListener(v -> {
            for (String f : files) {
                File file = new File(f);
                file.delete();
            }
            llLogFiles.removeAllViews();
            tvLogContent.setText("");
        });
    }

    private void loadLog(String path) {
        String content = readFileContent(path);
        tvLogContent.setText(content);
    }

    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr + "\n");
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }
}
