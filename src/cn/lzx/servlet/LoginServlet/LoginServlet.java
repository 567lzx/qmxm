/*
package cn.lzx.servlet.LoginServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String userid = req.getParameter("userid");
        System.out.println("账号：" + userid);
        String password = req.getParameter("password");
        System.out.println("密码是：" + password);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

//        注册驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //        获取链接
            String url = "jdbc:mysql://127.0.0.1:3306/cjglxt";
            String DBUsername = "root";
            String DBPassword = "000000";
            conn = DriverManager.getConnection(url, DBUsername, DBPassword);

//        定义sql，用？来占位
            String sql = "select*from stu where stu_id=?  and stu_pass=? ";


//        获取pstmt对象
            pstmt = conn.prepareStatement(sql);


//        设置问号的值
            pstmt.setString(1, userid);
            pstmt.setString(2, password);


//        执行sql
            rs = pstmt.executeQuery();

//        判断登录是否为学生账号，如果是，跳转到学生平台
            if (rs.next()) {
                String stuId = rs.getString("stu_id");
                String stuName = rs.getString("stu_name");
                String stuPass = rs.getString("stu_pass");
                String stuClass = rs.getString("stu_class");
                String stuSex = rs.getString("stu_sex");
                Integer stuTele = rs.getInt("stu_tele");
                HttpSession session = req.getSession();
                session.setAttribute("userid", userid);
                session.setAttribute("stuId", stuId);
                session.setAttribute("stuName", stuName);
                session.setAttribute("stuPass", stuPass);
                session.setAttribute("stuClass", stuClass);
                session.setAttribute("stuSex", stuSex);
                session.setAttribute("stuTele", stuTele);
                session.setAttribute("grant","学生");
                System.out.println(userid + "登录成功");
                try {
                    //请求转发
                    req.getRequestDispatcher("/WEB-INF/xuesheng.jsp").forward(req, resp);
                } catch (ServletException e) {
                    e.printStackTrace();
                }
            }
            //如果不是学生，再判断是否管理员账号，如果是，跳转到管理员平台
            else {
                String sql2 = "select*from admin where admin_id=? and admin_pass=? ";
                pstmt = conn.prepareStatement(sql2);
                pstmt.setString(1, userid);
                pstmt.setString(2, password);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    String adminId = rs.getString("admin_id");

                    String adminPass = rs.getString("admin_pass");

                    String adminName = rs.getString("admin_name");
                    HttpSession session = req.getSession();
                    session.setAttribute("userid", userid);
                    session.setAttribute("adminName", adminName);
                    session.setAttribute("adminId", adminId);
                    session.setAttribute("adminPass", adminPass);
                    session.setAttribute("grant","管理员");
                    System.out.println(userid + "登录成功");
                    try {
                        //请求转发
                        req.getRequestDispatcher("/WEB-INF/guanliyuan.jsp").forward(req, resp);
                    } catch (ServletException e) {
                        e.printStackTrace();
                    }
                }
                //如果不是学生也不是管理员，就判断是不是老师，如果是就跳转到教师平台，如果不是则登录失败
                else {
                    String sql3 = "select*from tea where tea_id=? and tea_pass=? ";
                    pstmt = conn.prepareStatement(sql3);
                    pstmt.setString(1, userid);
                    pstmt.setString(2, password);
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        String teaId = rs.getString("tea_id");
                        String teaPass = rs.getString("tea_pass");
                        String teaName = rs.getString("tea_name");
                        String teaSex = rs.getString("tea_sex");
                        String teaTele = rs.getString("tea_tele");

                        HttpSession session = req.getSession();
                        session.setAttribute("userid", userid);
                        session.setAttribute("teaName", teaName);
                        session.setAttribute("teaId", teaId);
                        session.setAttribute("teaPass", teaPass);
                        session.setAttribute("teaSex", teaSex);
                        session.setAttribute("teaTele", teaTele);
                        session.setAttribute("grant","教师");
                        System.out.println(userid + "登录成功");
                        try {
                            //请求转发
                            req.getRequestDispatcher("/WEB-INF/jiaoshi.jsp").forward(req, resp);
                        } catch (ServletException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("登录失败");
                        //重定向
                        resp.sendRedirect("/main.jsp？err=1");
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                pstmt.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }
}
*/
package cn.lzx.servlet.LoginServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {



    private MongoClient mongoClient;

    @Override
    public void init() throws ServletException {
        super.init();
        // 初始化MongoClient
        mongoClient = new MongoClient("localhost", 27017);
    }

    @Override
    public void destroy() {
        super.destroy();
        // 关闭MongoClient
        mongoClient.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        int userid = Integer.parseInt(req.getParameter("userid"));
        System.out.println("账号：" + userid);
        String password = req.getParameter("password");
        System.out.println("密码是：" + password);

        MongoDatabase db = mongoClient.getDatabase("cjglxt");
        MongoCollection<Document> stuColl = db.getCollection("stu");
        MongoCollection<Document> adminColl = db.getCollection("admin");
        MongoCollection<Document> teaColl = db.getCollection("tea");

        // 查询学生账号
        FindIterable<Document> stuResults = stuColl.find(Filters.and(Filters.eq("stu_id", userid), Filters.eq("stu_pass", password)));
        Document stuDoc = stuResults.first();
        if (stuDoc != null) {
            int stuId = stuDoc.getInteger("stu_id");
            String stuName = stuDoc.getString("stu_name");
            String stuPass = stuDoc.getString("stu_pass");
            String stuClass = stuDoc.getString("stu_class");
            String stuSex = stuDoc.getString("stu_sex");
            Integer stuTele = stuDoc.getInteger("stu_tele");

            HttpSession session = req.getSession();
            session.setAttribute("userid", userid);
            session.setAttribute("stuId", stuId);
            session.setAttribute("stuName", stuName);
            session.setAttribute("stuPass", stuPass);
            session.setAttribute("stuClass", stuClass);
            session.setAttribute("stuSex", stuSex);
            session.setAttribute("stuTele", stuTele);
            session.setAttribute("grant", "学生");
            System.out.println(userid + "登录成功");
            try {
                //请求转发
                req.getRequestDispatcher("/WEB-INF/xuesheng.jsp").forward(req, resp);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
        // 如果不是学生，查询管理员账号
        else {
            FindIterable<Document> adminResults = adminColl.find(Filters.and(Filters.eq("admin_id", userid), Filters.eq("admin_pass", password)));
            Document adminDoc = adminResults.first();
            if (adminDoc != null) {
                int adminId = adminDoc.getInteger("admin_id");
                String adminPass = adminDoc.getString("admin_pass");
                String adminName = adminDoc.getString("admin_name");

                HttpSession session = req.getSession();
                session.setAttribute("userid", userid);
                session.setAttribute("adminId", adminId);
                session.setAttribute("adminPass", adminPass);
                session.setAttribute("adminName", adminName);
                session.setAttribute("grant", "管理员");
                System.out.println(userid + "登录成功");
                try {
                    //请求转发
                    req.getRequestDispatcher("/WEB-INF/guanliyuan.jsp").forward(req, resp);
                } catch (ServletException e) {
                    e.printStackTrace();
                }
            }
            // 如果不是学生也不是管理员，查询教师账号
            else {
                FindIterable<Document> teaResults = teaColl.find(Filters.and(Filters.eq("tea_id", userid), Filters.eq("tea_pass", password)));
                Document teaDoc = teaResults.first();
                if (teaDoc != null) {
                    int teaId = teaDoc.getInteger("tea_id");
                    String teaName = teaDoc.getString("tea_name");
                    String teaPass = teaDoc.getString("tea_pass");
                    String teaDept = teaDoc.getString("tea_dept");
                    String teaSex = teaDoc.getString("tea_sex");
                    Integer teaTele = teaDoc.getInteger("tea_tele");


                    HttpSession session = req.getSession();
                    session.setAttribute("userid", userid);
                    session.setAttribute("teaId", teaId);
                    session.setAttribute("teaName", teaName);
                    session.setAttribute("teaPass", teaPass);
                    session.setAttribute("teaDept", teaDept);
                    session.setAttribute("teaSex", teaSex);
                    session.setAttribute("teaTele", teaTele);
                    session.setAttribute("grant", "教师");
                    System.out.println(userid + "登录成功");
                    try {
                        //请求转发
                        req.getRequestDispatcher("/WEB-INF/jiaoshi.jsp").forward(req, resp);
                    } catch (ServletException e) {
                        e.printStackTrace();
                    }
                }
                // 如果都不是，则登录失败，返回登录页面
                else {
                    System.out.println("登录失败");
                    try {
                        //请求重定向
                        resp.sendRedirect("/main.jsp？err=1");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}