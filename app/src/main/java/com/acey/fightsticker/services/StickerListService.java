package com.acey.fightsticker.services;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.acey.fightsticker.R;

import androidx.fragment.app.Fragment;

public class StickerListService extends Fragment {

    private LinearLayout _layout;
    //private TestImage _testImage = new TestImage();

    public StickerListService() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_list, container, false);
        initView(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void initView(View view) {
        _layout = (LinearLayout) view.findViewById(R.id.frg_home);

        for (int i = 0; i < 3; i++) {
            initCell(view);
        }
    }

    private void initCell(View view) {
        Context self = this.getContext();

        // 创建单个的单元格的容器（RelativeLayout）
        RelativeLayout.LayoutParams layoutWrapper = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout wrapper = new RelativeLayout(self);
        wrapper.setPadding(0, 30, 0, 30);
        _layout.addView(wrapper, layoutWrapper);

        // 创建封面图片（ImageView）
        RelativeLayout.LayoutParams layoutCover = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600);
        ImageView imgCover = new ImageView(self);
        int idCover = view.generateViewId();
        imgCover.setId(idCover);
        // 异步加载网络图片（图片URL为测试图片）
        loadImage("http://pic9.nipic.com/20100904/4845745_195609329636_2.jpg", imgCover);
        imgCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgCover.setPadding(20, 0, 20, 0);
        wrapper.addView(imgCover, layoutCover);

        // 创建标题（TextView）
        RelativeLayout.LayoutParams layoutTitle = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutTitle.setMargins(20, 0, 20, 0);
        layoutTitle.addRule(RelativeLayout.BELOW, idCover);
        TextView txtTitle = new TextView(self);
        int idTitle = view.generateViewId();
        txtTitle.setId(idTitle);
        txtTitle.setText("标题内容标题内容标题内容标题内容标题内容标题内容");
        txtTitle.setTextSize(20);
        // 标题单行显示，多余的字符用省略号代替（包括以下两行）
        txtTitle.setEllipsize(TextUtils.TruncateAt.END);
        txtTitle.setSingleLine();
        wrapper.addView(txtTitle, layoutTitle);

        // 创建作者（TextView）
        RelativeLayout.LayoutParams layoutAuthor = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutAuthor.setMargins(20, 0, 20, 0);
        layoutAuthor.addRule(RelativeLayout.BELOW, idTitle);
        TextView txtAuthor = new TextView(self);
        int idAuthor = view.generateViewId();
        txtAuthor.setId(idAuthor);
        txtAuthor.setText("作者名称");
        wrapper.addView(txtAuthor, layoutAuthor);

        // 创建日期（TextView）
        RelativeLayout.LayoutParams layoutTime = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutTime.setMargins(20, 0, 20, 0);
        layoutTime.addRule(RelativeLayout.BELOW, idAuthor);
        TextView txtTime = new TextView(self);
        txtTime.setText("2016年9月22日 16:33");
        wrapper.addView(txtTime, layoutTime);
    }

    // 再次封装 TestImage，实现异步加载，方便页面内调用
    private void loadImage(String url, final ImageView imageView) {
        Drawable imgCache = new TestImage().getImage(url, new TestImage.Callback() {

            @Override
            public void imageLoaded(Drawable drawable) {
                imageView.setImageDrawable(drawable);
            }
        });

        if (imgCache != null) {
            imageView.setImageDrawable(imgCache);
        }
    }
}
