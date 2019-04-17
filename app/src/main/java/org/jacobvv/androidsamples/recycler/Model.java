package org.jacobvv.androidsamples.recycler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jacob
 * @date 19-4-17
 */
public class Model {
    private static int sCount = 0;
    private static String[] TITLES = {
            "Android",
            "Mobile operating system",
            "Java (programming language)",
            "Open-source software",
            "Mobile app"
    };
    private static String[] CONTENTS = {
            "Android is a mobile operating system developed by Google.",
            "A mobile operating system (or mobile OS) is an operating...",
            "Java is a general-purpose programming language that is class...",
            "Open-source software (OSS) is a type of computer software in...",
            "A mobile app or mobile application is a computer program or..."
    };

    public String img;
    public String title;
    public String content;

    private Model(String img, String title, String content) {
        this.img = img;
        this.title = title;
        this.content = content;
    }

    public static Collection<Model> buildList(int count) {
        List<Model> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(new Model(String.valueOf(TITLES[sCount % 5].charAt(0)),
                    sCount + ". " + TITLES[sCount % 5], CONTENTS[sCount % 5]));
            sCount++;
        }
        return result;
    }

    public static Model buildItem() {
        Model model = new Model(String.valueOf(TITLES[sCount % 5].charAt(0)),
                sCount + ". " + TITLES[sCount % 5], CONTENTS[sCount % 5]);
        sCount++;
        return model;
    }
}
