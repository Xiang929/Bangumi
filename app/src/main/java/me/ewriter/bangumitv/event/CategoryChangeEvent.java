package me.ewriter.bangumitv.event;


public class CategoryChangeEvent {
    private String category;

    public CategoryChangeEvent(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
