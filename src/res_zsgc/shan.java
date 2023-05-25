/*
package res_zsgc;

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

@WebServlet("/resDelete")
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
                    "    location.href=\"/res\";\n" +
                    "}\n" +
                    "window.onload=function(){setTimeout('jumpurl()',3000)\n}" +
                    "</script>");
            return;
        }
            int resId = Integer.parseInt(req.getParameter("resId"));


            Properties prop = new Properties();
            prop.load(new FileInputStream("/qmxm/src/druid.properties"));
            DataSource dataSource = null;
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                dataSource = DruidDataSourceFactory.createDataSource(prop);
                conn = dataSource.getConnection();


                String sql = "delete from res where res_id=?";
                pstmt = conn.prepareStatement(sql);


                pstmt.setInt(1, resId);

                int count = pstmt.executeUpdate();
                System.out.println(count > 0);
                resp.sendRedirect("/res");
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
package res_zsgc;

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

@WebServlet("/resDelete")
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
                    "    location.href=\"/res\";\n" +
                    "}\n" +
                    "window.onload=function(){setTimeout('jumpurl()',3000)\n}" +
                    "</script>");
            return;
        }
        /*接收名为“resId”的请求参数*/
        int resId = Integer.parseInt(req.getParameter("resId"));
        /*获得对“cjglxt”数据库和“res”集合的引用*/
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            MongoDatabase database = mongoClient.getDatabase("cjglxt");
            MongoCollection<Document> collection = database.getCollection("res");
            /*创建一个新的Document对象，该对象包含要删除的文档的_id。*/
            Document query = new Document("res_id", resId);
            /*使用该文档_id作为查询条件，调用MongoCollection的deleteOne()方法来从集合中删除文档*/
            collection.deleteOne(query);
            resp.sendRedirect("/res");
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }
}