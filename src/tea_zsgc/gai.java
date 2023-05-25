/*package tea_zsgc;

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

@WebServlet("/teaModify")
public class gai extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");


        String teaName = req.getParameter("teaName");
        String teaSex = req.getParameter("teaSex");
        int teaTele = Integer.parseInt(req.getParameter("teaTele"));
//        String teaIdStr = req.getParameter("teaId");
//        int teaId = Integer.parseInt(teaIdStr);
        int teaId = Integer.parseInt(req.getParameter("teaId"));

        Properties prop = new Properties();
        prop.load(new FileInputStream("/qmxm/src/druid.properties"));
        DataSource dataSource = null;
        Connection conn = null;
                PreparedStatement pstmt =null;
        try {
            dataSource = DruidDataSourceFactory.createDataSource(prop);
            conn = dataSource.getConnection();


            String sql="update tea set tea_name=?,tea_sex=?,tea_tele=? where tea_id=?";



            pstmt = conn.prepareStatement(sql);


            pstmt.setString(1,teaName);
            pstmt.setString(2,teaSex);
            pstmt.setInt(3,teaTele);
            pstmt.setInt(4,teaId);

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

@WebServlet("/teaModify")
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

        String teaName = req.getParameter("teaName");
        String teaSex = req.getParameter("teaSex");
        int teaTele = Integer.parseInt(req.getParameter("teaTele"));
        int teaId = Integer.parseInt(req.getParameter("teaId"));

        /*创建 MongoDB 数据库和集合的连接，并使用教师的 ID 和新信息构建筛选器和更新文档。*/
        try (com.mongodb.client.MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("cjglxt");
            MongoCollection<Document> collection = database.getCollection("tea");

            // 构建查询条件和更新内容
            Document filter = new Document("tea_id", teaId);
            Document update = new Document("$set", new Document()
                    .append("tea_name", teaName)
                    .append("tea_sex", teaSex)
                    .append("tea_tele", teaTele));

            // 执行更新操作
            collection.updateOne(filter, update);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*重定向用户到教师信息页面。*/
        resp.sendRedirect("/tea");
    }
}
