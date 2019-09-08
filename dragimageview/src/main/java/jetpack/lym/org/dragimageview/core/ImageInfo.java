package jetpack.lym.org.dragimageview.core;

/**
 * author: ym.li
 * since: 2019/9/8
 */
public class ImageInfo {
    private String thumbnailUrl;
    private String originUrl;
    private long imgSize;

    public ImageInfo(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public long getImgSize() {
        return imgSize;
    }

    public void setImgSize(long imgSize) {
        this.imgSize = imgSize;
    }
}
