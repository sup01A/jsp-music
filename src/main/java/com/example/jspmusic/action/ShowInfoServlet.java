package com.example.jspmusic.action;

import com.example.jspmusic.utils.MybatisUtils;
import com.example.jspmusic.vo.NewSong;
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

@WebServlet("/ShowInfoServlet")
public class ShowInfoServlet extends HttpServlet {
    String info = "";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        info = req.getParameter("info");

        // 跳转到首页
        if (info.equals("index")){
            this.to_index(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
    // 跳转到首页
    protected void to_index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SqlSession session = MybatisUtils.getSession();
        List<Recommend> recommends = session.selectList("mapper.RecommendMapper.findAll");
        List<NewSong> newSongs = session.selectList("mapper.NewSongMapper.findAll");
        List<Song> downloadRank = session.selectList("mapper.SongMapper.downloadRank");
        session.close();

        request.setAttribute("recommends", recommends);
        request.setAttribute("newSongs", newSongs);
        request.setAttribute("dRank", downloadRank);
        request.getRequestDispatcher("main.jsp").forward(request, response);
    }
}
