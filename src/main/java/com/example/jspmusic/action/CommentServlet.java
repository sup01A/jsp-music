package com.example.jspmusic.action;

import com.example.jspmusic.utils.MybatisUtils;
import com.example.jspmusic.vo.Comments;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet("/CommentsServlet")
public class CommentServlet extends HttpServlet {
    private String info = "";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        info = req.getParameter("info");

        if (info.equals("add")){
            this.comment_Add(req, resp);
        }
    }
    protected void comment_Add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commentText = request.getParameter("commentText");
        int userId = Integer.parseInt(request.getParameter("userId"));
        int songId = Integer.parseInt(request.getParameter("songId"));

        Comments commentsData = new Comments();
        commentsData.setCommentText(commentText);
        commentsData.setUserId(userId);
        commentsData.setSongId(songId);
        commentsData.setCommentDate(new Date());
        System.out.println(commentsData.toString());
        SqlSession session = MybatisUtils.getSession();
        int insert = session.insert("mapper.NormalUserMapper.saveComment", commentsData);
        session.commit();
        session.close();
        if (insert == 1){
            request.setAttribute("message", "评论成功");
        } else {
            request.setAttribute("message", "评论失败");
        }
        request.setAttribute("flag", true);
        request.getRequestDispatcher("SongServlet?info=play&playId=" + songId).forward(request, response);
    }
}
