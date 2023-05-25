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

@WebServlet("/resAdd")
public class zeng extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int stuId = Integer.parseInt(req.getParameter("stuId"));
        String resSubName = req.getParameter("resSubName");
        int resNum = Integer.parseInt(req.getParameter("resNum"));
        String resTerm =req.getParameter("resTerm");


        Properties prop = new Properties();
        prop.load(new FileInputStream("/qmxm/src/druid.properties"));
        DataSource dataSource = null;
        Connection conn = null;
                PreparedStatement pstmt =null;
        try {
            dataSource = DruidDataSourceFactory.createDataSource(prop);
            conn = dataSource.getConnection();


            String sql = "insert into res(stu_id,sub_name,res_num,res_term) value (?,?,?,?);";
            pstmt = conn.prepareStatement(sql);


            pstmt.setInt(1,stuId);
            pstmt.setString(2,resSubName);
            pstmt.setInt(3,resNum);
            pstmt.setString(4,resTerm);

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
/*
package res_zsgc;

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

@WebServlet("/resAdd")
public class zeng extends HttpServlet {

    private static final String DB_HOST = "localhost"; // MongoDB 主机名
    private static final int DB_PORT = 27017; // MongoDB 端口号
    private static final String DB_NAME = "cjglxt"; // MongoDB 数据库名称
    private static final String COLLECTION_NAME = "res"; // MongoDB 集合名称

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int stuId = Integer.parseInt(req.getParameter("stuId"));
        String resSubName = req.getParameter("resSubName");
        int resNum = Integer.parseInt(req.getParameter("resNum"));
        String resTerm =req.getParameter("resTerm");

        Document document = new Document();
        document.append("res_id", stuId);
        document.append("res_name", resSubName);
        document.append("res_num", resNum);
        document.append("res_term", resTerm);
        AggregateIterable<Document> iterable = collection.aggregate(
                Arrays.asList(
                        new Document("$sort", new Document("res_id", -1)),
                        new Document("$limit", 1),
                        new Document("$project", new Document("_id", 0).append("max_res_id", "$res_id"))
                )
        );
        Document result = iterable.first();
        int maxResId = result.getInteger("max_res_id");
        document.append("res_id", maxResId+1);
        collection.insertOne(document);

        resp.sendRedirect("/res");
    }

    @Override
    public void destroy() {
        super.destroy();
        if (collection != null) {
            collection = null;
        }
    }
}*/
package res_zsgc;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.util.Arrays;

@WebServlet("/resAdd")
public class zeng extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*Servlet从请求中（前端）接收到以下参数：stuId、resSubName、resNum和resTerm。*/
        int stuId = Integer.parseInt(req.getParameter("stuId"));
        String resSubName = req.getParameter("resSubName");
        int resNum = Integer.parseInt(req.getParameter("resNum"));
        String resTerm =req.getParameter("resTerm");

        MongoClient mongoClient = null;
        try {
            /*获取对“cjglxt”数据库和“res”集合的引用*/
            mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
            MongoDatabase database = mongoClient.getDatabase("cjglxt");
            MongoCollection<Document> collection = database.getCollection("res");
            /*创建一个包含接收到的参数的新文档*/
            Document document = new Document();
            document.append("stu_id", stuId);
            document.append("sub_name", resSubName);
            document.append("res_num", resNum);
            document.append("res_term", resTerm);
            /*使用聚合管道从集合中检索res_id字段的最大值：按照res_id的降序对集合进行排序，将结果限制为一个文档，并投影出res_id字段的值。*/
            AggregateIterable<Document> iterable = collection.aggregate(
                    Arrays.asList(
                            new Document("$sort", new Document("res_id", -1)),
                            new Document("$limit", 1),
                            new Document("$project", new Document("_id", 0).append("max_res_id", "$res_id"))
                    )
            );
            /*通过将新文档的res_id值加1来插入到“res”集合中。*/
            Document result = iterable.first();
            int maxResId = result.getInteger("max_res_id");
            document.append("res_id", maxResId+1);
            // Insert document into "res" collection
            collection.insertOne(document);
            resp.sendRedirect("/res");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
    }

}