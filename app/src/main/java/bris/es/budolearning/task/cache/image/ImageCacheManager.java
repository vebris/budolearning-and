package bris.es.budolearning.task.cache.image;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;

import java.io.File;

import bris.es.budolearning.task.volley.VolleyControler;
import bris.es.budolearning.utiles.Utiles;

/**
 * Implementation of volley's ImageCache interface. This manager tracks the application image loader and cache.
 *
 * Volley recommends an L1 non-blocking cache which is the default MEMORY CacheType.
 * @author Trey Robinson
 *
 */
public class ImageCacheManager{

    /**
     * Volley recommends in-memory L1 cache but both a disk and memory cache are provided.
     * Volley includes a L2 disk cache out of the box but you can technically use a disk cache as an L1 cache provided
     * you can live with potential i/o blocking.
     *
     */
    public enum CacheType {
        DISK
        , MEMORY
    }

    private static ImageCacheManager mInstance;

    /**
     * Volley image loader
     */
    private ImageLoader mImageLoader;

    /**
     * Image cache implementation
     */
    private ImageCache mImageCache;

    /**
     * @return
     * 		instance of the cache manager
     */
    public static ImageCacheManager getInstance(){
        if(mInstance == null)
            mInstance = new ImageCacheManager();

        return mInstance;
    }

    /**
     * Initializer for the manager. Must be called prior to use.
     * @param cacheSize max size for the cache
     * @param compressFormat file type compression format.
     * @param quality image quality
     */
    public void init(File directory, int cacheSize, CompressFormat compressFormat, int quality, CacheType type){
        switch (type) {
            case DISK:
                mImageCache= new ImageCacheLruDisk(directory, cacheSize, compressFormat, quality);
                break;
            case MEMORY:
                mImageCache = new ImageCacheLruBitmap(cacheSize);
            default:
                mImageCache = new ImageCacheLruBitmap(cacheSize);
                break;
        }

        mImageLoader = new ImageLoader(VolleyControler.getInstance().getRequestQueue(), mImageCache);
    }

    public Bitmap getBitmap(String url) {
        try {
            return mImageCache.getBitmap(createKey(url));
        } catch (NullPointerException e) {
            throw new IllegalStateException("Disk Cache Not initialized");
        }
    }

    public void putBitmap(String url, Bitmap bitmap) {
        try {
            mImageCache.putBitmap(createKey(url), bitmap);
        } catch (NullPointerException e) {
            throw new IllegalStateException("Disk Cache Not initialized");
        }
    }

    public void clearCache(){
        if(mImageCache instanceof ImageCacheLruBitmap) {
            ((ImageCacheLruBitmap) mImageCache).evictAll();
        }
        if(mImageCache instanceof ImageCacheLruDisk) {
            ((ImageCacheLruDisk) mImageCache).clearCache();
        }
    }

    public long size(){
        if(mImageCache instanceof ImageCacheLruBitmap) {
            return ((ImageCacheLruBitmap) mImageCache).size();
        }
        if(mImageCache instanceof ImageCacheLruDisk) {
            return ((ImageCacheLruDisk) mImageCache).size();
        }
        return 0;
    }

    /**
     * 	Executes and image load
     * @param url location of image
     * @param listener Listener for completion
     */
    public void getImage(String url, ImageListener listener){
        mImageLoader.get(url, listener);
    }

    /**
     * @return
     * 		instance of the image loader
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    /**
     * Creates a unique cache key based on a url value
     * @param str url to be used in key creation
     * @return cache key value
     */
    public String createKey(String str) {
        String tmp = "";
        if(str != null && str.length() > 0) {
            try {
                tmp = new String(Base64.encode(str.getBytes(), Base64.DEFAULT)).trim();
            } catch(Throwable e) {
                e.printStackTrace();
            }
        }
        return tmp;
    }

}