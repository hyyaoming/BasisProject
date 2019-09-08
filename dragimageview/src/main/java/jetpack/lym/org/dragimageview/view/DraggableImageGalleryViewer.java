package jetpack.lym.org.dragimageview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jetpack.lym.org.dragimageview.R;
import jetpack.lym.org.dragimageview.core.DraggableImageView;
import jetpack.lym.org.dragimageview.entities.DraggableImageInfo;

/**
 * author: ym.li
 * since: 2019/9/8
 */
public class DraggableImageGalleryViewer extends FrameLayout {
    private static final String TAG_PREGIX = "DraggableImageGalleryViewer_";
    private DraggableImageGalleryViewer.ActionListener actionListener;
    private ArrayList<DraggableImageInfo> mImageList;
    private boolean showWithAnimator;
    private ArrayList<DraggableImageView> vpContentViewList;
    private FixViewPager mHackyViewPager;
    private TextView mTvIndex;

    public DraggableImageGalleryViewer(@NonNull Context context) {
        this(context, null);
    }

    public DraggableImageGalleryViewer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableImageGalleryViewer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.mImageList = new ArrayList<>();
        this.showWithAnimator = true;
        LayoutInflater.from(getContext()).inflate(R.layout.view_image_viewr, this);
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mHackyViewPager = findViewById(R.id.vp);
        mTvIndex = findViewById(R.id.mImageViewerTvIndicator);
        this.setBackground(new ColorDrawable(0));
        this.initAdapter();
        this.vpContentViewList = new ArrayList<>();
    }

    private void initAdapter() {
        mHackyViewPager.setAdapter((new PagerAdapter() {
            public boolean isViewFromObject(@NonNull View view, @NonNull Object any) {
                return view.equals(any);
            }

            public int getCount() {
                return DraggableImageGalleryViewer.this.mImageList.size();
            }

            @NonNull
            public DraggableImageView instantiateItem(@NonNull ViewGroup container, int position) {
                DraggableImageView imageView = getImageViewFromCacheContainer();
                container.addView(imageView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                if (showWithAnimator) {
                    showWithAnimator = false;
                    imageView.showImageWithAnimator(mImageList.get(position));
                } else {
                    imageView.showImage(mImageList.get(position));
                }
                imageView.setTag(TAG_PREGIX + position);
                return imageView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object any) {
                container.removeView((View) any);
            }
        }));
        mHackyViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentImgIndex(position);
            }
        });
    }

    public boolean closeWithAnimator() {
        DraggableImageView currentView = findViewWithTag(TAG_PREGIX + mHackyViewPager.getCurrentItem());
        DraggableImageInfo imageInfo = mImageList.get(mHackyViewPager.getCurrentItem());
        if (imageInfo.getDraggableInfo().isValid()) {
            if (null != currentView) {
                currentView.closeWithAnimator();
            }
        } else {
            if (null != actionListener) {
                actionListener.closeViewer();
            }
        }
        return true;
    }

    private DraggableImageView getImageViewFromCacheContainer() {
        DraggableImageView availableImageView = null;
        if (null != vpContentViewList && vpContentViewList.size() > 0) {
            for (DraggableImageView draggableImageView : vpContentViewList) {
                if (draggableImageView.getParent() == null) {
                    availableImageView = draggableImageView;
                }
            }
        }
        if (availableImageView == null) {
            availableImageView = new DraggableImageView(getContext());
            availableImageView.setActionListener(new DraggableImageView.ActionListener() {
                @Override
                public void onExit() {
                    if (null != getActionListener()) {
                        getActionListener().closeViewer();
                    }
                }
            });
            this.vpContentViewList.add(availableImageView);
        }
        return availableImageView;
    }

    public final void showImagesWithAnimator(List<DraggableImageInfo> imageList, int index) {
        this.mImageList.clear();
        this.mImageList.addAll(imageList);
        PagerAdapter viewPagerAdapter = mHackyViewPager.getAdapter();
        if (viewPagerAdapter != null) {
            viewPagerAdapter.notifyDataSetChanged();
        }
        this.setCurrentImgIndex(index);
    }

    @SuppressLint("SetTextI18n")
    private void setCurrentImgIndex(int index) {
        mHackyViewPager.setCurrentItem(index, false);
        mTvIndex.setText(index + 1 + "/" + mImageList.size());
    }

    @Nullable
    public ActionListener getActionListener() {
        return actionListener;
    }

    public void setActionListener(@Nullable ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public interface ActionListener {
        void closeViewer();
    }
}
