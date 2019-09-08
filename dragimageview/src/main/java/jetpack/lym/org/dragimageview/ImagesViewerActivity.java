package jetpack.lym.org.dragimageview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import jetpack.lym.org.dragimageview.entities.DraggableImageInfo;
import jetpack.lym.org.dragimageview.view.DraggableImageGalleryViewer;

/**
 * author: ym.li
 * since: 2019/9/8
 */
public class ImagesViewerActivity extends AppCompatActivity {
    private static final String PARAMS = "draggableImages";
    private static final String INDEX = "index";
    private DraggableImageGalleryViewer mGalleryViewer;

    public static void start(Context context, ArrayList<DraggableImageInfo> draggableImages, int index) {
        Intent intent = new Intent(context, ImagesViewerActivity.class);
        intent.putExtra(PARAMS, draggableImages);
        intent.putExtra(INDEX, index);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_activity);
        transparentStatusBar(this);
        initView();
        initData();
    }

    private void initData() {
        List<DraggableImageInfo> draggableImages = (List<DraggableImageInfo>) getIntent().getSerializableExtra(PARAMS);
        int index = getIntent().getIntExtra(INDEX, 0);
        if (null != draggableImages && draggableImages.size() > 0) {
            mGalleryViewer.showImagesWithAnimator(draggableImages, index);
        }
    }

    private void initView() {
        mGalleryViewer = findViewById(R.id.root_view);
        mGalleryViewer.setActionListener(new DraggableImageGalleryViewer.ActionListener() {
            @Override
            public void closeViewer() {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!mGalleryViewer.closeWithAnimator()) {
            super.onBackPressed();
        }
    }
}
