package jetpack.lym.org.dragimageview.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import jetpack.lym.org.dragimageview.PhotoView;
import jetpack.lym.org.dragimageview.R;
import jetpack.lym.org.dragimageview.entities.DraggableImageInfo;
import jetpack.lym.org.dragimageview.glide.GlideHelper;
import jetpack.lym.org.dragimageview.util.Utils;

/**
 * author: ym.li
 * since: 2019/9/8
 */
public class DraggableImageView extends FrameLayout {
    private PhotoView mPhotoView;
    private DraggableZoomCore draggableZoomCore;
    private DraggableZoomCore.ActionListener draggableZoomActionListener;
    private ActionListener actionListener;
    private DraggableZoomCore.ExitAnimatorCallback exitAnimatorCallback;
    private DraggableImageInfo draggableImageInfo;
    private boolean needFitCenter = true;

    public DraggableImageView(@NonNull Context context) {
        this(context, null);
    }

    public DraggableImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initListener();
        initView();
    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    private void initListener() {
        draggableZoomActionListener = new DraggableZoomCore.ActionListener() {
            @Override
            public void onExit() {
                if (null != actionListener) {
                    actionListener.onExit();
                }
            }

            @Override
            public void currentAlphaValue(int percent) {
                setBackground(new ColorDrawable(Color.argb(percent, 0, 0, 0)));
            }
        };

        exitAnimatorCallback = new DraggableZoomCore.ExitAnimatorCallback() {
            @Override
            public void onStartInitAnimatorParams() {
                mPhotoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        };
    }

    private void initView() {
        LayoutInflater.from(this.getContext()).inflate(R.layout.view_draggable_simple_image, this);
        setLayoutParams(new android.widget.FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mPhotoView = findViewById(R.id.mDraggableImageViewPhotoView);
        mPhotoView.setOnClickListener(new OnClickListener() {
            public final void onClick(View it) {
                clickToExit();
            }
        });
    }

    private void clickToExit() {
        if (null != draggableZoomCore && draggableZoomCore.isAnimating()) {
            return;
        }
        if (mPhotoView.getScale() != 1F) {
            mPhotoView.setScale(1F, true);
        } else {
            if (null != draggableZoomCore) {
                draggableZoomCore.adjustScaleViewToCorrectLocation();
                draggableZoomCore.exitWithAnimator(false);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != draggableZoomCore) {
            draggableZoomCore.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = super.onInterceptTouchEvent(ev);
        if (draggableZoomCore.isAnimating()) {
            return false;
        }
        if (mPhotoView.getScale() != 1f) {
            return false;
        }
        if (!mPhotoView.getAttacher().displyRectIsFromTop()) {
            return false;
        }
        return null != draggableZoomCore && draggableZoomCore.onInterceptTouchEvent(isIntercept, ev);
    }

    public void showImageWithAnimator(DraggableImageInfo paramsInfo) {
        draggableImageInfo = paramsInfo;
        GlideHelper.retrieveImageWhRadioFromMemoryCache(getContext(), draggableImageInfo.getThumbnailImg(), new GlideHelper.GlideCallBack<Float>() {
            @Override
            public void onCallBack(final Float radio) {
                if (null != radio) {
                    draggableImageInfo.getDraggableInfo().setScaledViewWhRadio(radio);
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (!draggableImageInfo.getDraggableInfo().isValid()) {
                                showImage(draggableImageInfo);
                            } else {
                                DraggableImageView.this.needFitCenter = radio > (float) getWidth() * 1.0F / (float) getHeight();
                                draggableZoomCore = new DraggableZoomCore(
                                        draggableImageInfo,
                                        mPhotoView,
                                        getWidth(),
                                        getHeight(),
                                        draggableZoomActionListener,
                                        exitAnimatorCallback
                                );
                                draggableZoomCore.adjustScaleViewToInitLocation();
                                loadAvailableImage(true);
                            }
                        }
                    });
                }
            }
        });
    }

    private void loadAvailableImage(final boolean startAnimator) {
        mPhotoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mPhotoView.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
        final String thumbnailImg = draggableImageInfo.getThumbnailImg();
        String originImg = draggableImageInfo.getOriginImg();
        boolean wifiIsConnect = Utils.isWifiConnected(getContext());
        final boolean originImgInCache = GlideHelper.imageIsInCache(getContext(), originImg);
        final String targetUrl = !wifiIsConnect && !originImgInCache ? thumbnailImg : originImg;
        GlideHelper.checkImageIsInMemoryCache(getContext(), thumbnailImg, new GlideHelper.GlideCallBack<Boolean>() {
            @Override
            public void onCallBack(Boolean radio) {
                if (null != radio && radio) {
                    loadImage(thumbnailImg);
                }
                if (null != radio && radio && startAnimator) {
                    loadImage(thumbnailImg);
                    if (null != draggableZoomCore) {
                        draggableZoomCore.enterWithAnimator(new DraggableZoomCore.EnterAnimatorCallback() {
                            @Override
                            public void onEnterAnimatorStart() {
                                mPhotoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            }

                            @Override
                            public void onEnterAnimatorEnd() {
                                if (needFitCenter) {
                                    mPhotoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    draggableZoomCore.adjustViewToMatchParent();
                                }
                                loadImage(targetUrl);
                            }
                        });
                    }
                } else {
                    loadImage(targetUrl);
                    if (needFitCenter) {
                        draggableZoomCore.adjustViewToMatchParent();
                    }
                }
            }
        });
    }

    public void showImage(DraggableImageInfo paramsInfo) {
        this.draggableImageInfo = paramsInfo;
        GlideHelper.retrieveImageWhRadioFromMemoryCache(getContext(), paramsInfo.getThumbnailImg(), new GlideHelper.GlideCallBack<Float>() {
            @Override
            public void onCallBack(final Float radio) {
                if (null != radio) {
                    draggableImageInfo.getDraggableInfo().setScaledViewWhRadio(radio);
                    post(new Runnable() {
                        @Override
                        public void run() {
                            DraggableImageView.this.needFitCenter = radio > (float) getWidth() * 1.0F / (float) getHeight();
                            draggableZoomCore = new DraggableZoomCore(
                                    draggableImageInfo,
                                    mPhotoView,
                                    getWidth(),
                                    getHeight(),
                                    draggableZoomActionListener,
                                    exitAnimatorCallback
                            );
                            draggableZoomCore.adjustScaleViewToCorrectLocation();
                            loadAvailableImage(false);
                        }
                    });
                }
            }
        });
    }

    private void loadImage(final String url) {
        RequestOptions options = (new RequestOptions()).priority(Priority.HIGH);
        Glide.with(this.getContext()).load(url).apply(options).into((Target<Drawable>) (new SimpleTarget<Drawable>() {
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition transition) {
                if (resource instanceof GifDrawable) {
                    Glide.with(DraggableImageView.this.getContext()).load(url).into(mPhotoView);
                } else {
                    mPhotoView.setImageBitmap(translateToFixedBitmap(resource));
                }
                if (TextUtils.equals(url, draggableImageInfo.getOriginImg())) {
                    float whRadio = (float) resource.getIntrinsicWidth() * 1.0F / (float) resource.getIntrinsicHeight();
                    if (whRadio < (float) getWidthPixels() * 1.0F / (float) Utils.getScreenHeight(getContext())) {
                        mPhotoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    } else {
                        mPhotoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    }
                }
            }
        }));
    }

