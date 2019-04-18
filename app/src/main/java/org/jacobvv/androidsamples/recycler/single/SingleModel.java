package org.jacobvv.androidsamples.recycler.single;

import android.support.annotation.IdRes;

import org.jacobvv.androidsamples.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jacob
 * @date 19-4-17
 */
class SingleModel {
    private static int sCount = 0;

    int icon;
    String title;
    String content;

    private SingleModel(@IdRes int icon, String title, String content) {
        this.icon = icon;
        this.title = title;
        this.content = content;
    }

    static Collection<SingleModel> buildList(int count) {
        List<SingleModel> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(new SingleModel(Constants.ICONS[sCount % 9],
                    sCount + ". " + Constants.TITLES[sCount % 5],
                    Constants.CONTENTS[sCount % 5]));
            sCount++;
        }
        return result;
    }

    static SingleModel buildItem() {
        SingleModel model = new SingleModel(Constants.ICONS[sCount % 9],
                sCount + ". " + Constants.TITLES[sCount % 5],
                Constants.CONTENTS[sCount % 5]);
        sCount++;
        return model;
    }
}
