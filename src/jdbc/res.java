/*
package jdbc;

import com.resAccount;
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

@WebServlet("/res")
public class res extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement psta =null;



        //分页
        String n = req.getParameter("n");
        int pageSize = 2;
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

//        创建集合
            List<resAccount> resList = new ArrayList<>();
//新
            psta = conn.prepareStatement("select count(*) from res");
            rs1 = psta.executeQuery();
            rs1.next();
            lineCount = rs1.getInt(1);

            if(lineCount%pageSize==0){
                pageCount=lineCount/pageSize;
            }else {
                pageCount=lineCount/pageSize+1;
            }

            if(pageNow<=0){pageNow=1;}
            if(pageNow>pageCount){
                pageNow=pageCount;
            }
//            psta = conn.prepareStatement("select * from res limit ?,?");
            psta = conn.prepareStatement("SELECT res.res_id,stu.stu_id,stu.stu_name,stu.stu_class,res.sub_name,res.res_num,res.res_term"+
                    " FROM stu JOIN res ON stu.stu_id=res.stu_id limit ?,?");
            psta.setInt(1,pageSize*(pageNow-1));
            psta.setInt(2,pageSize);
            rs1 = psta.executeQuery();

//        处理结果，遍历rs中所有数据
//        光标向下移动一行，并且判断当前行是否有数据
            while (rs1.next()){
                resAccount account = new resAccount();

//            获取数据getXXX（）
                int id = rs1.getInt(1);
                int stuid = rs1.getInt(2);
                String name = rs1.getString(3);
                String cla= rs1.getString(4);
                String subName= rs1.getString(5);
                int num= rs1.getInt(6);
                String trem= rs1.getString(7);
//            赋值
                account.setId(id);
                account.setStuId(stuid);
                account.setName(name);
                account.setCla(cla);
                account.setSubName(subName);
                account.setNum(num);
                account.setTerm(trem);
//            存入集合
                resList.add(account);
            }
            req.setAttribute("resList",resList);

//            req.getRequestDispatcher("/stuInformation.jsp").forward(req,resp);
//            List<Account> list1 = (List<Account>) req.getAttribute("stuList");
//            System.out.println(list1.get(0));

            req.setAttribute("pageSize",pageSize);
            req.setAttribute("lineCount",lineCount);
            req.setAttribute("pageCount",pageCount);
            req.setAttribute("pageNow",pageNow);
            req.getRequestDispatcher("/res_xinxi/resInformation.jsp").forward(req, resp);

            psta.close();
            rs1.close();
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
*/
/*
package jdbc;

import com.Account;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UnwindOptions;
import com.resAccount;
import org.bson.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/res")
public class res extends HttpServlet {
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
            // Connect to MongoDB instance
            mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));

            // Get reference to "cjglxt" database
            MongoDatabase database = mongoClient.getDatabase("cjglxt");

            // Get reference to "res" and "stu" collections
            MongoCollection<Document> resCollection = database.getCollection("res");
            MongoCollection<Document> stuCollection = database.getCollection("stu");

            // Define pagination parameters
            int skip = (pageNow - 1) * pageSize;
            int limit = pageSize;

            // Query the "res" collection with pagination and join "stu" collection
            List<resAccount> list = new ArrayList<>();
            MongoCursor<Document> cursor = resCollection.aggregate(
                    Arrays.asList(
                            Aggregates.skip(skip),
                            Aggregates.limit(limit),
                            Aggregates.lookup("stu", "stu_id", "stu_id", "stuInfo"),
                            Aggregates.unwind("$stuInfo", new UnwindOptions().preserveNullAndEmptyArrays(true)),
                            Aggregates.project(
                                    Projections.fields(
                                            Projections.include("res_id", "stu_id", "res_num", "res_term", "sub_name"),
                                            Projections.computed("stu_name", "$stuInfo.stu_name"),
                                            Projections.computed("stu_class", "$stuInfo.stu_class")
                                    )
                            )
                    )
            ).iterator();

            while (cursor.hasNext()) {
                Document document = cursor.next();

                resAccount account = new resAccount();

                account.setId(document.getInteger("res_id"));
                account.setStuId(document.getInteger("stu_id"));
                account.setName(document.getString("stu_name"));
                account.setCla(document.getString("stu_class"));
                account.setSubName(document.getString("sub_name"));
                account.setNum(document.getInteger("res_num"));
                account.setTerm(document.getString("res_term"));
                list.add(account);
            }

            // Count the total number of documents in the "stu" collection
            lineCount = (int) resCollection.countDocuments();

            // Calculate pagination parameters
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

            // Set request attributes and forward to JSP page
            req.setAttribute("resList", list);
            req.setAttribute("pageSize", pageSize);
            req.setAttribute("lineCount", lineCount);
            req.setAttribute("pageCount", pageCount);
            req.setAttribute("pageNow", pageNow);
            req.getRequestDispatcher("/res_xinxi/resInformation.jsp").forward(req, resp);

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mongoClient != null) {
                mongoClient.close();
            }
        }

    }
}*/
package jdbc;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.resAccount;
import org.bson.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/res")
public class res extends HttpServlet {



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MongoClient mongoClient = null;
        MongoCursor<Document> cursor = null;

