package com.example.jspmusic.action;

import com.example.jspmusic.utils.MybatisUtils;
import com.example.jspmusic.vo.Recommend;
import com.example.jspmusic.vo.Song;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/RecommendServlet")
public class RecommendServlet extends HttpServlet {
    private String info = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        info = req.getParameter("info");
        // 添加推荐歌曲
        if (info.equals("add")){
            this.add(req, resp);
        }
        // 移除推荐歌曲
        if (info.equals("remove")){
            this.remove(req, resp);
        }
        // 根据页数查看所有歌曲
        if (info.equals("find")){
            this.findAll(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
    // 根据页数查看所有歌曲
    protected void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SqlSession session = MybatisUtils.getSession();
        List<Recommend> recommends = session.selectList("mapper.RecommendMapper.findAll");
        session.close();
//        List<Recommend> recommends = recommendDao.findAll();

        // 返回结果到页面
        request.setAttribute("recommends", recommends);
        request.getRequestDispatcher("admin/recommend.jsp").forward(request, response);
    }
    // 添加推荐歌曲
    protected void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int songId = Integer.parseInt(request.getParameter("songId"));
        SqlSession session = MybatisUtils.getSession();
        int insert = session.insert("mapper.RecommendMapper.add", songId);
        session.commit();
        session.close();

        if (insert == 1){
            request.setAttribute("message", "推荐歌曲成功！");
        } else {
            request.setAttribute("message", "推荐歌曲失败！");
        }
        request.setAttribute("flag", true);
        request.getRequestDispatcher("admin/recommend.jsp").forward(request, response);
    }// 移除推荐歌曲
    protected void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int recmdId = Integer.parseInt(request.getParameter("recmdId"));
        SqlSession session = MybatisUtils.getSession();
        int delete = session.delete("mapper.RecommendMapper.remove", recmdId);
        session.commit();
        session.close();

        if (delete == 1){
            request.setAttribute("message", "移除成功！");
        } else {
            request.setAttribute("message", "移除失败！");
        }
        request.setAttribute("flag", true);
        request.getRequestDispatcher("admin/recommend.jsp").forward(request, response);
    }

}
