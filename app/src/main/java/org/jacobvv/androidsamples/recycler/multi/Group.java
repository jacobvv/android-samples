package org.jacobvv.androidsamples.recycler.multi;

import org.jacobvv.androidsamples.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jacob
 * @date 19-4-18
 */
public class Group {

    private List<Item> list;

    public Group(int count) {
        this.list = new ArrayList<>(Item.buildList(count));
    }

    private static class Item {

        private static int sCount = 0;

        int image;
        String title;

        Item(int image, String title) {
            this.image = image;
            this.title = title;
        }

        static Collection<Item> buildList(int count) {
            List<Item> result = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                result.add(new Item(Constants.ICONS[sCount % 9],
                        sCount + ". " + Constants.TITLES[sCount % 5]));
                sCount++;
            }
            return result;
        }

        static Item buildItem() {
            Item model = new Item(Constants.ICONS[sCount % 9],
                    sCount + ". " + Constants.TITLES[sCount % 5]);
            sCount++;
            return model;
        }
    }
}
