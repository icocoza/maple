package com.ccz.modules.common.utils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ImageResizeWorker {
    public final int THREAD_COUNT = 20;
    
    Executor executor = null;
    public interface ImageResizerCallback {
    		void onCompleted(Object dest);
    		void onFailed(Object src);
    }
    
    public ImageResizeWorker() {
    	executor = Executors.newFixedThreadPool(THREAD_COUNT);
    }

    public void doResize(final String src, final String dest, final int width, final int height, final ImageResizerCallback cb) {
		Runnable runnable = () -> {
			try {
				if(ImageUtil.resize(new File(src), new File(dest), width, height)==true) {
					cb.onCompleted(dest);
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			cb.onFailed(src);
		};
		executor.execute(runnable);
    }

    public void doCrop(final String src, final String dest, final int width, final int height, final ImageResizerCallback cb) {
		Runnable runnable = () -> {
			try {
				if(ImageUtil.crop(new File(src), new File(dest), width, height)==true) {
					cb.onCompleted(dest);
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			cb.onFailed(src);
		};
		executor.execute(runnable);
    }

}
