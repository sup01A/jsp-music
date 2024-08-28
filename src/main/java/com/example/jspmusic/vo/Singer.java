package com.example.jspmusic.vo;

import java.util.Map;

public class Singer {
    int singerId;				// 歌手ID
    String singerName;			// 歌手名
    int singerSex;				// 歌手性别
    String singerThumbnail;		// 歌手图片
    String singerIntroduction;	// 歌手介绍

    // 构造方法
    public Singer() {
    }



    public int getSingerId() {
        return singerId;
    }
    public void setSingerId(int singerId) {
        this.singerId = singerId;
    }
    public String getSingerName() {
        return singerName;
    }
    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }
    public int getSingerSex() {
        return singerSex;
    }
    public void setSingerSex(int singerSex) {
        this.singerSex = singerSex;
    }
    public String getSingerThumbnail() {
        return singerThumbnail;
    }
    public void setSingerThumbnail(String singerThumbnail) {
        this.singerThumbnail = singerThumbnail;
    }
    public String getSingerIntroduction() {
        return singerIntroduction;
    }
    public void setSingerIntroduction(String singerIntroduction) {
        this.singerIntroduction = singerIntroduction;
    }


}
