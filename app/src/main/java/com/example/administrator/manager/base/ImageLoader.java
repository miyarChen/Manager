package com.example.administrator.manager.base;

import android.graphics.Bitmap;
import android.graphics.Path;
import android.os.Looper;
import android.os.Message;
import android.renderscript.Type;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Administrator on 2017/12/6 0006.
 */
public class ImageLoader {
    private static ImageLoader mInstance;

    /**
     * 图片缓存的核心对象
     */
    private LruCache<String,Bitmap> mLruCache;

    /**
     * 线程池
     */
    private ExecutorService mThreadPool;

    private static final int DEFAULT_THREAD_COUNT=1;

    /**
     * 队列的调度方式
     */
    private Type mType = Type.LIFO;

    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTaskQueue;

    /**
     * 后台轮询线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;

    /**
     * UI线程中的Handler
     */
    private Handler mUIHandler;

    public enum Type
    {
        FIFI,LIFO;
    }

    private ImageLoader(int mThreadCount,Type type){
        init(threadCount,type);
    }
    private void init(int threadCount,Type type){
        //后台轮询线程
        mPoolThread = new Thread(){
            @Override
            public void run(){
                Looper.prepare();
                mPoolThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        //线程池去取出一个任务进行执行
                    }
                };
                Looper.loop();
            };
        };
        mPoolThread.start();
        //获取我们应用的最大可用内存
        int maxMemory =(int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory/8;

        mLruCache = new LruCache<String,Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key,Bitmap value){
                return value.getRowBytes()*value.getHeight();
            }
        };

        //创建线程池
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<Runnable>();
        mType = type;
    }
    private static ImageLoader getmInstance(){
        if (mInstance == null){
            synchronized (ImageLoader.class){
                if (mInstance == null){
                    mInstance = new ImageLoader(DEFAULT_THREAD_COUNT,Type.LIFO);
                }
            }
        }
        return mInstance;
    }

    /**
     * 根据path为imageView设置哎图片
     * @param path
     * @param imageView
     */
    public void loadImage(String path,ImageView imageView){
        imageView.setTag(path);

        if (mUIHandler == null){
            mUIHandler = new Handler(){
                public void handleMassage(Message msg){
                   //获取得到图片，为imageView回调设置图片
                    ImgBeanHolder holder = (ImgBeanHolder)msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView imageview = holder.imageView;
                    String path = holder.path;
                    //将path与getTag存储路径进行比较
                    if (imageview.getTag().toString().equals(path)){
                        imageview.setImageBitmap(bm);
                    }
                };
            };
        }

        //根据path在缓存中获取bitmap
        Bitmap bm = getBitmapFromLruCache(path);

        if (bm != null){
            Message message = Message.obtain();
            ImgBeanHolder holder = new ImgBeanHolder();
            holder.bitmap = bm;
            holder.path = path;
            holder.imageView = imageView;
            message.obj = holder;
            mUIHandler.sendMessage(message);
        }else {

        }
    }

    private void addTasks(Runnable runnable){

    }
    /**
     * 根据path在缓存中获取bitma
     * @param key
     * @return
     */
    private Bitmap getBitmapFromLruCache(String key){
        return mLruCache.get(key);
    }

    private class ImgBeanHolder{
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }
}
