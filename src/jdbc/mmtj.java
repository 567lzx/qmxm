/*package jdbc;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
@WebServlet("/mmtj")
public class mmtj extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPut(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = Integer.parseInt(req.getParameter("userId"));
        String pass1 = req.getParameter("pass1");
        String pass2 = req.getParameter("pass2");
        String grant = (String) req.getSession().getAttribute("grant");
        if (!pass1.equals(pass2)){
            req.getRequestDispatcher("/grxx.jsp").forward(req,resp);
            return;
        }

        String sql = null;
        if (grant.equals("管理员")) {
            // language=MySQL
            sql="UPDATE admin set admin_pass=? where admin_id=?";
        } else if (grant.equals("教师")) {
                sql="UPDATE tea set tea_pass=? where tea_id=?";
        } else if (grant.equals("学生")) {
            sql="UPDATE stu set stu_pass=? where stu_id=?";
        }

        String url = "jdbc:mysql://127.0.0.1:3306/cjglxt";
        String DBUsername = "root";
        String DBPassword = "000000";
        Connection connection = null;
        PreparedStatement preparedStatement =null;
        try {
            connection = DriverManager.getConnection(url, DBUsername, DBPassword);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,pass1);
            preparedStatement.setInt(2,userId);
            int i = preparedStatement.executeUpdate();
            //if (i<=0){System.out.println("失败");}
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                preparedStatement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


    }
}*/
package jdbc;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

@WebServlet("/mmtj")
public class mmtj extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(mmtj.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPut(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*接收请求中的"userId"、"pass1"和"pass2"参数。*/
        int userId = Integer.parseInt(req.getParameter("userId"));
        String pass1 = req.getParameter("pass1");
        String pass2 = req.getParameter("pass2");
        String grant = (String) req.getSession().getAttribute("grant");
        /*然后，根据会话中的"grant"参数确定要查询的集合和密码字段。如果"pass1"和"pass2"不相同，则向请求对象设置属性并转发请求到当前页面，弹出修改失败。
        * 否则，servlet查询集合以查找匹配的用户，然后使用"$set"操作符更新密码字段的值。*/
        if (!pass1.equals(pass2)){
            req.getRequestDispatcher("/grxx.jsp").forward(req,resp);
            return;
        }

        String collectionName = null;
        String fieldName = null;

        if (grant.equals("管理员")) {
            collectionName = "admin";
            fieldName = "admin_pass";
        } else if (grant.equals("教师")) {
            collectionName = "tea";
            fieldName = "tea_pass";
        } else if (grant.equals("学生")) {
            collectionName = "stu";
            fieldName = "stu_pass";
        }
        /*连接到数据库，查询"admin"、"tea"和"stu"集合，并更新匹配用户ID的密码字段。*/
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient();
            MongoDatabase database = mongoClient.getDatabase("cjglxt");
            MongoCollection<Document> collection = database.getCollection(collectionName);
            Document query = new Document(collectionName+"_id", userId);
            Document update = new Document("$set", new Document(fieldName, pass1));
            collection.updateOne(query, update);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            if (mongoClient != null) {
                /*关闭MongoDB客户端连接*/
                mongoClient.close();
            }
        }
    }

}