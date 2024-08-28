package com.example.jspmusic.action;


import com.example.jspmusic.Constant;
import com.example.jspmusic.utils.MybatisUtils;
import com.example.jspmusic.vo.*;
import com.jspsmart.upload.File;
import com.jspsmart.upload.Files;
import com.jspsmart.upload.SmartUpload;
import org.apache.ibatis.session.SqlSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@WebServlet("/SongServlet")
public class SongServlet extends HttpServlet {
    private ServletConfig servletConfig;

    private java.lang.String info = "";

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.servletConfig = config;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        info = req.getParameter("info");
        // 跳转到播放当前歌曲页面
        if (info.equals("play")){
            this.song_play(req, resp);
        }
        // 跳转到关注管理页面
        if (info.equals("followmgr")){
            this.song_followmgr(req, resp);
        }
        // 收藏当前歌曲
        if (info.equals("follow")){
            this.song_follow(req, resp);
        }
        // 删除关注
        if (info.equals("removeFollow")){
            this.remove_follow(req, resp);
        }
        // 模糊搜索歌曲
        if (info.equals("search")){
            this.song_search(req, resp);
        }
        // 下载当前歌曲
        if (info.equals("download")){
            this.song_download(req, resp);
        }
        // 根据页数查看所有歌曲
        if (info.equals("find")){
            this.findByPage(req, resp);
        }
        // 添加歌曲
        if (info.equals("add")){
            this.song_add(req, resp);
        }
        // 删除歌曲
        if (info.equals("del")){
            this.song_del(req, resp);
        }
        // 修改歌曲
        if (info.equals("update")){
            this.song_update(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    // 跳转到播放当前歌曲页面
    protected void song_play(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int playId = Integer.parseInt(request.getParameter("playId"));
        Song songData = new Song();
        songData.setSongId(playId);
        SqlSession session = MybatisUtils.getSession();
        Song song = session.selectOne("mapper.SongMapper.findSongInfo_Song", songData);
        Album album = session.selectOne("mapper.SongMapper.findSongInfo_Album", song.getAlbumId());
        Singer singer = session.selectOne("mapper.SongMapper.findSongInfo_Singer", song.getSingerId());
        List<Comments> comments = session.selectList("mapper.SongMapper.findSongComments", song.getSongId());
        SongAllInfo songAllInfo = new SongAllInfo();
        songAllInfo.setSong(song);
        songAllInfo.setAlbum(album);
        songAllInfo.setSinger(singer);
        songAllInfo.setComments(comments);


        request.setAttribute("songInfo", songAllInfo);
        request.getRequestDispatcher("song.jsp").forward(request, response);

    }
    // 跳转到关注管理页面
    protected void song_followmgr(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        NormalUser user = (NormalUser) session.getAttribute("user");
        int userId = user.getUserId();
        NormalUserSong data = new NormalUserSong();
        data.setUserId(userId);
        SqlSession session1 = MybatisUtils.getSession();
        List<Song> songs = session1.selectList("mapper.NormalUserMapper.findUserSong",data);
        session1.close();
//        List<NormalUserSong> list = normalUserSongDao.findAllSong(data);
        request.setAttribute("list", songs);
        request.getRequestDispatcher("user/followSong.jsp").forward(request, response);
    }
    // 收藏当前歌曲
    protected void song_follow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int songId = Integer.parseInt(request.getParameter("songId"));
        int userId = Integer.parseInt(request.getParameter("userId"));

        NormalUserSong nusData = new NormalUserSong();
        nusData.setSongId(songId);
        nusData.setUserId(userId);

        // 判断是否已经收藏
        SqlSession session = MybatisUtils.getSession();
        NormalUserSong normalUserSong = session.selectOne("mapper.NormalUserMapper.isfollow", nusData);
        if (normalUserSong != null){
            int delete = session.delete("mapper.NormalUserMapper.deleteSong", nusData);
            session.commit();
            if (delete == 1){
                request.setAttribute("message", "取消收藏成功！");
            } else {
                request.setAttribute("message", "取消收藏失败！");
            }
        } else {
            int insert = session.insert("mapper.NormalUserMapper.saveSong", nusData);
            session.commit();
            if (insert == 1){
                request.setAttribute("message", "收藏成功！");
            } else {
                request.setAttribute("message", "收藏失败！");
            }
        }
        session.close();

        request.setAttribute("flag", true);
        request.getRequestDispatcher("SongServlet?info=play&playId=" + songId).forward(request, response);
    }
    protected void remove_follow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int songId = Integer.parseInt(request.getParameter("songId"));
        HttpSession session = request.getSession();
        NormalUser user = (NormalUser) session.getAttribute("user");
        int userId = user.getUserId();
        NormalUserSong nusData = new NormalUserSong();
        nusData.setUserId(userId);
        nusData.setSongId(songId);
        SqlSession session1 = MybatisUtils.getSession();
        int delete = session1.delete("mapper.NormalUserMapper.deleteSong", nusData);
        session1.commit();
        session1.close();
        if (delete != 1){
            request.setAttribute("message", "删除失败！");
            request.setAttribute("flag", true);
        }
        request.getRequestDispatcher("SongServlet?info=followmgr").forward(request, response);
    }
    protected void song_search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        java.lang.String songTitle = request.getParameter("songTitle");
//
//        Song songData = new Song();
//        songData.setSongTitle(songTitle);
        SqlSession session = MybatisUtils.getSession();
        List<Song> songList = session.selectList("mapper.SongMapper.findSongsByTitle", songTitle);
        session.close();
//        List<Song> songList = songDao.findSongsByTitle(songData);

