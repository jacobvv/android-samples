package org.jacobvv.androidsamples.recycler.multi.model;

import org.jacobvv.androidsamples.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jacob
 * @date 19-4-18
 */
public class Group {

    public List<Item> list;

    private Group(int count) {
        this.list = new ArrayList<>(Item.buildList(count));
    }

    public static Group buildItem(int count) {
        return new Group(count);
    }

    public static class Item {

        private static int sCount = 0;

        public int image;
        public String title;

        Item(int image, String title) {
            this.image = image;
            this.title = title;
        }

        static Collection<Item> buildList(int count) {
            List<Item> result = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                result.add(new Item(Constants.ICONS[sCount % 9], String.valueOf(sCount)));
                sCount++;
            }
            return result;
        }

        static Item buildItem() {
            Item model = new Item(Constants.ICONS[sCount % 9], String.valueOf(sCount));
            sCount++;
            return model;
        }
    }
}
