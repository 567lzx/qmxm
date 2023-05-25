/*package res_zsgc;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

@WebServlet("/resModify")
public class gai extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        int stuId = Integer.parseInt(req.getParameter("stuId"));
        String resSubName = req.getParameter("resSubName");
        int resNum = Integer.parseInt(req.getParameter("resNum"));
        String resTerm =req.getParameter("resTerm");
        int resId = Integer.parseInt(req.getParameter("resId"));

        Properties prop = new Properties();
        prop.load(new FileInputStream("/qmxm/src/druid.properties"));
        DataSource dataSource = null;
        Connection conn = null;
                PreparedStatement pstmt =null;
        try {
            dataSource = DruidDataSourceFactory.createDataSource(prop);
            conn = dataSource.getConnection();


            String sql="update res set stu_id=?,sub_name=?,res_num=?,res_term=? where res_id=?";



            pstmt = conn.prepareStatement(sql);


            pstmt.setInt(1,stuId);
            pstmt.setString(2,resSubName);
            pstmt.setInt(3,resNum);
            pstmt.setString(4,resTerm);
            pstmt.setInt(5,resId);

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

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/resModify")
public class gai extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
    /*POST请求以修改MongoDB集合中的文档。从请求中（前端）接收以下参数：stuName、resSubName、stuCla、resTerm、resNum、stuId和resId。*/
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String stuName = req.getParameter("stuName");
        String resSubName = req.getParameter("resSubName");
        String stuCla = req.getParameter("stuCla");
        String resTerm = req.getParameter("resTerm");
        int resNum = Integer.parseInt(req.getParameter("resNum"));
        int stuId = Integer.parseInt(req.getParameter("stuId"));
        int resId = Integer.parseInt(req.getParameter("resId"));


        // 连接 MongoDB 数据库，获得对“cjglxt”数据库和“res”集合的引用
        try (com.mongodb.client.MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("cjglxt");
            MongoCollection<Document> collection = database.getCollection("res");

            /*构建一个查询条件和更新内容的Document对象，将其传递给集合的updateOne()方法来更新集合中的文档。*/
            Document filter = new Document("stu_id", stuId);
            Document update = new Document("$set", new Document()
                    .append("stu_name", stuName)
                    .append("stu_class", stuCla)
                    .append("sub_name", resSubName)
                    .append("res_term", resTerm)
                    .append("res_num", resNum)
                    .append("res_id", resId));

            // 执行更新操作
            collection.updateOne(filter, update);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*将响应重定向到URL“/res”*/
        resp.sendRedirect("/res");
    }
}