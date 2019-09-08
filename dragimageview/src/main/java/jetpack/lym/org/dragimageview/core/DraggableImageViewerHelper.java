package jetpack.lym.org.dragimageview.core;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import jetpack.lym.org.dragimageview.ImagesViewerActivity;
import jetpack.lym.org.dragimageview.entities.DraggableImageInfo;
import jetpack.lym.org.dragimageview.entities.DraggableParamsInfo;


/**
 * author: ym.li
 * since: 2019/9/8
 */
public class DraggableImageViewerHelper {

    public static void showImages(Context context, List<View> views, List<ImageInfo> imageInfos, int index) {
        if (null == imageInfos || imageInfos.size() == 0) {
            return;
        }
        ArrayList<DraggableImageInfo> draggableImageInfos = new ArrayList<>();
        for (int i = 0; i < imageInfos.size(); i++) {
            ImageInfo info = imageInfos.get(i);
            if (views != null && i < views.size()) {
                draggableImageInfos.add(createImageDraggableParamsWithWHRadio(views.get(i), info.getOriginUrl(), info.getThumbnailUrl(), info.getImgSize()));
            } else {
                draggableImageInfos.add(createImageDraggableParamsWithWHRadio(null, info.getOriginUrl(), info.getThumbnailUrl(), info.getImgSize()));
            }
        }
        ImagesViewerActivity.start(context, draggableImageInfos, index);
    }

    /**
     * 根据宽高比，显示一张图片
     */
    private static DraggableImageInfo createImageDraggableParamsWithWHRadio(View view, String originUrl, String thumbUrl, long imgSize) {
        DraggableImageInfo draggableInfo;
        if (view != null) {
            int[] location = new int[2];
            view.getLocationInWindow(location);
            Rect windowRect = new Rect();
            view.getWindowVisibleDisplayFrame(windowRect);
            int top = location[1];
            draggableInfo = new DraggableImageInfo(originUrl,
                    thumbUrl,
                    new DraggableParamsInfo(
                            location[0],
                            top,
                            view.getWidth(),
                            view.getHeight()
                    ),
                    imgSize);
        } else {
            draggableInfo = new DraggableImageInfo(originUrl, thumbUrl, imgSize);
        }
        draggableInfo.adjustImageUrl();
        return draggableInfo;
    }
}
