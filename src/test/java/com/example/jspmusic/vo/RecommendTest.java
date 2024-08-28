package com.example.jspmusic.vo;

import com.example.jspmusic.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.List;


class RecommendTest {
    @Test
    void test01() {
        SqlSession session = MybatisUtils.getSession();
        List<Recommend> recommends = session.selectList("mapper.RecommendMapper.findAll");
        for (Recommend recommend : recommends) {
            System.out.println(recommend);
        }
    }
}