package jetpack.lym.org.dragimageview.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.SafeKeyGenerator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

/**
 * author: ym.li
 * since: 2019/9/8
 */
public final class GlideHelper {

    public static void retrieveImageWhRadioFromMemoryCache(Context context, final String thumbnailImg, final GlideCallBack<Float> retrieveCallBack) {
        Glide.with(context).load(thumbnailImg).apply((new RequestOptions()).onlyRetrieveFromCache(true)).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (resource.getIntrinsicWidth() > 0 && resource.getIntrinsicHeight() > 0) {
                    retrieveCallBack.onCallBack(resource.getIntrinsicWidth() * 1f / resource.getIntrinsicHeight());
                } else {
                    retrieveCallBack.onCallBack(-1F);
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                retrieveCallBack.onCallBack(-1F);
            }
        });
    }

    public static void checkImageIsInMemoryCache(Context context, String url, final GlideCallBack<Boolean> callback) {
        Glide.with(context).load(url).apply((new RequestOptions()).onlyRetrieveFromCache(true)).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition transition) {
                callback.onCallBack(true);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                callback.onCallBack(false);
            }
        });
    }

    public static boolean imageIsInCache(Context context, String url) {
        if (null == url || url.length() == 0) {
            return false;
        } else {
            try {
                SafeKeyGenerator safeKeyGenerator = new SafeKeyGenerator();
                String safeKey = safeKeyGenerator.getSafeKey(new GlideUrl(url));
                File file = new File(context.getCacheDir(), "image_manager_disk_cache");
                DiskLruCache diskLruCache = DiskLruCache.open(file, 1, 1, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
                DiskLruCache.Value value = diskLruCache.get(safeKey);
                if (value != null && value.getFile(0).exists()) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public interface GlideCallBack<T> {
        void onCallBack(T radio);
    }
}