        request.setAttribute("songList", songList);
        request.getRequestDispatcher("search.jsp").forward(request, response);

    }
    // 下载当前歌曲
    protected void song_download(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        java.lang.String path = request.getParameter("path");
        int songId = Integer.parseInt(request.getParameter("songId"));
        String substring = path.substring(path.lastIndexOf("/") + 1);

        // 更新数据库下载次数
        SqlSession session = MybatisUtils.getSession();
        int update = session.update("mapper.SongMapper.downloadSong", songId);
        session.commit();
        session.close();

        ServletContext servletContext = request.getServletContext();
        String realPath = servletContext.getRealPath(path);
        response.setContentType("application/octet-stream");
        String encode = URLEncoder.encode(substring, "UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encode);
        // 定义文件路径
//        String filePath = encode;
        // 创建输入流
        InputStream fileInputStream = null;
        ServletOutputStream outputStream = null;
        try {
            fileInputStream = new FileInputStream(realPath);
            outputStream = response.getOutputStream();

            // 读取文件并写入响应输出流
            byte[] buffer = new byte[102400];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }


        } catch (Exception e) {

        } finally {
            // 关闭流
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // 根据页数查看所有歌曲
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
        Integer totalRecord = session.selectOne("mapper.SongMapper.totalNum");
        //获取总页数
        int totalPage = totalRecord / pageSize;
        if(totalRecord % pageSize !=0){
            totalPage++;
        }
        List<Song> songs= session.selectList("mapper.SongMapper.limitSong", new Page((pageNum - 1) * pageSize, pageSize));
        List<Album> albums = session.selectList("mapper.AlbumMapper.findAllAlbums");
        List<Singer> singers = session.selectList("mapper.SingerMapper.findAllSingers");

        session.close();
        Pager<Song> result = new Pager<>(pageSize,pageNum,totalRecord,totalPage,songs);


        // 返回结果到页面
        request.setAttribute("result", result);
        request.setAttribute("singers", singers);
        request.setAttribute("albums", albums);
        request.getRequestDispatcher("admin/songmgr.jsp").forward(request, response);
    }
    // 添加歌曲
    protected void song_add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //新建一个SmartUpload对象
            SmartUpload su = new SmartUpload();
            //上传初始化
            su.initialize(servletConfig, request, response);
            //限制每个上传文件的最大长度 200MB
            su.setMaxFileSize(200*1024*1024);
            //设定禁止上传的文件（通过扩展名限制），禁止上传带有exe,bat,jsp,htm,html扩展名的文件和没有扩展名的文件
            su.setDeniedFilesList("exe,bat,jsp,htm,html");
            //上传文件
            su.upload();
            //获取上传的文件操作
            Files files = su.getFiles();
            //获取歌曲音频文件
            File singleFile = files.getFile(0);
            //获取歌曲歌词文件
            File lrcFile = files.getFile(1);
            //获取上传文件的扩展名
            String fileType = singleFile.getFileExt();
            //设置上传文件的扩展名
            String[] type = {"MP3","mp3","flac"};
            // 判断上传文件的类型是否正确
            int place = java.util.Arrays.binarySearch(type, fileType);
            //判断文件扩展名是否正确
            if (place != -1){
                //判断该文件是否被选择
                if (!singleFile.isMissing()){
                    //以系统时间作为上传文件名称，设置上传完整路径
                    String fileName = String.valueOf(System.currentTimeMillis());
                    String lrcfileName = String.valueOf(System.currentTimeMillis());
                    String filedir = Constant.MUSIC_PATH + fileName + "." + singleFile.getFileExt();
                    String lrcfiledir = Constant.LYRIC_PATH + lrcfileName  + "." + lrcFile.getFileExt();
                    // 带后缀名保存与数据库中
                    String saveFileName = fileName + "." + singleFile.getFileExt();
                    String savelrcFileName = lrcfileName + "." + lrcFile.getFileExt();
                    //执行上传操作
                    singleFile.saveAs(filedir, File.SAVEAS_VIRTUAL);
                    lrcFile.saveAs(lrcfiledir, File.SAVEAS_VIRTUAL);
                    String songTitle = su.getRequest().getParameter("songTitle");
                    int singerId = Integer.parseInt(su.getRequest().getParameter("singerId"));
                    int albumId = Integer.parseInt(su.getRequest().getParameter("albumId"));
                    Song songData = new Song();
                    songData.setSingerId(singerId);
                    songData.setAlbumId(albumId);
                    songData.setSongTitle(songTitle);
                    songData.setSongPlaytimes(0);
                    songData.setSongDldtimes(0);
                    songData.setSongFile(saveFileName);
                    songData.setSongLyric(savelrcFileName);
                    SqlSession session = MybatisUtils.getSession();
                    int insert = session.insert("mapper.SongMapper.save", songData);
                    session.commit();
                    session.close();
                    if (insert == 1){
                        request.setAttribute("message", "成功添加歌曲！");
                    } else {
                        request.setAttribute("message", "添加歌曲失败！");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("添加歌曲Servlet异常！", e);
        }
        request.setAttribute("flag", true);
        request.getRequestDispatcher("admin/songmgr.jsp").forward(request, response);
    }
    // 删除歌曲
    protected void song_del(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int songId = Integer.parseInt(request.getParameter("songId"));

        Song songData = new Song();
        songData.setSongId(songId);;
        SqlSession session = MybatisUtils.getSession();
        int delete = session.delete("mapper.SongMapper.del", songData);
        session.commit();
        session.close();
        if (delete == 1){
            request.setAttribute("message", "删除成功！");
        } else {
            request.setAttribute("message", "删除失败！");
        }
        request.setAttribute("flag", true);
        request.getRequestDispatcher("admin/songmgr.jsp").forward(request, response);
    }
//     修改歌曲
    protected void song_update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            //新建一个SmartUpload对象
            SmartUpload su = new SmartUpload();
            //上传初始化
            su.initialize(servletConfig, request, response);
            //限制每个上传文件的最大长度 200MB
            su.setMaxFileSize(200*1024*1024);
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
            //获取歌曲歌词文件
            File lrcFile = files.getFile(1);
            //获取上传文件的扩展名
            String fileType = singleFile.getFileExt();
            //设置上传文件的扩展名
            String[] type = {"MP3","mp3","flac"};
            // 判断上传文件的类型是否正确
            int place = java.util.Arrays.binarySearch(type, fileType);
            //判断文件扩展名是否正确
            if (place != -1){
                //判断该文件是否被选择
                if (!singleFile.isMissing()){

                    //以系统时间作为上传文件名称，设置上传完整路径
                    String fileName = String.valueOf(System.currentTimeMillis());
                    String lrcfileName = String.valueOf(System.currentTimeMillis());
                    String filedir = Constant.MUSIC_PATH + fileName + "." + singleFile.getFileExt();
                    String lrcfiledir = Constant.LYRIC_PATH + lrcfileName  + "." + lrcFile.getFileExt();
                    // 带后缀名保存与数据库中
                    String saveFileName = fileName + "." + singleFile.getFileExt();
                    String savelrcFileName = lrcfileName + "." + lrcFile.getFileExt();

                    //执行上传操作
                    singleFile.saveAs(filedir, File.SAVEAS_VIRTUAL);
                    lrcFile.saveAs(lrcfiledir, File.SAVEAS_VIRTUAL);

                    int songId = Integer.parseInt(su.getRequest().getParameter("songId"));
                    String songTitle = su.getRequest().getParameter("songTitle");
                    int singerId = Integer.parseInt(su.getRequest().getParameter("singerId"));
                    int albumId = Integer.parseInt(su.getRequest().getParameter("albumId"));

                    Song songData = new Song();
                    songData.setSongId(songId);
                    songData.setSingerId(singerId);
                    songData.setAlbumId(albumId);
                    songData.setSongTitle(songTitle);
                    songData.setSongFile(saveFileName);
                    songData.setSongLyric(savelrcFileName);
                    SqlSession session = MybatisUtils.getSession();
                    int update = session.update("mapper.SongMapper.update", songData);
                    session.commit();
                    session.close();
                    if (update == 1){
                        request.setAttribute("message", "成功修改歌曲！");
                    } else {
                        request.setAttribute("message", "修改歌曲失败！");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("修改歌曲Servlet异常！", e);
        }

        request.setAttribute("flag", true);
        request.getRequestDispatcher("admin/songmgr.jsp").forward(request, response);
    }
}
