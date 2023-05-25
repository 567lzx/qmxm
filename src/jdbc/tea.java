/*
package jdbc;

import com.Account;
import com.teaAccount;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


//Result 查询数据，把数据封装到account里面，将account对象存入ArrayList集合中
@WebServlet("/tea")
public class tea extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement psta =null;



        //分页
        String n = req.getParameter("n");
        int pageSize = 8;
        int lineCount;
        int pageCount;
        int pageNow=1;
        if(n!=null){
            pageNow = Integer.parseInt(n);
        }

//        注册驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

//        获取链接
            String url = "jdbc:mysql://127.0.0.1:3306/cjglxt";
            String username = "root";
            String password = "000000";
            conn = DriverManager.getConnection(url, username, password);


            //        定义sql
            String sql = "select * from tea";

//        获取statemen
            stmt = conn.createStatement();
//        执行sql
            rs = stmt.executeQuery(sql);
//        创建集合
            List<teaAccount> teaList = new ArrayList<>();
//新
            psta = conn.prepareStatement("select count(*) from tea");
            rs = psta.executeQuery();
            rs.next();
            lineCount = rs.getInt(1);

            if(lineCount%pageSize==0){
                pageCount=lineCount/pageSize;
            }else {
                pageCount=lineCount/pageSize+1;
            }

            if(pageNow<=0){pageNow=1;}
            if(pageNow>pageCount){
                pageNow=pageCount;
            }
            psta = conn.prepareStatement("select * from tea limit ?,?");
            psta.setInt(1,pageSize*(pageNow-1));
            psta.setInt(2,pageSize);
            rs = psta.executeQuery();

//        处理结果，遍历rs中所有数据
//        光标向下移动一行，并且判断当前行是否有数据
            while (rs.next()){
                teaAccount account = new teaAccount();

//            获取数据getXXX（）
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String sex= rs.getString(4);
                int tele= rs.getInt(5);

//            赋值
                account.setId(id);
                account.setName(name);
                account.setSex(sex);
                account.setTele(tele);
//            存入集合
                teaList.add(account);
            }
            req.setAttribute("teaList",teaList);

//            req.getRequestDispatcher("/stuInformation.jsp").forward(req,resp);
//            List<Account> list1 = (List<Account>) req.getAttribute("stuList");
//            System.out.println(list1.get(0));

            req.setAttribute("pageSize",pageSize);
            req.setAttribute("lineCount",lineCount);
            req.setAttribute("pageCount",pageCount);
            req.setAttribute("pageNow",pageNow);
            req.getRequestDispatcher("/tea_xinxi/teaInformation.jsp").forward(req, resp);

            psta.close();
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }



    }

}*/
package jdbc;

import com.Account;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.teaAccount;
import org.bson.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/tea")
public class tea extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        MongoClient mongoClient = null;

        //分页
        String n = req.getParameter("n");
        int pageSize = 8;
        int lineCount;
        int pageCount;
        int pageNow = 1;
        if (n != null) {
            pageNow = Integer.parseInt(n);
        }

        try {

            mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));


            MongoDatabase database = mongoClient.getDatabase("cjglxt");


            MongoCollection<Document> collection = database.getCollection("tea");


            int skip = (pageNow - 1) * pageSize;
            int limit = pageSize;


            List<teaAccount> teaList = new ArrayList<>();
            MongoCursor<Document> cursor = collection.find().skip(skip).limit(limit).iterator();

            while (cursor.hasNext()) {
                Document document = cursor.next();

                teaAccount account = new teaAccount();

                account.setId(document.getInteger("tea_id"));
                account.setName(document.getString("tea_name"));
                account.setSex(document.getString("tea_sex"));
                account.setTele(document.getInteger("tea_tele"));
                teaList.add(account);
            }


            lineCount = (int) collection.countDocuments();


            if (lineCount % pageSize == 0) {
                pageCount = lineCount / pageSize;
            } else {
                pageCount = lineCount / pageSize + 1;
            }

            if (pageNow <= 0) {
                pageNow = 1;
            }
            if (pageNow > pageCount) {
                pageNow = pageCount;
            }


            req.setAttribute("teaList", teaList);
            req.setAttribute("pageSize", pageSize);
            req.setAttribute("lineCount", lineCount);
            req.setAttribute("pageCount", pageCount);
            req.setAttribute("pageNow", pageNow);
            req.getRequestDispatcher("/tea_xinxi/teaInformation.jsp").forward(req, resp);

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mongoClient != null) {
                mongoClient.close();
            }
        }

    }
}