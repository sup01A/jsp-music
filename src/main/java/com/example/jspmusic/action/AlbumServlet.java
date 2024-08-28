package com.example.jspmusic.action;

import com.example.jspmusic.Constant;
import com.example.jspmusic.utils.MybatisUtils;
import com.example.jspmusic.vo.*;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/AlbumServlet")
public class AlbumServlet extends HttpServlet {
    private ServletConfig servletConfig;
    private String info = "";

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.servletConfig = config;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        info = req.getParameter("info");
        // 跳转到所有专辑页面
        if (info.equals("list")){
            this.album_list(req, resp);
        }
        // 跳转到当前专辑页面
        if (info.equals("play")){
            this.album_play(req, resp);
        }
        // 跳转到关注管理页面
        if (info.equals("followmgr")){
            this.album_followmgr(req, resp);
        }
        // 收藏专辑
        if (info.equals("follow")){
            this.album_follow(req, resp);
        }
        // 取消关注
        if (info.equals("removeFollow")){
            this.remove_follow(req, resp);
        }
        // 根据页数查看所有专辑
        if (info.equals("find")){
            this.findByPage(req, resp);
        }
        // 添加专辑
        if (info.equals("add")){
            this.album_add(req, resp);
        }
        // 删除专辑
        if (info.equals("del")){
            this.album_del(req, resp);
        }
        // 修改专辑
        if (info.equals("update")){
            this.album_update(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    protected void album_list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SqlSession session = MybatisUtils.getSession();
        List<Album> albumList = session.selectList("mapper.AlbumMapper.findAllAlbums");
        session.close();
        request.setAttribute("albumList", albumList);
        request.getRequestDispatcher("albumlist.jsp").forward(request, response);
    }
    // 跳转到当前专辑页面
    protected void album_play(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int playId = Integer.parseInt(request.getParameter("playId"));
        Album albumData = new Album();
        albumData.setAlbumId(playId);
        SqlSession session = MybatisUtils.getSession();
        AlbumAllInfo albumAllInfo = session.selectOne("mapper.AlbumMapper.findAlbumInfo",albumData);
        List<Song> songs = session.selectList("mapper.SongMapper.AlbumAllInfo", albumData);
        session.close();
        albumAllInfo.setSongs(songs);

        request.setAttribute("albumInfo", albumAllInfo);
//        System.out.println(request.getSession().getAttribute("login_flag"));
//        System.out.println(request.getSession().getAttribute("admin_login_flag"));
        request.getRequestDispatcher("album.jsp").forward(request, response);
    }
    // 跳转到关注管理页面
    protected void album_followmgr(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        NormalUser user = (NormalUser) session.getAttribute("user");
        int userId = user.getUserId();
        NormalUserAlbum nus = new NormalUserAlbum();
        nus.setUserId(userId);
        SqlSession session1 = MybatisUtils.getSession();
        List<Album> albums = session1.selectList("mapper.NormalUserMapper.findAllAlbum", nus);
        session1.close();
//        List<NormalUserAlbum> list = normalUserAlbumDao.findAllAlbum(nus);
        request.setAttribute("list", albums);
        request.getRequestDispatcher("user/followAlbum.jsp").forward(request, response);
    }
    // 收藏专辑
    protected void album_follow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int albumId = Integer.parseInt(request.getParameter("albumId"));
        int userId = Integer.parseInt(request.getParameter("userId"));


        NormalUserAlbum nusData = new NormalUserAlbum();
        nusData.setAlbumId(albumId);
        nusData.setUserId(userId);
        SqlSession session = MybatisUtils.getSession();
        NormalUserAlbum isfollow = session.selectOne("mapper.NormalUserMapper.isfollowAlbum", nusData);
        // 判断是否已经收藏
        if (isfollow != null){
            int delete = session.delete("mapper.NormalUserMapper.deleteAlbum", nusData);
            session.commit();
            if (delete == 1){
                request.setAttribute("message", "取消收藏成功！");
            } else {
                request.setAttribute("message", "取消收藏失败！");
            }
        } else {
            int insert = session.insert("mapper.NormalUserMapper.saveAlbum", nusData);
            session.commit();
            if (insert == 1){
                request.setAttribute("message", "收藏成功！");
            } else {
                request.setAttribute("message", "收藏失败！");
            }
        }
        session.close();
        request.setAttribute("flag", true);
        request.getRequestDispatcher("AlbumServlet?info=play&playId=" + albumId).forward(request, response);
    }
    // 取消关注
    protected void remove_follow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int albumId = Integer.parseInt(request.getParameter("albumId"));
        HttpSession session = request.getSession();
        NormalUser user = (NormalUser) session.getAttribute("user");
        int userId = user.getUserId();
        NormalUserAlbum nuaData = new NormalUserAlbum();
        nuaData.setUserId(userId);
        nuaData.setAlbumId(albumId);
        SqlSession session1 = MybatisUtils.getSession();
        int delete = session1.delete("mapper.NormalUserMapper.deleteAlbum", nuaData);
        session1.commit();
        session1.close();
        if (delete != 1){
            request.setAttribute("message", "删除失败！");
            request.setAttribute("flag", true);
        }
        request.getRequestDispatcher("AlbumServlet?info=followmgr").forward(request, response);
    }
    // 根据页数查看所有专辑
    protected void findByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageNumStr = request.getParameter("pageNum");

        int pageNum = 1; //显示第几页数据
        if(pageNumStr!=null && !"".equals(pageNumStr.trim())){
            pageNum = Integer.parseInt(pageNumStr);
        }

        int pageSize = Constant.PAGE_SIZE;  // 每页显示多少条记录
        String pageSizeStr = request.getParameter("pageSize");
        if(pageSizeStr!=null && !"".equals(pageSizeStr.trim())){
            pageSize = Integer.parseInt(pageSizeStr);
        }
        //获取总记录数
        SqlSession session = MybatisUtils.getSession();
        Integer totalRecord = session.selectOne("mapper.AlbumMapper.totalNum");
        //获取总页数
        int totalPage = totalRecord / pageSize;
        if(totalRecord % pageSize !=0){
            totalPage++;
        }
        List<Album> albums = session.selectList("mapper.AlbumMapper.limitSinger", new Page((pageNum - 1) * pageSize, pageSize));
        List<Singer> singers = session.selectList("mapper.SingerMapper.findAllSingers");
        session.close();

        Pager<Album> result = new Pager<>(pageSize,pageNum,totalRecord,totalPage,albums);

        // 返回结果到页面
        request.setAttribute("result", result);
        request.setAttribute("singers", singers);
        request.getRequestDispatcher("admin/albummgr.jsp").forward(request, response);
    }
    // 添加专辑
    protected void album_add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //新建一个SmartUpload对象
            SmartUpload su = new SmartUpload();
            //上传初始化
            su.initialize(servletConfig, request, response);
            //限制每个上传文件的最大长度
            su.setMaxFileSize(1000000);
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
            String[] type = {"JPG","jpg","PNG","png"};
            // 判断上传文件的类型是否正确
            int place = java.util.Arrays.binarySearch(type, fileType);
            //判断文件扩展名是否正确
            if (place != -1){
                //判断该文件是否被选择
                if (!singleFile.isMissing()){
                    //以系统时间作为上传文件名称，设置上传完整路径
                    String fileName = String.valueOf(System.currentTimeMillis());
                    String filedir = Constant.ALBUM_PATH + fileName + "." + singleFile.getFileExt();
                    // 带后缀名保存与数据库中
                    String saveFileName = fileName + "." + singleFile.getFileExt();
                    //执行上传操作
                    singleFile.saveAs(filedir, File.SAVEAS_VIRTUAL);
                    String albumTitle = su.getRequest().getParameter("albumTitle");
                    int singerId = Integer.parseInt(su.getRequest().getParameter("singerId"));
                    String albumPubCom = su.getRequest().getParameter("albumPubCom");
                    // 获取日期
                    String albumPubDateString = su.getRequest().getParameter("albumPubDate");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date albumPubDate = sdf.parse(albumPubDateString);

                    Album albumData = new Album();
                    albumData.setAlbumTitle(albumTitle);
                    albumData.setSingerId(singerId);
                    albumData.setAlbumPubCom(albumPubCom);
                    albumData.setAlbumPubDate(albumPubDate);
                    albumData.setAlbumPic(saveFileName);
                    SqlSession session = MybatisUtils.getSession();
                    int insert = session.insert("mapper.AlbumMapper.save", albumData);
                    session.commit();
                    session.close();
                    if (insert == 1){
                        request.setAttribute("message", "成功添加专辑！");
                    } else {
                        request.setAttribute("message", "添加专辑失败！");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("添加专辑Servlet异常！", e);
        }
        request.setAttribute("flag", true);
        request.getRequestDispatcher("admin/albummgr.jsp").forward(request, response);
    }
    // 删除专辑
    protected void album_del(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int albumId = Integer.parseInt(request.getParameter("albumId"));

        Album albumData = new Album();
        albumData.setAlbumId(albumId);
        SqlSession session = MybatisUtils.getSession();
        int delete = session.delete("mapper.AlbumMapper.del", albumData);
        session.commit();
        session.close();
        if (delete == 1){
            request.setAttribute("message", "删除成功！");
        } else {
            request.setAttribute("message", "删除失败！");
        }
        request.setAttribute("flag", true);
        request.getRequestDispatcher("admin/albummgr.jsp").forward(request, response);
    }
    // 修改专辑
    protected void album_update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            String[] type = {"JPG","jpg","PNG","png"};
            // 判断上传文件的类型是否正确
            int place = java.util.Arrays.binarySearch(type, fileType);
            //判断文件扩展名是否正确
            if (place != -1){
                //判断该文件是否被选择
                if (!singleFile.isMissing()){

                    //以系统时间作为上传文件名称，设置上传完整路径
                    String fileName = String.valueOf(System.currentTimeMillis());
                    String filedir = Constant.ALBUM_PATH + fileName + "." + singleFile.getFileExt();
                    // 带后缀名保存与数据库中
                    String saveFileName = fileName + "." + singleFile.getFileExt();

                    //执行上传操作
                    singleFile.saveAs(filedir, File.SAVEAS_VIRTUAL);
                    System.out.println("上传至： " + filedir);

                    int albumId = Integer.parseInt(su.getRequest().getParameter("albumId"));
                    String albumTitle = su.getRequest().getParameter("albumTitle");
                    int singerId = Integer.parseInt(su.getRequest().getParameter("singerId"));
                    String albumPubCom = su.getRequest().getParameter("albumPubCom");
                    // 获取日期
                    String albumPubDateString = su.getRequest().getParameter("albumPubDate");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date albumPubDate = sdf.parse(albumPubDateString);

                    Album albumData = new Album();
                    albumData.setAlbumId(albumId);
                    albumData.setAlbumTitle(albumTitle);
                    albumData.setSingerId(singerId);
                    albumData.setAlbumPubCom(albumPubCom);
                    albumData.setAlbumPubDate(albumPubDate);
                    albumData.setAlbumPic(saveFileName);
                    SqlSession session = MybatisUtils.getSession();
                    int update = session.update("mapper.AlbumMapper.update", albumData);
                    session.commit();
                    session.close();

                    if (update == 1){
                        request.setAttribute("message", "修改专辑成功！");
                    } else {
                        request.setAttribute("message", "修改专辑失败！");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("修改专辑Servlet异常！", e);
        }
        request.setAttribute("flag", true);
        request.getRequestDispatcher("admin/albummgr.jsp").forward(request, response);
    }
}
