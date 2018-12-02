package me.home.bangumi.api.entity;

/**
 * 单个标签实体,动画页详情使用，暂不知漫画，游戏详情页能否使用
 */
public class TagEntity {

    String tagName;
    String tagUrl;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        tagName = tagName;
    }

    public String getTagUrl() {
        return tagUrl;
    }

    public void setTagUrl(String tagUrl) {
        tagUrl = tagUrl;
    }
}
