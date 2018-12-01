package me.ewriter.bangumitv.ui.persons.adapter;

import java.util.List;

import me.drakeet.multitype.Item;


public class PersonItemList implements Item{

    public List<PersonItem> mList;

    public PersonItemList(List<PersonItem> mList) {
        this.mList = mList;
    }
}
