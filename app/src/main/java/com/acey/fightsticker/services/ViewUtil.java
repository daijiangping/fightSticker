package com.acey.fightsticker.services;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewUtil {

    static void addView(final LinearLayout lineLayout, Context context)
    {
        final TextView showText = new TextView(context);
        showText.setTextColor(Color.GREEN);
        showText.setTextSize(30);
        showText.setId(11);//设置 id
        showText.setText("我是在程序中添加的第一个文本");
        showText.setBackgroundColor(Color.GRAY);
        // set 文本大小
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //set 四周距离
        params.setMargins(10, 10, 10, 10);

        showText.setLayoutParams(params);

        //添加文本到主布局
        lineLayout.addView(showText );

        //创建按钮
        Button btn = new Button(context);
        btn.setText("点击删除文本");
        btn.setBackgroundColor(Color.GRAY) ;
        LinearLayout.LayoutParams btn_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        btn_params.setMargins(0, 60, 60, 0);
        btn_params.gravity = Gravity.CENTER_HORIZONTAL;
        btn.setLayoutParams(btn_params);
        // 动态添加按钮到主布局
        lineLayout.addView(btn);
        //动态创建一个相对布局
        RelativeLayout relaLayout = new RelativeLayout(context);
        relaLayout.setBackgroundColor(Color.BLUE);
        RelativeLayout.LayoutParams lp11 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 120);


        relaLayout.setLayoutParams(lp11);
        //动态创建一个文本
        final TextView RelaText = new TextView(context);
        RelaText.setTextColor(Color.GREEN);
        RelaText.setTextSize(20);
        RelaText.setText("我是在程序中添加的第二个文本，在相对布局中");
        RelaText.setBackgroundColor(Color.GRAY);

        //设置文本的布局
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        lp2.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        lp2.setMargins(10, 10, 10, 10);
        //将文本添加到相对布局中
        relaLayout.addView(RelaText,lp2);
        //将这个布局添加到主布局中
        lineLayout.addView(relaLayout);

    }

}
