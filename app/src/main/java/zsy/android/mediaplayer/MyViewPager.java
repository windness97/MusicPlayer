package zsy.android.mediaplayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2015/8/14.
 */
public class MyViewPager {

    static final public int FLAG_DONOTCHANGE = Integer.MAX_VALUE;

    private int textSize = 15;
    private int textColor = Color.parseColor("#505050");
    private int cursorColor = Color.parseColor("#1080CC");
    private int cursorHeight = 5;

    public void setTextSize(int size){textSize = size;Log.i("MyLog", "size = " + textSize);}
    public void setTextColor(int color){textColor = color;}
    public void setCursorColor(int color){cursorColor = color;}
    public void setCursorHeight(int height){cursorHeight = height;}
    public void init(int size, int textColor, int cursorColor, int cursorHeight){
        if (size != FLAG_DONOTCHANGE) {setTextSize(size);}
        if (textColor != FLAG_DONOTCHANGE) {setTextColor(textColor);}
        if (cursorColor != FLAG_DONOTCHANGE) {setCursorColor(cursorColor);}
        if (cursorHeight != FLAG_DONOTCHANGE) {setCursorHeight(cursorHeight);}
    }

    Activity activity;
    LinearLayout myView;

    private int startIndex = 0;
    private int points = 0;
    private List<View> viewList;
    private List<String> nameList;
    private MyPagerAdapter adapter;

    private LinearLayout textContent;
    private FrameLayout layout;
    private ImageView animCursor;
    private ViewPager mPager;

    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int cursorW;// 动画图片宽度

    public MyViewPager(Activity activity, View view, List<View> list, List<String> name){
        this.activity = activity;
        myView = (LinearLayout) view;
        viewList = list;
        nameList = name;
        points = viewList.size();
    }

    public MyViewPager(Activity activity, View view, List<View> list, List<String> name, int start){
        this(activity, view, list, name);
        startIndex = start;
    }

    public void prepare(){
        initViews();
        initImageCursor();
        initViewPager();

    }

    public void initViews(){
        mPager = new ViewPager(activity);
        mPager.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        mPager.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);

        layout = new FrameLayout(activity);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        animCursor = new ImageView(activity);
        animCursor.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        layout.addView(animCursor);

        textContent = new LinearLayout(activity);
        textContent.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textContent.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < points; i++) {
            TextView textView = new TextView(activity);
            textView.setText(nameList.get(i));
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0, 10, 0, 10);
            textView.setTextSize(textSize);
            textView.setTextColor(textColor);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1
            ));
            if (textContent == null) Log.i("MyLog", "textContent == null");
            else Log.i("MyLog", "textContent != null");
            textView.setOnClickListener(new MyOnClickListener(i));
            textContent.addView(textView);
        }
        myView.addView(textContent);
        myView.addView(layout);
        myView.addView(mPager);
    }

    public void initImageCursor(){
//        int layoutW = layout.getWidth();// 获取装载cursor的layout的宽度

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int layoutW = dm.widthPixels;// 获取分辨率宽度

        Log.i("MyLog", layoutW + "");
        Bitmap map = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        map.setPixel(0 ,0 , cursorColor);
        Bitmap btm = Bitmap.createScaledBitmap(
//                BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_bar),
                map,
                layoutW / points * 7 / 8,
                cursorHeight,
                false);
        animCursor.setImageBitmap(btm);

        cursorW = btm.getWidth();//计算光标宽度
        Log.i("MyLog", cursorW + "");

        offset = (layoutW / points - cursorW) / 2;// 计算偏移量
        Log.i("MyLog", offset + "");

        Animation animation = new TranslateAnimation(0, offset, 0, 0);
//        animation.setDuration(0);
        animation.setFillAfter(true);
        animCursor.setAnimation(animation);//设置光标初始位置
    }

    public void initViewPager(){
        adapter = new MyPagerAdapter(viewList);
        mPager.setAdapter(adapter);
        mPager.setCurrentItem(startIndex);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mViewList;

        MyPagerAdapter(List<View> list){
            mViewList = list;
        }

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mViewList.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mViewList.get(arg1), 0);
            return mViewList.get(arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        int singlePointW = offset * 2 + cursorW;
        int maxPoints = points;
        int positionArray[] = new int[maxPoints];

        public MyOnPageChangeListener(){
            super();
            for (int i = 0; i < maxPoints; i++){
                positionArray[i] = offset + i * singlePointW;
                Log.i("MyLog", "positionArray[" + i + "] = " + positionArray[i]);
            }
        }

        @Override
        public void onPageSelected(int position) {
            Animation animation = new TranslateAnimation(positionArray[currIndex], positionArray[position], 0, 0);
            Log.i("MyLog", "currIndex = " + currIndex + "   its position is " + positionArray[currIndex]);
            Log.i("MyLog", "position = " + position + "   its position is " + positionArray[position]);
            currIndex = position;
            animation.setDuration(300);
            animation.setFillAfter(true);
            animCursor.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public class MyOnClickListener implements View.OnClickListener{
        private int index = -1;
        public MyOnClickListener(int index){this.index = index;}
        @Override
        public void onClick(View v) {mPager.setCurrentItem(index);}
    }

}
