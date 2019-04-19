package org.jacobvv.androidsamples.recycler.multi.model;

import android.support.annotation.IdRes;

import org.jacobvv.androidsamples.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jacob
 * @date 19-4-18
 */
public class Banner {

    private static int sCount = 0;

    public int banner;

    private Banner(@IdRes int banner) {
        this.banner = banner;
    }

    public static Collection<Banner> buildList(int count) {
        List<Banner> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(new Banner(Constants.BANNERS[sCount % 4]));
            sCount++;
        }
        return result;
    }

    public static Banner buildItem() {
        Banner model = new Banner(Constants.BANNERS[sCount % 4]);
        sCount++;
        return model;
    }
}
