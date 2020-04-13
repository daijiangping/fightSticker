package com.acey.fightsticker.services;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestImage {
    // 为了加快速度，在内存中开启缓存
    // 主要应用于重复图片较多时，或者同一个图片要多次被访问，比如在ListView时来回滚动
    public Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();

    // 固定 10 个线程来执行任务
    private ExecutorService _exeService = Executors.newFixedThreadPool(10);
    private final Handler _handler = new Handler();

    public Drawable getImage(final String url, final Callback callback) {

        // 缓存中存在就用缓存中的图片
        if (imageCache.containsKey(url)) {
            SoftReference<Drawable> softReference = imageCache.get(url);

            if (softReference.get() != null) {
                return softReference.get();
            }
        }

        // 缓存中没有图片，就从网络中获取图片，同时，存入缓存
        _exeService.submit(new Runnable() {

            @Override
            public void run() {
                final Drawable drawable = getImage(url);
                imageCache.put(url, new SoftReference<Drawable>(drawable));

                _handler.post(new Runnable() {

                    @Override
                    public void run() {
                        callback.imageLoaded(drawable);
                    }
                });
            }
        });

        return null;
    }

    // 从网络中获取图片
    protected Drawable getImage(String url) {
        Drawable drawable = null;

        try {
            drawable = Drawable.createFromStream(new URL(url).openStream(), "img.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return drawable;
    }

    // 回调方法
    public interface Callback {
        void imageLoaded(Drawable drawable);
    }
}