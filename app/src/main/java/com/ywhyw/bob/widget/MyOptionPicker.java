package com.ywhyw.bob.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ywhyw.bob.R;

import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.util.ConvertUtils;

/**
 * Created by yangfan on 2016/3/27.
 */
public class MyOptionPicker extends OptionPicker {

    /**
     * Instantiates a new Option picker.
     *
     * @param activity the activity
     * @param options  the options
     */
    public MyOptionPicker(Activity activity, String[] options) {
        super(activity, options);
    }

    @Nullable
    @Override
    protected View makeFooterView() {
        View v = LayoutInflater.from(activity).inflate(R.layout.picker_foot,null);
        TextView tv = (TextView) v.findViewById(R.id.tv_submit);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
        return v;
    }

    @Nullable
    @Override
    protected View makeHeaderView() {
        return null ;
    }
}
