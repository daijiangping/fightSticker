package com.acey.fightsticker;

import android.accessibilityservice.AccessibilityService;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RedService extends AccessibilityService {
    private String[] filter = new String[]{"恭喜发财", "开"};
    //    树节点
    private AccessibilityNodeInfo rootNode = null;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        rootNode = getRootInActiveWindow();
        System.out.println(rootNode);
        if (Objects.isNull(rootNode)) {
            return;
        }
        startClick(rootNode);
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
        inflater.inflate(R.layout.action_bar, mLayout);
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
