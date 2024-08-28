package com.example.jspmusic.vo;

import com.example.jspmusic.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NewSongTest {
    @Test
    void test01() {
        SqlSession session = MybatisUtils.getSession();
        List<Object> objects = session.selectList("mapper.NewSongMapper.findAll");
        for (Object object : objects) {
            System.out.println(object);
        }
    }
}