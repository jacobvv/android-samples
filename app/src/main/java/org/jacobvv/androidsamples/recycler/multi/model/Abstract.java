package org.jacobvv.androidsamples.recycler.multi.model;

import androidx.annotation.IdRes;

import org.jacobvv.androidsamples.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jacob
 * @date 19-4-18
 */
public class Abstract {

    private static int sCount = 0;

    public int image;
    public String title;
    public String content;

    private Abstract(@IdRes int image, String title, String content) {
        this.image = image;
        this.title = title;
        this.content = content;
    }

    public static Collection<Abstract> buildList(int count) {
        List<Abstract> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(new Abstract(Constants.ICONS[sCount % 9],
                    sCount + ". " + Constants.TITLES[sCount % 5],
                    Constants.CONTENTS[sCount % 5]));
            sCount++;
        }
        return result;
    }

    public static Abstract buildItem() {
        Abstract model = new Abstract(Constants.ICONS[sCount % 9],
                sCount + ". " + Constants.TITLES[sCount % 5],
                Constants.CONTENTS[sCount % 5]);
        sCount++;
        return model;
    }
}
