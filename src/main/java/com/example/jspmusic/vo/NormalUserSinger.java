package com.example.jspmusic.vo;

public class NormalUserSinger {
    int userId;		// 用户ID
    int singerId;	// 歌手ID

    // 数据库没有的字段
    Singer singer;

    // 构造方法
    public NormalUserSinger() {
    }


    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getSingerId() {
        return singerId;
    }
    public void setSingerId(int singerId) {
        this.singerId = singerId;
    }


    public Singer getSinger() {
        return singer;
    }


    public void setSinger(Singer singer) {
        this.singer = singer;
    }
}
