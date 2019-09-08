package jetpack.lym.org.dragimageview.entities;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * author: ym.li
 * since: 2019/9/8
 */
public class DraggableImageInfo implements Serializable {
    private String originImg;
    private String thumbnailImg;
    private DraggableParamsInfo draggableInfo;
    private long imageSize;

    public DraggableImageInfo(String originImg, String thumbnailImg, DraggableParamsInfo draggableInfo, long imageSize) {
        this.originImg = originImg;
        this.thumbnailImg = thumbnailImg;
        this.draggableInfo = draggableInfo;
        this.imageSize = imageSize;
    }

    public DraggableImageInfo(String originUrl, String thumbUrl, long imgSize) {
        this.originImg = originUrl;
        this.thumbnailImg = thumbUrl;
        this.imageSize = imgSize;
    }

    public void adjustImageUrl() {
        if (!TextUtils.isEmpty(originImg) && !TextUtils.isEmpty(thumbnailImg)) {
            return;
        }
        if (TextUtils.isEmpty(originImg) && !TextUtils.isEmpty(thumbnailImg)) {
            originImg = thumbnailImg;
        } else {
            thumbnailImg = originImg;
        }
    }

    public String getOriginImg() {
        return originImg;
    }

    public void setOriginImg(String originImg) {
        this.originImg = originImg;
    }

    public String getThumbnailImg() {
        return thumbnailImg;
    }

    public void setThumbnailImg(String thumbnailImg) {
        this.thumbnailImg = thumbnailImg;
    }

    public DraggableParamsInfo getDraggableInfo() {
        return draggableInfo;
    }

    public void setDraggableInfo(DraggableParamsInfo draggableInfo) {
        this.draggableInfo = draggableInfo;
    }

    public long getImageSize() {
        return imageSize;
    }

    public void setImageSize(long imageSize) {
        this.imageSize = imageSize;
    }
}
