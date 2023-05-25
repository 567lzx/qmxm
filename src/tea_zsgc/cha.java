/*package tea_zsgc;

import com.Account;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.teaAccount;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@WebServlet("/teaQuery")
public class cha extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {



        int teaId = Integer.parseInt(req.getParameter("teaId"));


        Properties prop = new Properties();
        prop.load(new FileInputStream("/qmxm/src/druid.properties"));
        DataSource dataSource = null;
        Connection conn = null;
        PreparedStatement pstmt =null;
        ResultSet rs =null;


        //分页
        String n = req.getParameter("n");
        int pageSize = 8;
        int lineCount = 0;
        int pageCount;
        int pageNow=1;
        if(n!=null){
            pageNow = Integer.parseInt(n);
        }


        try {
            dataSource = DruidDataSourceFactory.createDataSource(prop);
            conn = dataSource.getConnection();

            String sql = "SElECT COUNT(*) FROM tea WHERE tea_id LIKE ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%"+teaId+"%");
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()){
                lineCount=resultSet.getInt(1);
            }
            if(lineCount%pageSize==0){
                pageCount=lineCount/pageSize;
            }else {
                pageCount=lineCount/pageSize+1;
            }

            if(pageNow<=0){pageNow=1;}
            if(pageNow>pageCount){
                pageNow=pageCount;
            }



            dataSource = DruidDataSourceFactory.createDataSource(prop);
            conn = dataSource.getConnection();
            String sql1 = "SElECT tea_id,tea_name,tea_sex,tea_tele FROM tea WHERE tea_id LIKE ? limit ?,?";
            pstmt = conn.prepareStatement(sql1);
            pstmt.setString(1,"%"+teaId+"%");
            pstmt.setInt(2,pageSize*(pageNow-1));
            pstmt.setInt(3,pageSize);


            rs = pstmt.executeQuery();
//            lineCount = rs.getMetaData().getColumnCount();
            if(lineCount%pageSize==0){
                pageCount=lineCount/pageSize;
            }else {
                pageCount=lineCount/pageSize+1;
            }

            if(pageNow<=0){pageNow=1;}
            if(pageNow>pageCount){
                pageNow=pageCount;
            }


            List<teaAccount> list = new ArrayList<>();
//        处理结果，遍历rs中所有数据
//        光标向下移动一行，并且判断当前行是否有数据
            while (rs.next()){
                teaAccount account = new teaAccount();

//            获取数据getXXX（）
                int id = rs.getInt("tea_id");
                String name = rs.getString("tea_name");
                String sex= rs.getString("tea_sex");
                int tele= rs.getInt("tea_tele");

//            赋值
                account.setId(id);
                account.setName(name);
                account.setSex(sex);
                account.setTele(tele);
//            存入集合
                list.add(account);
            }
            req.setAttribute("teaList",list);

//            req.getRequestDispatcher("/stuInformation.jsp").forward(req,resp);
//            List<Account> list1 = (List<Account>) req.getAttribute("stuList");
//            System.out.println(list1.get(0));

            req.setAttribute("pageSize",pageSize);
            req.setAttribute("lineCount",lineCount);
            req.setAttribute("pageCount",pageCount);
            req.setAttribute("pageNow",pageNow);
            req.setAttribute("lastQuery",teaId);
            req.getRequestDispatcher("/tea_xinxi/teaQuery.jsp").forward(req, resp);


        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
import java.util.regex.Pattern;

@WebServlet("/teaQuery")
public class cha extends HttpServlet {
    /*Servlet通过一个POST请求接收表单数据，包括教师姓名、性别、电话、id。*/
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String teaName = req.getParameter("teaName");

        //分页
        String n = req.getParameter("n");
        int pageSize = 8;
        int lineCount = 0;
        int pageCount;
        int pageNow=1;
        if(n!=null){
            pageNow = Integer.parseInt(n);
        }

        // 创建MongoDB客户端连接
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            // 获取MongoDB数据库实例
            MongoDatabase database = mongoClient.getDatabase("cjglxt");
            // 获取集合
            MongoCollection<Document> collection = database.getCollection("tea");
            /*根据教师姓名使用正则表达式构造一个查询过滤器，并使用该过滤器查询符合条件的文档数量。*/
            Pattern pattern = Pattern.compile("^.*" + teaName + ".*$", Pattern.CASE_INSENSITIVE);
            Document filter = new Document("tea_name", pattern);
            // 查询符合条件的文档数量
            lineCount = (int) collection.countDocuments(filter);
            // 计算分页相关参数，并使用MongoDB集合的find()方法查询符合条件的文档并进行分页。
            if(lineCount%pageSize==0){
                pageCount=lineCount/pageSize;
            }else {
                pageCount=lineCount/pageSize+1;
            }
            if(pageNow<=0){pageNow=1;}
            if(pageNow>pageCount){
                pageNow=pageCount;
            }

            MongoCursor<Document> cursor = collection.find(filter)
                    .skip(pageSize*(pageNow-1))
                    .limit(pageSize)
                    .iterator();
            List<teaAccount> list = new ArrayList<>();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                teaAccount account = new teaAccount();
                // 从Document中获取数据，查询结果被封装成一个包含教师信息的List集合，并存储到请求属性中。
                int id = document.getInteger("tea_id");
                String name = document.getString("tea_name");
                String sex= document.getString("tea_sex");
                int tele= document.getInteger("tea_tele");
                // 赋值
                account.setId(id);
                account.setName(name);
                account.setSex(sex);
                account.setTele(tele);
                // 存入集合
                list.add(account);
            }
            //将分页相关参数和上一次查询的教师姓名存储到请求属性中，并将请求转发到JSP页面以展示查询结果。
            req.setAttribute("teaList",list);
            req.setAttribute("pageSize",pageSize);
            req.setAttribute("lineCount",lineCount);
            req.setAttribute("pageCount",pageCount);
            req.setAttribute("pageNow",pageNow);
            req.setAttribute("lastQuery",teaName);
            req.getRequestDispatcher("/tea_xinxi/teaQuery.jsp").forward(req, resp);
        }
    }
}