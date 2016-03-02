package bris.es.budolearning.task.volley;

import android.app.Application;
import android.graphics.Bitmap;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

import bris.es.budolearning.utiles.Constants;
import bris.es.budolearning.utiles.Utiles;
import bris.es.budolearning.task.cache.image.ImageCacheManager;

public class VolleyControler extends Application {

    public static final String TAG = VolleyControler.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private DiskBasedCache cache;
    private static VolleyControler mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public void createImageCache() {
        int sizeImages = Utiles.getConfiguracion(getApplicationContext()).getTamanoCacheImagenes() * 1024 * 1024;
        if(sizeImages < 0) sizeImages = Integer.MAX_VALUE;

        ImageCacheManager.getInstance().init(
                Utiles.getDirectorioCacheImagenes()
                , sizeImages
                , Constants.DISK_IMAGECACHE_COMPRESS_FORMAT
                , Constants.DISK_IMAGECACHE_QUALITY
                , ImageCacheManager.CacheType.DISK);
    }


    public static synchronized VolleyControler getInstance() {
        return mInstance;
    }

    public boolean initializeQueue(){
        return createQueue() != null;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = createQueue();
        }
        return mRequestQueue;
    }
    private RequestQueue createQueue(){
        // Instantiate the cache
        int sizeCache = Utiles.getConfiguracion(getApplicationContext()).getTamanoCachePeticiones() * 1024 * 1024;
        if(sizeCache < 0) sizeCache = Integer.MAX_VALUE;
        cache = new DiskBasedCache(Utiles.getDirectorioCache(), sizeCache);

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
        // Start the queue
        mRequestQueue.start();
        //mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            createImageCache();
            mImageLoader = ImageCacheManager.getInstance().getImageLoader();
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setRetryPolicy(new DefaultRetryPolicy(
                7 * 24 * 60 * 1000, // 1 semana
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void clearImages(){
        if (mRequestQueue != null) {
            ImageCacheManager.getInstance().clearCache();
            createImageCache();
        }
    }
    public void clearCache(){
        if (mRequestQueue != null) {
            cache.clear();
            cache.initialize();
        }
    }

    public DiskBasedCache getCache() {
        return cache;
    }
    public void setCache(DiskBasedCache cache) {
        this.cache = cache;
    }

}