package com.acey.fightsticker.services;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class StickerService extends AccessibilityService {
    static String content = "";
    private static String EDIT_TEXT_CLASS_NAME = "android.widget.EditText";
    private static AccessibilityNodeInfo textNode;
    private static HorizontalScrollView stickerLayout;
    private static LinearLayout linearLayout;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();

        if (AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED == eventType) {
            textNode = getEditText();
            String text = Objects.isNull(textNode.getText()) ? "" : textNode.getText() + "";
            if (!text.equals(content) && text.endsWith(" ")) {
                Optional.ofNullable(linearLayout).ifPresent(ViewGroup::removeAllViews);
                Optional.ofNullable(stickerLayout).ifPresent(ViewGroup::removeAllViews);
                content = text;
                WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
                lp.format = PixelFormat.TRANSLUCENT;
                lp.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = 200;

                stickerLayout = new HorizontalScrollView(this);

                linearLayout = new LinearLayout(this);
                stickerLayout.addView(linearLayout);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.BOTTOM);

                List<String> data = Arrays.asList("https://tva1.sinaimg.cn/large/006Xzox4gy1gclawe1n2oj306o06ojri.jpg",
                        "https://tva1.sinaimg.cn/large/006Xzox4gy1gclaw2pnyij306o06oaa4.jpg",
                        "https://tva1.sinaimg.cn/large/006Xzox4gy1gclaw38lo3j306o06oq30.jpg",
                        "https://tva1.sinaimg.cn/large/006Xzox4gy1gclaw985btj306o06odfx.jpg",
                        "https://tva1.sinaimg.cn/large/006Xzox4gy1gclaw4c5zxj306o06omxa.jpg"
                );
                for (int i = 0; i < 4; ++i) {
                    ImageView imageView = new ImageView(this);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));  //设置图片宽高
                    Bitmap httpBitmap = FileUtil.getImageInputStream(data.get(i));
                    String filePath = FileUtil.saveImage(httpBitmap, getApplicationContext());

                    imageView.setImageBitmap(httpBitmap); //图片资源
                    imageView.setOnClickListener(v -> {
                        Bundle arguments = new Bundle();
                        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, filePath);
                        textNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    });

                    linearLayout.addView(imageView); //动态添加图片
                }
                windowManager.addView(stickerLayout, lp);
                Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
            }
        }
        if (isChangePage()) {
            Optional.ofNullable(linearLayout).ifPresent(ViewGroup::removeAllViews);
            Optional.ofNullable(stickerLayout).ifPresent(ViewGroup::removeAllViews);
            stickerLayout = null;
            linearLayout = null;
        }
    }

    private boolean isChangePage() {
        AccessibilityNodeInfo editText = getEditText();
        if (Objects.isNull(editText) && Objects.nonNull(stickerLayout)) {
            return true;
        }
        if (Objects.nonNull(editText)) {
            Rect rect = new Rect();
            editText.getBoundsInScreen(rect);
            return rect.bottom > 1500;
        }
        return false;
    }


    private AccessibilityNodeInfo getEditText() {
        AccessibilityNodeInfo root = getRootInActiveWindow();
        return findByClassName(root, EDIT_TEXT_CLASS_NAME);
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
