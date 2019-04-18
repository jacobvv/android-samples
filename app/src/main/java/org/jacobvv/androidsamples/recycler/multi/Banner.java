package org.jacobvv.androidsamples.recycler.multi;

import android.support.annotation.IdRes;

import org.jacobvv.androidsamples.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jacob
 * @date 19-4-18
 */
class Banner {

    private static int sCount = 0;

    private int banner;

    private Banner(@IdRes int banner) {
        this.banner = banner;
    }

    static Collection<Banner> buildList(int count) {
        List<Banner> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(new Banner(Constants.BANNERS[sCount % 4]));
            sCount++;
        }
        return result;
    }

    static Banner buildItem() {
        Banner model = new Banner(Constants.BANNERS[sCount % 4]);
        sCount++;
        return model;
    }
}
