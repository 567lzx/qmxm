/*
package tea_zsgc;

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

@WebServlet("/teaDelete")
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
            writer.println("您不具备该操作权限");
            writer.println("<script>function jumpurl(){\n" +
                    "    location.href=\"/tea\";\n" +
                    "}\n" +
                    "window.onload=function(){setTimeout('jumpurl()',3000)\n}" +
                    "</script>");
            return;
        }
            int teaId = Integer.parseInt(req.getParameter("teaId"));


            Properties prop = new Properties();
            prop.load(new FileInputStream("/qmxm/src/druid.properties"));
            DataSource dataSource = null;
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                dataSource = DruidDataSourceFactory.createDataSource(prop);
                conn = dataSource.getConnection();


                String sql = "delete from tea where tea_id=?";
                pstmt = conn.prepareStatement(sql);


                pstmt.setInt(1, teaId);

                int count = pstmt.executeUpdate();
                System.out.println(count > 0);

                resp.sendRedirect("/tea");
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
package tea_zsgc;

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

@WebServlet("/teaDelete")
public class shan extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*首先检查用户是否具有管理员权限，如果没有，则向用户显示错误消息并在 3 秒后将其重定向回 /tea 路径。否则，从 HTTP 请求中获取要删除的教师的 teaId*/
        resp.setContentType("text/html;charset=utf-8");
        String grant = (String) req.getSession().getAttribute("grant");
        if (grant==null||!grant.equals("管理员")) {
            PrintWriter writer = resp.getWriter();
            writer.println("您不具备该操作权限,将在三秒后返回原页面！！！");
            writer.println("<script>function jumpurl(){\n" +
                    "    location.href=\"/tea\";\n" +
                    "}\n" +
                    "window.onload=function(){setTimeout('jumpurl()',3000)\n}" +
                    "</script>");
            return;
        }
        int teaId = Integer.parseInt(req.getParameter("teaId"));
        /*使用 MongoClient、MongoDatabase 和 MongoCollection 对象连接到 MongoDB 数据库，然后使用 collection.deleteOne() 方法根据给定的条件删除符合条件的文档。*/
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("cjglxt");
            MongoCollection<Document> collection = database.getCollection("tea");
            Document query = new Document("tea_id", teaId);
            collection.deleteOne(query);
            /*最后，将用户重定向到 /tea 路径。*/
            resp.sendRedirect("/tea");
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }
}