package basisproject.lym.org.basisproject.dragimageview;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import basisproject.lym.org.basisproject.BaseActivity;
import basisproject.lym.org.basisproject.R;
import jetpack.lym.org.dragimageview.core.DraggableImageViewerHelper;
import jetpack.lym.org.dragimageview.core.ImageInfo;

/**
 * author: ym.li
 * since: 2019/9/8
 */
public class DragImageViewActivity extends BaseActivity {

    private List<ImageInfo> imags = new ArrayList<>();
    private List<View> mViews = new ArrayList<>();
    private ImageView mIv3;
    private ImageView mIv2;
    private ImageView mIv1;

    private void initData() {
        imags.add(new ImageInfo("https://upload-bbs.mihoyo.com/upload/2019/08/21/73766616/4d09b6b94491d3921344be906aa7971a_4136353673894269217.png"));
        imags.add(new ImageInfo("https://upload-bbs.mihoyo.com/upload/2019/08/12/50600998/1543e13e5cba414a1e4d396d8e6bdbb0_4959259236143228277.jpg"));
        imags.add(new ImageInfo("https://upload-bbs.mihoyo.com/upload/2019/02/03/74582189/ede10255b2a99cfcf33868d1afd81a92_6059341049122226062.png"));
    }

    @Override
    protected void bindViews() {
        mViews.add(mIv1);
        mViews.add(mIv2);
        mViews.add(mIv3);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_drag_image;
    }

    private void loadImage(ImageView imageView, String url) {
        Glide.with(this)
                .load(url)
                .into(imageView);
    }

    @Override
    protected void initViews() {
        initData();
        mIv1 = findViewById(R.id.mImagesIv1);
        loadImage(mIv1, imags.get(0).getThumbnailUrl());
        mIv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImages(0);
            }
        });


        mIv2 = findViewById(R.id.mImagesIv2);
        loadImage(mIv2, imags.get(1).getThumbnailUrl());
        mIv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImages(1);
            }
        });

        mIv3 = findViewById(R.id.mImagesIv3);
        loadImage(mIv3, imags.get(2).getThumbnailUrl());
        mIv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImages(2);
            }
        });
    }

    private void showImages(int index) {
        DraggableImageViewerHelper.showImages(this, mViews, imags, index);
    }
}
