package com.stela.comics_unlimited.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lxy.baselibs.glide.GlideUtils;
import com.stela.comics_unlimited.R;
import com.stela.comics_unlimited.data.entity.SeriesAsset;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class HomeBanner extends FrameLayout {
    /**
     * 宽高比
     */
    private float scale = 1.0F;
    private int mLayoutResId = R.layout.layout_home_banner;
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int mIndicatorMargin;
    private int mIndicatorSelectedResId;
    private int mIndicatorUnselectedResId;
    private int indicatorSize;
    private Context context;
    private HomeBannerViewPager mViewPager;
    private List<HomeImageView> pagerViews = new ArrayList<>();
    private BannerPagerAdapter mBannerAdapter;
    private CircleIndicator mCircleIndicator;
    private ArrayList<ArrayList<SeriesAsset>> assets;
    private OnBannerListener listener;

    public HomeBanner(@NonNull Context context) {
        super(context);
    }

    public HomeBanner(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context, attrs);
    }

    public HomeBanner(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        pagerViews.clear();
        handleTypedArray(context, attrs);
        View view = LayoutInflater.from(context).inflate(mLayoutResId, this, true);
        mViewPager = (HomeBannerViewPager) view.findViewById(R.id.vp_banner);
        mCircleIndicator = (CircleIndicator) view.findViewById(R.id.ci_home_indicator);

    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerHome);
//        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.BannerHome_indicator_width, indicatorSize);
//        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.BannerHome_indicator_height, indicatorSize);
//        mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.BannerHome_indicator_margin, BannerConfig.PADDING_SIZE);
//        mIndicatorSelectedResId = typedArray.getResourceId(R.styleable.BannerHome_indicator_drawable_selected, R.drawable.gray_radius);
//        mIndicatorUnselectedResId = typedArray.getResourceId(R.styleable.BannerHome_indicator_drawable_unselected, R.drawable.white_radius);
//        mLayoutResId = typedArray.getResourceId(R.styleable.BannerHome_banner_layout, mLayoutResId);
        scale = typedArray.getFloat(R.styleable.BannerHome_scale_home, 1.0F);
        typedArray.recycle();
    }
    //获取数据
    public void setData(ArrayList<ArrayList<SeriesAsset> > assets) {
        //加载viewpager
        this.assets =assets;
        for (int i = 0; i < assets.size(); i++) {
            HomeImageView  mHomeImageView =   new HomeImageView(context);
            mHomeImageView.setScale(scale);
            mHomeImageView.setLock(assets.get(i).get(0).isLock ==1,true);
            pagerViews.add(mHomeImageView);
        }
        initViewpager();
    }

    private void initViewpager() {
        mBannerAdapter = new BannerPagerAdapter();
        mViewPager.setAdapter(mBannerAdapter);
        mCircleIndicator.setViewPager(mViewPager);
    }

    public void setOnBannerListener(OnBannerListener listener) {
        this.listener = listener;
    }


    private class BannerPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            if (pagerViews != null) {
                return pagerViews.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            HomeImageView homeImageView = pagerViews.get(position);
            GlideUtils.loadImageHttps(context,homeImageView.getImageView(),assets.get(position).get(0).url,R.color.stela_blue);
            if (listener != null) {
                homeImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.OnBannerClick(position);
                    }
                });
            }
            container.addView(homeImageView);
            return homeImageView;
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        if (scale>0) {
            int childWidthSize = getMeasuredWidth();
            //高度和宽度一样
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childWidthSize * scale), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
