package me.home.bangumi.ui.bangumidetail.adapter;

import java.util.List;

import me.drakeet.multitype.Item;
import me.home.bangumi.api.entity.AnimeCharacterEntity;


public class DetailCharacterList implements Item {

    List<AnimeCharacterEntity> mList;

    public DetailCharacterList(List<AnimeCharacterEntity> mList) {
        this.mList = mList;
    }
}
