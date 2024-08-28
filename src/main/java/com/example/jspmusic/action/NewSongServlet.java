package com.example.jspmusic.action;

import com.example.jspmusic.utils.MybatisUtils;
import com.example.jspmusic.vo.NewSong;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/NewSongServlet")
public class NewSongServlet extends HttpServlet {
    private String info = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        info = request.getParameter("info");

        // 添加推荐歌曲
        if (info.equals("add")){
            this.add(request, response);
        }
        // 移除推荐歌曲
        if (info.equals("remove")){
            this.remove(request, response);
        }
        // 根据页数查看所有歌曲
        if (info.equals("find")){
            this.findAll(request, response);
        }
    }
    protected void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SqlSession session = MybatisUtils.getSession();
        List<NewSong> newSongs = session.selectList("mapper.NewSongMapper.findAll");
        session.close();
        request.setAttribute("newSongs", newSongs);
        request.getRequestDispatcher("admin/newSong.jsp").forward(request, response);
    }
    protected void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int songId = Integer.parseInt(request.getParameter("songId"));
        SqlSession session = MybatisUtils.getSession();
        int insert = session.insert("mapper.NewSongMapper.add", songId);
        session.commit();
        session.close();

        if (insert == 1){
            request.setAttribute("message", "添加歌曲成功！");
        } else {
            request.setAttribute("message", "添加歌曲失败！");
        }
        request.setAttribute("flag", true);
        request.getRequestDispatcher("admin/newSong.jsp").forward(request, response);
    }
    protected void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int newSongId = Integer.parseInt(request.getParameter("newSongId"));
        SqlSession session = MybatisUtils.getSession();
        int delete = session.delete("mapper.NewSongMapper.remove", newSongId);
        session.commit();
        session.close();

        if (delete == 1){
            request.setAttribute("message", "移除成功！");
        } else {
            request.setAttribute("message", "移除失败！");
        }
        request.setAttribute("flag", true);
        request.getRequestDispatcher("admin/newSong.jsp").forward(request, response);
    }
}
