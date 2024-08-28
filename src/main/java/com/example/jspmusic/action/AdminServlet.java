package com.example.jspmusic.action;

import com.example.jspmusic.Constant;
import com.example.jspmusic.utils.MybatisUtils;
import com.example.jspmusic.vo.Admin;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
    private String info = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 根据info参数不同的值，执行不同的操作
        info = req.getParameter("info");
        // 登录
        if (info.equals("login")){
            this.admin_login(req, resp);
        }
        // 注销
        if (info.equals("logout")){
            this.admin_logout(req, resp);
        }
        // 添加管理员
        if (info.equals("add")){
            this.admin_add(req, resp);
        }
        // 修改密码
        if (info.equals("psw")){
            this.admin_modifyPsw(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    /**
     * 管理员登录操作
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void admin_login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Admin adminData = new Admin();
        adminData.setAdminUsername(username);
        adminData.setAdminPassword(password);

        SqlSession session1 = MybatisUtils.getSession();
        Admin admin = session1.selectOne("mapper.AdminMapper.login", adminData);
        session1.close();
        if (admin != null){
//			request.setAttribute("admin", admin);
//			request.setAttribute("login_flag", Constant.LOGIN_SUCCESS);
            HttpSession session = request.getSession();
            session.setAttribute("admin", admin);
            session.setAttribute("admin_login_flag", Constant.LOGIN_SUCCESS);
            request.getRequestDispatcher("manager.jsp").forward(request, response);

        }
    }
    /**
     * 管理员注销操作
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void admin_logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("admin");
        session.setAttribute("admin_login_flag", Constant.LOGIN_FAILURE);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
    public void admin_add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Date register = new Date();
        Date lastDate = new Date();

        Admin adminData = new Admin();
        adminData.setAdminUsername(username);
        adminData.setAdminPassword(password);
        adminData.setAdminRegisterDate(register);
        adminData.setAdminLastDate(lastDate);

        SqlSession session = MybatisUtils.getSession();
        int insert = session.insert("mapper.AdminMapper.save", adminData);
        session.commit();
        session.close();
        request.setAttribute("message", insert == 1 ? "添加成功！" : "添加失败！");
        request.setAttribute("flag", true);
        request.getRequestDispatcher("admin/newAdmin.jsp").forward(request, response);
    }
    /**
     * 修改管理员密码
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void admin_modifyPsw(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String newPassword = request.getParameter("newPassword");

        Admin adminData = new Admin();
        adminData.setAdminUsername(username);
        adminData.setAdminPassword(password);

        // 检查密码是否正确
        SqlSession session = MybatisUtils.getSession();
        Admin admin = session.selectOne("mapper.AdminMapper.login", adminData);
        if (admin != null){
            admin.setAdminPassword(newPassword);
            int update = session.update("mapper.AdminMapper.update", admin);
            session.commit();
            session.close();
            if (update == 1){
                request.setAttribute("message", "修改密码成功！");
            } else {
                request.setAttribute("message", "修改密码失败！");
            }
        } else {
            request.setAttribute("message", "修改密码失败！");
        }
        request.setAttribute("flag", true);
        request.getRequestDispatcher("admin/psw.jsp").forward(request, response);
    }

}
