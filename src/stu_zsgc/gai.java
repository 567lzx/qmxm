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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

@WebServlet("/stuModify")
public class gai extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String stuName = req.getParameter("stuName");
        String stuCla=req.getParameter("stuCla");
        String stuSex = req.getParameter("stuSex");
        int stuTele = Integer.parseInt(req.getParameter("stuTele"));
        int stuId = Integer.parseInt(req.getParameter("stuId"));

        Properties prop = new Properties();
        prop.load(new FileInputStream("/qmxm/src/druid.properties"));
        DataSource dataSource = null;
        Connection conn = null;
                PreparedStatement pstmt =null;
        try {
            dataSource = DruidDataSourceFactory.createDataSource(prop);
            conn = dataSource.getConnection();


            String sql="update stu set stu_name=?,stu_class=?,stu_sex=?,stu_tele=? where stu_id=?";



            pstmt = conn.prepareStatement(sql);


            pstmt.setString(1,stuName);

            pstmt.setString(2,stuCla);
            pstmt.setString(3,stuSex);
            pstmt.setInt(4,stuTele);
            pstmt.setInt(5,stuId);

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
}
*/
package stu_zsgc;

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

@WebServlet("/stuModify")
public class gai extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
    /*通过 POST 请求从表单接收数据。表单数据包括教师的姓名、性别、电话号码和ID。*/
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String stuName = req.getParameter("stuName");
        String stuCla = req.getParameter("stuCla");
        String stuSex = req.getParameter("stuSex");
        int stuTele = Integer.parseInt(req.getParameter("stuTele"));
        int stuId = Integer.parseInt(req.getParameter("stuId"));

        /*创建 MongoDB 数据库和集合的连接，并使用教师的 ID 和新信息构建筛选器和更新文档。*/
        try (com.mongodb.client.MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("cjglxt");
            MongoCollection<Document> collection = database.getCollection("stu");

            // 构建查询条件和更新内容
            Document filter = new Document("stu_id", stuId);
            Document update = new Document("$set", new Document()
                    .append("stu_name", stuName)
                    .append("stu_class", stuCla)
                    .append("stu_sex", stuSex)
                    .append("stu_tele", stuTele));

            // 执行更新操作
            collection.updateOne(filter, update);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*重定向用户到学生信息页面。*/
        resp.sendRedirect("/stu");
    }
}