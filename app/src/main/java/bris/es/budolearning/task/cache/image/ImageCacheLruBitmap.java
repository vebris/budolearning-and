package bris.es.budolearning.task.cache.image;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import com.android.volley.toolbox.ImageLoader.ImageCache;



/**
 * Basic LRU Memory cache.
 *
 * @author Trey Robinson
 *
 */
public class ImageCacheLruBitmap extends LruCache<String, Bitmap> implements ImageCache{

    private final String TAG = this.getClass().getSimpleName();

    public ImageCacheLruBitmap(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    public Bitmap getBitmap(String url) {
        Log.v(TAG, "Retrieved item from Mem Cache");
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        Log.v(TAG, "Added item to Mem Cache");
        put(url, bitmap);
    }
}