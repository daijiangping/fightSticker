package com.acey.fightsticker.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.acey.fightsticker.FileUtil;
import com.acey.fightsticker.R;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class StickerService extends AccessibilityService {
    static String content = "";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        if (AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED == eventType) {
            AccessibilityNodeInfo accessibilityNodeInfo = getEditText("android.widget.EditText");
            String text = Objects.isNull(accessibilityNodeInfo.getText()) ? "" : accessibilityNodeInfo.getText() + "";
            if (!text.equals(content)) {
                content = text;
                WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
                lp.format = PixelFormat.TRANSLUCENT;
                lp.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = 200;
//                lp.gravity = Gravity.TOP;

                HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);

                LinearLayout lineLayout = new LinearLayout(this);
                horizontalScrollView.addView(lineLayout);
                lineLayout.setOrientation(LinearLayout.HORIZONTAL);
                lineLayout.setGravity(Gravity.BOTTOM);

//size:代码中获取到的图片数量
                lineLayout.removeAllViews();  //clear linearlayout
                for (int i = 0; i < 10; ++i) {
                    ImageView imageView = new ImageView(this);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));  //设置图片宽高
                    Bitmap httpBitmap = FileUtil.getImageInputStream("https://images0.cnblogs.com/blog/430074/201302/01220037-4e6a57c1199748fea9f8391e7e0548d7.jpg");
                    String filePath = FileUtil.saveImage(httpBitmap,getApplicationContext());

                    imageView.setImageBitmap(httpBitmap); //图片资源
                    imageView.setOnClickListener(v -> {
                        Bundle arguments = new Bundle();
                        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, filePath);
                        accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    });
                    lineLayout.addView(imageView); //动态添加图片

                }
                wm.addView(horizontalScrollView, lp);
                Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
            }
        }
    }



    private AccessibilityNodeInfo getEditText(String viewClassName) {
        AccessibilityNodeInfo root = getRootInActiveWindow();
        return findByClassName(root, viewClassName);
    }

    private AccessibilityNodeInfo findByClassName(AccessibilityNodeInfo root, String viewClassName) {
        for (int i = 0; i < root.getChildCount(); ++i) {
            AccessibilityNodeInfo child = root.getChild(i);
            if (child.getClassName().equals(viewClassName)) {
                return child;
            }
            AccessibilityNodeInfo found = findByClassName(child, viewClassName);
            if (Objects.nonNull(found)) {
                return found;
            }
        }
        return null;
    }

    private void startClick(AccessibilityNodeInfo rootNode) {
        List<AccessibilityNodeInfo> nodes = findByText(rootNode);

        if (nodes.isEmpty()) {
            return;
        }
        AccessibilityNodeInfo lastNode = nodes.get(nodes.size() - 1);
        System.out.println(lastNode);
        if (lastNode.getClassName().equals("android.widget.Button")) {
            lastNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            return;
        }

//        lastNode.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        FrameLayout mLayout = new FrameLayout(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        lp.format = PixelFormat.TRANSLUCENT;
        lp.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.TOP;
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.image_list, mLayout);
        wm.addView(mLayout, lp);
    }

    private List<AccessibilityNodeInfo> findByText(AccessibilityNodeInfo rootNode) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<AccessibilityNodeInfo> open = getRootInActiveWindow().findAccessibilityNodeInfosByText("开");
        if (!open.isEmpty()) {
            return open;
        }
        return rootNode.findAccessibilityNodeInfosByText("恭喜发财");
    }

    @Override
    public void onInterrupt() {

    }
}
