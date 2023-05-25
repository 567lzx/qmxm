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
@WebServlet("/stuAdd")
public class zeng extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String stuName = req.getParameter("stuName");
        String stuCla=req.getParameter("stuCla");
        String stuSex = req.getParameter("stuSex");
        int stuTele = Integer.parseInt(req.getParameter("stuTele"));


        Properties prop = new Properties();
        prop.load(new FileInputStream("/qmxm/src/druid.properties"));
        DataSource dataSource = null;
        Connection conn = null;
                PreparedStatement pstmt =null;
        try {
            dataSource = DruidDataSourceFactory.createDataSource(prop);
            conn = dataSource.getConnection();


            String sql = "insert into stu(stu_name,stu_class,stu_sex,stu_tele) value (?,?,?,?);";
            pstmt = conn.prepareStatement(sql);


            pstmt.setString(1,stuName);

            pstmt.setString(2,stuCla);
            pstmt.setString(3,stuSex);
            pstmt.setInt(4,stuTele);

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

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


import com.mongodb.client.AggregateIterable;

@WebServlet("/stuAdd")
public class zeng extends HttpServlet {
    /*定义 MongoDB 主机名、端口号、数据库名称和集合名称等常量。在 init() 方法中，使用这些常量创建了一个 MongoClient 对象，连接到 MongoDB 数据库，并获取了名为 stu 的集合。*/
    private static final String DB_HOST = "localhost"; // MongoDB 主机名
    private static final int DB_PORT = 27017; // MongoDB 端口号
    private static final String DB_NAME = "cjglxt"; // MongoDB 数据库名称
    private static final String COLLECTION_NAME = "stu"; // MongoDB 集合名称

    private MongoCollection<Document> collection;

    @Override
    public void init() throws ServletException {
        super.init();
        MongoClient mongoClient = new MongoClient(DB_HOST, DB_PORT);
        MongoDatabase database = mongoClient.getDatabase(DB_NAME);
        collection = database.getCollection(COLLECTION_NAME);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
    /*在 doPost() 方法中，从 HTTP 请求中获取表单参数 stuName、stuCla、stuSex和stuTele。*/
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String stuName = req.getParameter("stuName");
        String stuCla = req.getParameter("stuCla");
        String stuSex = req.getParameter("stuSex");
        int stuTele = Integer.parseInt(req.getParameter("stuTele"));

        Document document = new Document();
        document.append("stu_name", stuName);
        document.append("stu_class", stuCla);
        document.append("stu_sex", stuSex);
        document.append("stu_tele", stuTele);

        /*创建一个新的 Document 对象，将这些参数添加到该文档中，还添加了一个 stu_pass 字段，用于保存默认密码*/
        document.append("stu_pass", "000000");
        /*接下来，使用 collection.aggregate() 方法查询 tea 集合中最大的 stu_id 值，然后将新的文档的 stu_id 值设置为最大值加 1。*/
        AggregateIterable<Document> iterable = collection.aggregate(
                Arrays.asList(
                        new Document("$sort", new Document("stu_id", -1)),
                        new Document("$limit", 1),
                        new Document("$project", new Document("_id", 0).append("max_stu_id", "$stu_id"))
                )
        );
        Document result = iterable.first();
        int maxStuId = result.getInteger("max_stu_id");
        document.append("stu_id", maxStuId+1);
        collection.insertOne(document);
        /*最后，将文档插入到 stu 集合中，并通过 resp.sendRedirect() 方法将用户重定向到 /stu 路径。*/
        resp.sendRedirect("/stu");
    }

    @Override
    /*关闭集合连接，以便 Servlet 容器可以释放资源*/
    public void destroy() {
        super.destroy();
        if (collection != null) {
            collection = null;
        }
    }
}