/*package jdbc;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
@WebServlet("/xgmm")
public class xgmm extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        int userid = Integer.parseInt((String) session.getAttribute("userid"));
        String grant = (String) session.getAttribute("grant");
        if(grant==null){
            resp.sendRedirect("/main.jsp");
            return;
        }
        String url = "jdbc:mysql://127.0.0.1:3306/cjglxt";
        String DBUsername = "root";
        String DBPassword = "000000";
        Connection conn = null;
        String sql = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet =null;
        try {
            conn = DriverManager.getConnection(url, DBUsername, DBPassword);
            if (grant.equals("管理员")) {
                // language=MySQL
                sql = "SELECT * FROM admin WHERE admin_id=?";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1,userid);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    String admin_pass = resultSet.getString("admin_pass");
                    req.setAttribute("admin_pass",admin_pass);
                    req.getRequestDispatcher("/xgmm.jsp").forward(req,resp);

                }
            } else if (grant.equals("教师")) {
                sql = "SELECT * FROM tea WHERE tea_id=?";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1,userid);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    String tea_pass = resultSet.getString("tea_pass");
                    req.setAttribute("tea_pass",tea_pass);
                    req.getRequestDispatcher("/xgmm.jsp").forward(req,resp);

                }
            } else if (grant.equals("学生")) {
                sql = "SELECT * FROM stu WHERE stu_id=?";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1,userid);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    String stu_pass = resultSet.getString("stu_pass");
                    req.setAttribute("stu_pass",stu_pass);
                    req.getRequestDispatcher("/xgmm.jsp").forward(req,resp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}*/
package jdbc;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/xgmm")
public class xgmm extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*从请求中接受一个“userid”参数，并使用它来查询对应的用户密码。它确定要查询的集合名称和密码字段，根据用户权限（grant）的不同来选择不同的集合和字段。*/
        HttpSession session = req.getSession();
        /*int userid = Integer.parseInt((String) session.getAttribute("userid"));*/
        int userid = (int) session.getAttribute("userid");
        String grant = (String) session.getAttribute("grant");
        if(grant==null){
            resp.sendRedirect("/main.jsp");
            return;
        }
        /*连接到数据库，查询“admin”，“tea”和“stu”集合，并检索相关数据。*/
        String uri = "mongodb://localhost:27017";
        MongoClientURI clientURI = new MongoClientURI(uri);
        MongoClient mongoClient = new MongoClient(clientURI);
        MongoDatabase database = mongoClient.getDatabase("cjglxt");

        String collectionName = null;
        String fieldName = null;
        /*从MongoDB中获取与给定ID对应的文档，并提取相应的密码字段。最后，它将密码字段作为属性设置到请求对象中，并将请求转发到一个JSP页面来呈现。*/
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

        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document query = new Document(collectionName+"_id", userid);
        Document result = collection.find(query).first();

        if (result != null) {
            String password = result.getString(fieldName);
            req.setAttribute(fieldName, password);
            req.getRequestDispatcher("/xgmm.jsp").forward(req, resp);
        }
        /*关闭数据库连接以避免资源浪费。*/
        mongoClient.close();
    }
}