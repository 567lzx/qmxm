/*
package stu_zsgc;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

@WebServlet("/stuDelete")
public class shan extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        String grant = (String) req.getSession().getAttribute("grant");
        if (grant==null||!grant.equals("管理员")) {
            PrintWriter writer = resp.getWriter();
            writer.println("您不具备该操作权限,将在三秒后返回原页面！！！");
            writer.println("<script>function jumpurl(){\n" +
                    "    location.href=\"/stu\";\n" +
                    "}\n" +
                    "window.onload=function(){setTimeout('jumpurl()',3000)\n}" +
                    "</script>");
            return;
        }
            int stuId = Integer.parseInt(req.getParameter("stuId"));


            Properties prop = new Properties();
            prop.load(new FileInputStream("/qmxm/src/druid.properties"));
            DataSource dataSource = null;
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                dataSource = DruidDataSourceFactory.createDataSource(prop);
                conn = dataSource.getConnection();


                String sql = "delete from stu where stu_id=?";
                pstmt = conn.prepareStatement(sql);


                pstmt.setInt(1, stuId);

                int count = pstmt.executeUpdate();
                System.out.println(count > 0);
                resp.sendRedirect("/stu");
            } catch (Exception e) {
                e.printStackTrace();
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
}*/
package stu_zsgc;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@WebServlet("/stuDelete")
public class shan extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*首先检查用户是否具有管理员权限，如果没有，则向用户显示错误消息并在 3 秒后将其重定向回 /stu 路径。否则，从 HTTP 请求中获取要删除的教师的 stuId*/
        resp.setContentType("text/html;charset=utf-8");
        String grant = (String) req.getSession().getAttribute("grant");
        if (grant==null||!grant.equals("管理员")) {
            PrintWriter writer = resp.getWriter();
            writer.println("您不具备该操作权限,将在三秒后返回原页面！！！");
            writer.println("<script>function jumpurl(){\n" +
                    "    location.href=\"/stu\";\n" +
                    "}\n" +
                    "window.onload=function(){setTimeout('jumpurl()',3000)\n}" +
                    "</script>");
            return;
        }
        int stuId = Integer.parseInt(req.getParameter("stuId"));
        /*使用 MongoClient、MongoDatabase 和 MongoCollection 对象连接到 MongoDB 数据库，然后使用 collection.deleteOne() 方法根据给定的条件删除符合条件的文档。*/
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("cjglxt");
            MongoCollection<Document> collection = database.getCollection("stu");

            Document query = new Document("stu_id", stuId);
            collection.deleteOne(query);
            /*最后，将用户重定向到 /stu 路径。*/
            resp.sendRedirect("/stu");
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }
}