        //分页
        String n = req.getParameter("n");
        int pageSize = 8;
        int lineCount;
        int pageCount;
        int pageNow = 1;
        if(n != null){
            pageNow = Integer.parseInt(n);
        }

        try {
            mongoClient = new MongoClient("localhost", 27017);


            MongoDatabase db = mongoClient.getDatabase("cjglxt");


            MongoCollection<Document> collection = db.getCollection("res");

            lineCount = (int) collection.countDocuments();

            if(lineCount % pageSize == 0){
                pageCount = lineCount / pageSize;
            } else {
                pageCount = lineCount / pageSize + 1;
            }

            if(pageNow <= 0){ pageNow = 1; }
            if(pageNow > pageCount){
                pageNow = pageCount;
            }

            //查询数据
            cursor = collection.aggregate(
                    Arrays.asList(
                            new Document("$lookup",
                                    new Document("from", "stu")
                                            .append("localField", "stu_id")
                                            .append("foreignField", "stu_id")
                                            .append("as", "stu_info")
                            ),
                            new Document("$unwind", "$stu_info"),
                            new Document("$project",
                                    new Document("res_id", "$res_id")
                                            .append("stu_id", "$stu_info.stu_id")
                                            .append("stu_name", "$stu_info.stu_name")
                                            .append("stu_class", "$stu_info.stu_class")
                                            .append("sub_name", "$sub_name")
                                            .append("res_num", "$res_num")
                                            .append("res_term", "$res_term")
                            ),
                            new Document("$skip", pageSize * (pageNow - 1)),
                            new Document("$limit", pageSize)
                    )
            ).iterator();

            List<resAccount> resList = new ArrayList<>();
            while(cursor.hasNext()){
                Document doc = cursor.next();
                resAccount account = new resAccount();

                account.setId(doc.getInteger("res_id"));
                account.setStuId(doc.getInteger("stu_id"));
                account.setName(doc.getString("stu_name"));
                account.setCla(doc.getString("stu_class"));
                account.setSubName(doc.getString("sub_name"));
                account.setNum(doc.getInteger("res_num"));
                account.setTerm(doc.getString("res_term"));

                resList.add(account);
            }

            req.setAttribute("resList",resList);
            req.setAttribute("pageSize",pageSize);
            req.setAttribute("lineCount",lineCount);
            req.setAttribute("pageCount",pageCount);
            req.setAttribute("pageNow",pageNow);
            req.getRequestDispatcher("/res_xinxi/resInformation.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (mongoClient != null) {
                mongoClient.close();
            }
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}