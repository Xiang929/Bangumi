package me.home.bangumi.api.response;

import java.util.List;

public class BgmListItem {
    private String titleCN;
    private String titleJP;
    private String titleEN;
    private String officalSite;
    private int weekDayJp;
    private int weekdayCN;
    private String timeJP;
    private String timeCN;
    private Boolean newBgm;
    private int bgmId;
    private String showDate;
    private List<String> onAirSite;

    public BgmListItem(String titleCN, String titleJP, String titleEN, String officalSite, int weekDayJp, int weekdayCN, String timeJP, String timeCN, Boolean newBgm, int bgmId, String showDate, List<String> onAirSite) {
        this.titleCN = titleCN;
        this.titleJP = titleJP;
        this.titleEN = titleEN;
        this.officalSite = officalSite;
        this.weekDayJp = weekDayJp;
        this.weekdayCN = weekdayCN;
        this.timeJP = timeJP;
        this.timeCN = timeCN;
        this.newBgm = newBgm;
        this.bgmId = bgmId;
        this.showDate = showDate;
        this.onAirSite = onAirSite;
    }

    public String getTitleCN() {
        return titleCN;
    }

    public String getTitleJP() {
        return titleJP;
    }

    public String getTitleEN() {
        return titleEN;
    }

    public String getOfficalSite() {
        return officalSite;
    }

    public int getWeekDayJp() {
        return weekDayJp;
    }

    public int getWeekdayCN() {
        return weekdayCN;
    }

    public String getTimeJP() {
        return timeJP;
    }

    public String getTimeCN() {
        return timeCN;
    }

    public Boolean getNewBgm() {
        return newBgm;
    }

    public int getBgmId() {
        return bgmId;
    }

    public String getShowDate() {
        return showDate;
    }

    public List<String> getOnAirSite() {
        return onAirSite;
    }

    public void setTitleCN(String titleCN) {
        this.titleCN = titleCN;
    }

    public void setTitleJP(String titleJP) {
        this.titleJP = titleJP;
    }

    public void setTitleEN(String titleEN) {
        this.titleEN = titleEN;
    }

    public void setOfficalSite(String officalSite) {
        this.officalSite = officalSite;
    }

    public void setWeekDayJp(int weekDayJp) {
        this.weekDayJp = weekDayJp;
    }

    public void setWeekdayCN(int weekdayCN) {
        this.weekdayCN = weekdayCN;
    }

    public void setTimeJP(String timeJP) {
        this.timeJP = timeJP;
    }

    public void setTimeCN(String timeCN) {
        this.timeCN = timeCN;
    }

    public void setNewBgm(Boolean newBgm) {
        this.newBgm = newBgm;
    }

    public void setBgmId(int bgmId) {
        this.bgmId = bgmId;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public void setOnAirSite(List<String> onAirSite) {
        this.onAirSite = onAirSite;
    }
}
