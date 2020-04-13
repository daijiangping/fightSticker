package com.acey.fightsticker.services;

import android.accessibilityservice.AccessibilityService;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.acey.fightsticker.R;

import java.util.List;
import java.util.Objects;

public class StickerService extends AccessibilityService {
    static String content = "";
    private String[] filter = new String[]{"恭喜发财", "开"};
    //    树节点

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        if (AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED == eventType) {
            AccessibilityNodeInfo accessibilityNodeInfo = getEditText("android.widget.EditText");
            String text = Objects.isNull(accessibilityNodeInfo.getText()) ? "" : accessibilityNodeInfo.getText() + "";
            if (!text.equals(content)) {
                content = text;
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
