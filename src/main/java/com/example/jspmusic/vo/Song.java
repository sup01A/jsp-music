package com.example.jspmusic.vo;

import java.util.Map;

public class Song {
    int songId;			// 歌曲ID
    int singerId;		// 所属歌手ID
    int albumId;		// 所属专辑ID
    String songTitle;	// 歌曲名
    int songPlaytimes;	// 歌曲播放次数
    int songDldtimes;	// 歌曲下载次数
    String songFile;	// 歌曲文件名
    String songLyric;	// 歌曲歌词文件名

    // 数据库没有的对象
    Singer singer;		// 歌曲所属歌手对象

    // 构造方法
    public Song() {
    }

    public int getSongId() {
        return songId;
    }
    public void setSongId(int songId) {
        this.songId = songId;
    }
    public int getSingerId() {
        return singerId;
    }
    public void setSingerId(int singerId) {
        this.singerId = singerId;
    }
    public int getAlbumId() {
        return albumId;
    }
    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }
    public String getSongTitle() {
        return songTitle;
    }
    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }
    public int getSongPlaytimes() {
        return songPlaytimes;
    }
    public void setSongPlaytimes(int songPlaytimes) {
        this.songPlaytimes = songPlaytimes;
    }
    public int getSongDldtimes() {
        return songDldtimes;
    }
    public void setSongDldtimes(int songDldtimes) {
        this.songDldtimes = songDldtimes;
    }
    public String getSongFile() {
        return songFile;
    }
    public void setSongFile(String songFile) {
        this.songFile = songFile;
    }
    public Singer getSinger() {
        return singer;
    }
    public void setSinger(Singer singer) {
        this.singer = singer;
    }

    public void setSongLyric(String songLyric) {
        this.songLyric = songLyric;
    }

    public String getSongLyric() {
        return songLyric;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songId=" + songId +
                ", singerId=" + singerId +
                ", albumId=" + albumId +
                ", songTitle='" + songTitle + '\'' +
                ", songPlaytimes=" + songPlaytimes +
                ", songDldtimes=" + songDldtimes +
                ", songFile='" + songFile + '\'' +
                ", songLyric='" + songLyric + '\'' +
                ", singer=" + singer +
                '}';
    }
}