    private Bitmap translateToFixedBitmap(Drawable originDrawable) {
        float whRadio = (float) originDrawable.getIntrinsicWidth() * 1.0F / (float) originDrawable.getIntrinsicHeight();
        int screenWidth = getWidthPixels();
        int bpWidth = this.getWidth() != 0 ? (originDrawable.getIntrinsicWidth() > this.getWidth() ? this.getWidth() : originDrawable.getIntrinsicWidth()) : (originDrawable.getIntrinsicWidth() > screenWidth ? screenWidth : originDrawable.getIntrinsicWidth());
        if (bpWidth > screenWidth) {
            bpWidth = screenWidth;
        }
        int bpHeight = (int) ((float) bpWidth * 1.0F / whRadio);
        Glide glide = Glide.get(this.getContext());
        Bitmap bp = glide.getBitmapPool().get(bpWidth, bpHeight, bpHeight > 5000 ? Bitmap.Config.RGB_565 : Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bp);
        originDrawable.setBounds(0, 0, bpWidth, bpHeight);
        originDrawable.draw(canvas);
        return bp;
    }

    private int getWidthPixels() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public void closeWithAnimator() {
        if (null != draggableZoomCore) {
            draggableZoomCore.adjustScaleViewToCorrectLocation();
            draggableZoomCore.exitWithAnimator(false);
        }
    }

    public interface ActionListener {
        void onExit(); // drag to exit
    }
}
