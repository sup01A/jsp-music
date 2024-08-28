package com.example.jspmusic.action;

import com.example.jspmusic.Constant;
import com.example.jspmusic.utils.MybatisUtils;
import com.example.jspmusic.vo.NormalUser;
import com.jspsmart.upload.File;
import com.jspsmart.upload.Files;
import com.jspsmart.upload.SmartUpload;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/NormalUserServlet")
public class NormalUserServlet extends HttpServlet {
    private ServletConfig servletConfig;
    private String info = "";

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.servletConfig = config;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        info = req.getParameter("info");

        // 普通用户登录
        if (info.equals("login")){
            this.user_login(req, resp);
        }
        // 普通用户管理
        if (info.equals("manager")){
            this.user_manager(req, resp);
        }
        // 用户基本设置
        if (info.equals("set")){
            this.user_set(req, resp);
        }
        // 修改头像
        if (info.equals("avatar")){
            this.user_avatar(req, resp);
        }
        // 修改密码
        if (info.equals("psw")){
            this.user_psw(req, resp);
        }
        // 普通用户注销
        if (info.equals("logout")){
            this.user_logout(req, resp);
        }
        //管理员封禁用户
        if (info.equals("ban")){
            this.user_ban(req, resp);
        }
        // 普通用户注册
        if (info.equals("signup")){
            this.user_signup(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
    // 普通用户登录
    protected void user_login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");

        NormalUser userData = new NormalUser();
        userData.setUserName(userName);
        userData.setUserPassword(userPassword);

        SqlSession session1 = MybatisUtils.getSession();
        NormalUser user = session1.selectOne("mapper.NormalUserMapper.getAllUser",userData);
        if (user != null){
            if (user.getUserStatus() == 0){
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("login_flag", Constant.LOGIN_SUCCESS);
                request.getRequestDispatcher("index.jsp").forward(request, response);
            } else {
                request.setAttribute("message", "该用户已被封禁！");
                request.setAttribute("flag", true);
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("message", "用户名或密码输入错误！");
            request.setAttribute("flag", true);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    // 普通用户管理
    protected void user_manager(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
    // 用户基本设置
    protected void user_set(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userNickname = request.getParameter("userNickname");
        int userSex = Integer.parseInt(request.getParameter("sex"));
        int userId = Integer.parseInt(request.getParameter("userId"));
        NormalUser userData = new NormalUser();
        userData.setUserId(userId);
        userData.setUserNickname(userNickname);
        userData.setUserSex(userSex);
        SqlSession session = MybatisUtils.getSession();
        int update = session.update("mapper.NormalUserMapper.setting", userData);
        session.commit();
        NormalUser updateUser = session.selectOne("mapper.NormalUserMapper.afterSetting", userData.getUserId());
        session.close();
        request.getSession().setAttribute("user",updateUser);
        if (update == 1){
            request.setAttribute("message", "修改成功！");
        } else {
            request.setAttribute("message", "修改失败！");
        }
        request.setAttribute("flag", true);
        request.getRequestDispatcher("user/setting.jsp").forward(request, response);
    }
    protected void user_avatar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //新建一个SmartUpload对象
            SmartUpload su = new SmartUpload();
            //上传初始化
            su.initialize(servletConfig, request, response);
            //限制每个上传文件的最大长度10M
            su.setMaxFileSize(10*1024*1024);
            //限制总上传数据的长度
//			su.setTotalMaxFileSize(200000);
            //设定允许上传的文件（通过扩展名限制），仅允许jpg，png文件
//			su.setAllowedFilesList("jpg,png");
            //设定禁止上传的文件（通过扩展名限制），禁止上传带有exe,bat,jsp,htm,html扩展名的文件和没有扩展名的文件
            su.setDeniedFilesList("exe,bat,jsp,htm,html");
            //上传文件
            su.upload();
            //获取上传的文件操作
            Files files = su.getFiles();
            //获取单个文件
            File singleFile = files.getFile(0);
            //获取上传文件的扩展名
            String fileType = singleFile.getFileExt();
            //设置上传文件的扩展名
            String[] type = {"JPG","jpg"};
            // 判断上传文件的类型是否正确
            int place = java.util.Arrays.binarySearch(type, fileType);
            //判断文件扩展名是否正确
            if (place != -1){
                //判断该文件是否被选择
                if (!singleFile.isMissing()){

                    //以系统时间作为上传文件名称，设置上传完整路径
                    String fileName = String.valueOf(System.currentTimeMillis());
                    String filedir = Constant.DEFAULT_AVATAR_SECPATH + fileName + "." + singleFile.getFileExt();
                    // 带后缀名保存与数据库中
                    String saveFileName = fileName + "." + singleFile.getFileExt();

                    //执行上传操作
                    singleFile.saveAs(filedir, File.SAVEAS_VIRTUAL);


                    int userId = Integer.parseInt(su.getRequest().getParameter("userId"));

                    NormalUser userData = new NormalUser();
                    userData.setUserId(userId);
                    userData.setUserAvatar(saveFileName);
                    //数据库执行更新操作
                    SqlSession session = MybatisUtils.getSession();
                    int update = session.update("mapper.NormalUserMapper.save_avatar", userData);
                    session.commit();
                    //更新后返回数据用于更新个人页面信息
                    NormalUser normalUserAfterUpdate = session.selectOne("mapper.NormalUserMapper.afterSetting", userData.getUserId());
                    request.getSession().setAttribute("user",normalUserAfterUpdate);
                    session.close();
                    if (update == 1){
                        request.setAttribute("message", "修改头像成功！");
                    } else {
                        request.setAttribute("message", "修改头像失败！");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("修改头像Servlet异常！", e);
        }
        request.setAttribute("flag", true);
        request.getRequestDispatcher("user/avatar.jsp").forward(request, response);
    }
    protected void user_psw(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String newPassword = request.getParameter("newPassword");
        String password = request.getParameter("password");
        int userId = Integer.parseInt(request.getParameter("userId"));
        String userName = request.getParameter("userName");

        NormalUser userLoginData = new NormalUser();
        userLoginData.setUserPassword(password);
        userLoginData.setUserName(userName);
        SqlSession session = MybatisUtils.getSession();
        NormalUser user = session.selectOne("mapper.NormalUserMapper.getAllUser", userLoginData);
//        NormalUser user = normalUserDao.login(userLoginData);
        if (user != null){
            // 原密码正确
            NormalUser userData = new NormalUser();
            userData.setUserId(userId);
            userData.setUserPassword(newPassword);
            int update = session.update("mapper.NormalUserMapper.save_psw", userData);
            session.commit();
            session.close();
            if (update == 1){
                request.setAttribute("message", "修改密码成功！");
            } else {
                request.setAttribute("message", "修改密码失败！");
            }
        } else {
            request.setAttribute("message", "原密码错误！");
        }
        request.setAttribute("flag", true);
        request.getRequestDispatcher("user/psw.jsp").forward(request, response);
    }
    // 普通用户注销
    protected void user_logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        session.setAttribute("login_flag", Constant.LOGIN_FAILURE);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
    //管理员封禁普通用户
    protected void user_ban(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        NormalUser userData = new NormalUser();
        userData.setUserId(userId);
        SqlSession session = MybatisUtils.getSession();
        int update = session.update("mapper.NormalUserMapper.ban", userData);
        session.commit();
        session.close();
        if (update == 1){
            request.setAttribute("message", "封禁成功！");
        } else {
            request.setAttribute("message", "封禁失败！");
        }
        request.setAttribute("flag", true);
        request.getRequestDispatcher("admin/ban.jsp").forward(request, response);
    }
    // 普通用户注册
    protected void user_signup(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        String userEmail = request.getParameter("userEmail");

        NormalUser user = new NormalUser();
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setUserNickname(userName);
        user.setUserSex(Constant.SEX_DEFAULT);
        user.setUserEmail(userEmail);
        user.setUserAvatar(Constant.DEFAULT_AVATAR);
        user.setUserRegisterDate(Constant.DEFAULT_DATE);
        user.setUserLastDate(Constant.DEFAULT_DATE);
        user.setUserStatus(Constant.USER_STATUS_NORMAL);
        SqlSession session = MybatisUtils.getSession();
        Object o = session.selectOne("mapper.NormalUserMapper.existUsername", user.getUserName());
        if (o!= null){
            request.setAttribute("message",  "用户名已存在！");
            request.setAttribute("flag", true);
            session.close();
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }else {
            int insert = session.insert("mapper.NormalUserMapper.addNormalUser",user);
            session.commit();
            session.close();
            request.setAttribute("message", insert == 1 ? "注册成功！" : "注册失败！");
            request.setAttribute("flag", true);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }

    }

}
