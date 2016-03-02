package bris.es.budolearning.task.cache.image;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.jakewharton.disklrucache.DiskLruCache;

/**
 * Implementation of DiskLruCache by Jake Wharton
 * modified from http://stackoverflow.com/questions/10185898/using-disklrucache-in-android-4-0-does-not-provide-for-opencache-method
 */
public class ImageCacheLruDisk implements ImageCache {

    private DiskLruCache mDiskCache;
    private CompressFormat mCompressFormat = CompressFormat.JPEG;
    private static int IO_BUFFER_SIZE = 8 * 1024;
    private int mCompressQuality = 70;
    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;
    private int diskCacheSize;
    private File directory;

    public ImageCacheLruDisk(File directory, int diskCacheSize, CompressFormat compressFormat, int quality) {
        this.diskCacheSize = diskCacheSize;
        mCompressFormat = compressFormat;
        mCompressQuality = quality;
        this.directory = directory;
        createCache();
    }

    public void createCache() {
        try {
            mDiskCache = DiskLruCache.open(directory, APP_VERSION, VALUE_COUNT, diskCacheSize);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }

    }

    private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor )
            throws IOException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream( editor.newOutputStream( 0 ), IO_BUFFER_SIZE );
            return bitmap.compress( mCompressFormat, mCompressQuality, out );
        } finally {
            if ( out != null ) {
                out.close();
            }
        }
    }

    @Override
    public void putBitmap( String key, Bitmap data ) {
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskCache.edit(transformKey(key));
            if ( editor == null ) {
                return;
            }
            if( writeBitmapToFile( data, editor ) ) {
                mDiskCache.flush();
                editor.commit();
            } else {
                editor.abort();
            }
        } catch (IOException e) {
            try {
                if ( editor != null ) {
                    editor.abort();
                }
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public Bitmap getBitmap( String key ) {
        Bitmap bitmap = null;
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskCache.get(transformKey(key));
            if ( snapshot == null ) {
                return null;
            }
            final InputStream in = snapshot.getInputStream( 0 );
            if ( in != null ) {
                final BufferedInputStream buffIn =
                        new BufferedInputStream( in, IO_BUFFER_SIZE );
                bitmap = BitmapFactory.decodeStream( buffIn );
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            if ( snapshot != null ) {
                snapshot.close();
            }
        }
        return bitmap;
    }

    public boolean containsKey( String key ) {
        boolean contained = false;
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskCache.get(transformKey(key));
            contained = snapshot != null;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if ( snapshot != null ) {
                snapshot.close();
            }
        }
        return contained;
    }

    public void clearCache() {
        try {
            mDiskCache.delete();
            createCache();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public long size(){
        return mDiskCache.size();
    }

    public File getCacheFolder() {
        return mDiskCache.getDirectory();
    }
    /**
     * Creates a unique cache key based on a url value
     * @param key url to be used in key creation
     * @return cache key value
     */
    private String transformKey (String key){
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(key.getBytes(), 0, key.length());
        return new BigInteger(1, m.digest()).toString(16);
    }
}