package me.home.bangumi.widget.commonAdapter;

import me.drakeet.multitype.Item;


public class TitleItem implements Item {

    public String title;
    public int titleImageRes;

    public TitleItem(String title, int titleImageRes) {
        this.title = title;
        this.titleImageRes = titleImageRes;
    }
}
