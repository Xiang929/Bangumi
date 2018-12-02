package me.home.bangumi.widget.commonAdapter;

import me.drakeet.multitype.Item;

/**
 * 用于显示网页上左侧的信息
 */

public class TextItem implements Item {
    public String text;

    public TextItem(String text) {
        this.text = text;
    }
}
