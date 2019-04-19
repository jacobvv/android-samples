package org.jacobvv.androidsamples.recycler.cursor.model;

import android.net.Uri;

/**
 * @author jacob
 * @date 19-4-20
 */
public class Image {
    public String name;
    public String path;
    public Uri uri;

    public Image(String name, String path, Uri uri) {
        this.name = name;
        this.path = path;
        this.uri = uri;
    }
}
