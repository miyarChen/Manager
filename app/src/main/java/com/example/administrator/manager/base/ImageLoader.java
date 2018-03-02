package com.example.administrator.manager.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.os.Looper;
import android.os.Message;
import android.renderscript.Type;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
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

    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
    public enum Type
    {
        FIFO,LIFO;
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
                        mThreadPool.execute(getTask());
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

    /**
     * 从任务队列取出一个方法
     * @return
     */
    private Runnable getTask(){
        if(mType == Type.FIFO)
        {
            return mTaskQueue.removeFirst();
        }else if(mType == Type.LIFO){
            return mTaskQueue.removeLast();
        }
        return null;
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
     * 根据path为imageView设置图片
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
            addTask(new Runnable({
                @Override
                public void run()
                {
                    //加载图片
                    //图片的压缩
                    //1.获得图片需要显示的大小
                    ImageSize imageSize = getImageViewSize(imageView);
                    //2.压缩图片
                    Bitmap bm = decodeSampeledBitmapFromPath(path,imageSize.width,imageSize.height);
                    //3.把图片加入到缓存
                    addBitmapToLruCache(path,bm);

                    Message message = Message.obtain();
                    ImgBeanHolder holder = new ImgBeanHolder();
                    holder.bitmap = bm;
                    holder.path = path;
                    holder.imageView = imageView;
                    message.obj = holder;
                    mUIHandler.sendMessage(message);
                }
            });
        }
    }

    /**
     * 将图片加入LruCache
     * @param path
     * @param bm
     */
    protected void addBitmapToLruCache(String path,Bitmap bm){

        if (getBitmapFromLruCache(path) == null){
            if (bm != null)
                mLruCache.put(path,bm);
        }
    }

    /**
     * 根据图片需要显示的宽和高对图片进行压缩
     * @param path
     * @param width
     * @param height
     * @return
     */
    protected Bitmap decodeSampeledBitmapFromPath(String path,int width,int height)
    {
        //获取图片的宽和高，并不把图片加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        options.inSampleSize = caculateInSampleSize(options,width,height);

        //使用或得到的InSampleSize再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
        return bitmap;
    }

    /**
     * 根据需求的宽和高以及图片实际的宽和高计算SampleSize
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int caculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;

        if(width > reqWidth || height > reqHeight)
        {
            int widthRadio = Math.round(width*1.0f/reqWidth);
            int heightRadio = Math.round(height*1.0f/reqHeight);

            inSampleSize = Math.max(widthRadio,heightRadio);
        }
        return inSampleSize;
    }
    /**
     * 根据ImageView获取适当的压缩的宽和高
     * @param imageView
     * @return
     */
    protected ImageSize getImageViewSize(ImageView imageView)
    {
        ImageSize imageSize = new ImageSize();

        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        LayoutParams lp = imageView.getLayoutParams();

       // int width = lp.width ==(LayoutParams.WRAP_CONTENT? 0:imageView.getWidth());
        int width = imageView.getWidth();//获取imageView的实际宽度
        if(width<=0){
            width=lp.width;//获取imageview在layout中声明的宽度
        }
        if (width <= 0){
            width = imageView.getMaxWidth();//检查最大值
        }
        if (width <= 0){
            width  = displayMetrics.widthPixels;
        }

        int height = imageView.getHeight();//获取imageView的实际宽度
        if(height <= 0){
            height = lp.height;//获取imageview在layout中声明的宽度
        }
        if (height <= 0){
            height = imageView.getMaxHeight();//检查最大值
        }
        if (height <= 0){
            height  = displayMetrics.heightPixels;
        }

        imageSize.width = width;
        imageSize.height = height;
        return imageSize;
    }
    private void addTask(Runnable runnable){
        mTaskQueue.add(runnable);
        try
        {
            mSemaphorePoolThreadHandler.acquire();
        }catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        mPoolThreadHandler.sendEmptyMessage(0x110);
    }
    /**
     * 根据path在缓存中获取bitma
     * @param key
     * @return
     */
    private Bitmap getBitmapFromLruCache(String key){
        return mLruCache.get(key);
    }

    private class ImageSize{
        int width;
        int height;
    }
    private class ImgBeanHolder{
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }
}
