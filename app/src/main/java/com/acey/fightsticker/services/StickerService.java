package com.acey.fightsticker.services;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.net.Uri;
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

import com.acey.fightsticker.MainActivity;
import com.acey.fightsticker.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import androidx.fragment.app.Fragment;

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
                Fragment fragment = new StickerListService();
                WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
                lp.format = PixelFormat.TRANSLUCENT;
                lp.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = 1000;
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
                    imageView.setImageBitmap(getHttpBitmap("https://images0.cnblogs.com/blog/430074/201302/01220037-4e6a57c1199748fea9f8391e7e0548d7.jpg")); //图片资源
                    lineLayout.addView(imageView); //动态添加图片

                }
                wm.addView(horizontalScrollView, lp);
                Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
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
