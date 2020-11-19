package lib.interview.java.java8;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:sjy
 * @date: 2020/11/18
 * Description 艺术家
 */
public class Art {
    String name;
    List<ArtDemo> artList;//艺术作品


    public Art(String name, List<ArtDemo> artList) {
        this.name = name;
        this.artList = artList;
    }

    public Art() {
        this.name = "默认";
        this.artList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public static String getName2() {
        return "默认2";
    }

    public List<ArtDemo> getArtList() {
        return artList;
    }

    static class ArtDemo {
        String artName;
        int artTime;
        boolean isSingle;

        public ArtDemo(String artName, int artTime, boolean isSingle) {
            this.artName = artName;
            this.artTime = artTime;
            this.isSingle = isSingle;
        }

    }
}
