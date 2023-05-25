/*
package res_zsgc;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.resAccount;

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

@WebServlet("/cjcx")
public class cjcx extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int stuId = Integer.parseInt(req.getParameter("stuId"));


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

            String sql = "SElECT COUNT(*) FROM res WHERE stu_id LIKE ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%"+stuId+"%");
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
            String sql1 = "SELECT res.res_id,stu.stu_id,stu.stu_name,stu.stu_class,res.sub_name,res.res_num,res.res_term" +
                    " FROM stu JOIN res ON stu.stu_id=res.stu_id WHERE res.stu_id LIKE ? limit ?,?";
            pstmt = conn.prepareStatement(sql1);
            pstmt.setString(1,"%"+stuId+"%");
            pstmt.setInt(2,pageSize*(pageNow-1));
            pstmt.setInt(3,pageSize);


            rs = pstmt.executeQuery();
//            lineCount = rs.getMetaData().getColumnCount();


            List<resAccount> reslist = new ArrayList<>();
//        处理结果，遍历rs中所有数据
//        光标向下移动一行，并且判断当前行是否有数据
            while (rs.next()){
                resAccount account = new resAccount();

//            获取数据getXXX（）
                int id = rs.getInt(1);
                int stuid = rs.getInt(2);
                String name = rs.getString(3);
                String cla= rs.getString(4);
                String subName= rs.getString(5);
                int num= rs.getInt(6);
                String trem= rs.getString(7);
//            赋值
                account.setId(id);
                account.setStuId(stuid);
                account.setName(name);
                account.setCla(cla);
                account.setSubName(subName);
                account.setNum(num);
                account.setTerm(trem);
//            存入集合

                reslist.add(account);
            }
            req.setAttribute("resList",reslist);

//            req.getRequestDispatcher("/stuInformation.jsp").forward(req,resp);
//            List<Account> list1 = (List<Account>) req.getAttribute("stuList");
//            System.out.println(list1.get(0));

            req.setAttribute("pageSize",pageSize);
            req.setAttribute("lineCount",lineCount);
            req.setAttribute("pageCount",pageCount);
            req.setAttribute("pageNow",pageNow);
            req.setAttribute("lastQuery",stuId);
            req.getRequestDispatcher("/res_xinxi/cjcx.jsp").forward(req, resp);


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
}
*/
package res_zsgc;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
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
import java.util.List;
import java.util.regex.Pattern;
/*和查询一样的*/
@WebServlet("/cjcx")
public class cjcx extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String stuName = req.getParameter("stuName");
        MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("cjglxt");
        MongoCollection<Document> stuCollection = database.getCollection("stu");
        MongoCollection<Document> resCollection = database.getCollection("res");

        String n = req.getParameter("n");
        int pageSize = 8;
        int lineCount = 0;
        int pageCount;
        int pageNow = 1;
        if (n != null) {
            pageNow = Integer.parseInt(n);
        }

        try {
            Pattern pattern = Pattern.compile("^.*" + stuName + ".*$", Pattern.CASE_INSENSITIVE);
            Document filter = new Document("stu_name", pattern);
            lineCount = (int) stuCollection.countDocuments(filter);
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

            List<resAccount> reslist = new ArrayList<>();
            MongoCursor<Document> cursor = stuCollection.find(filter).iterator();
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                int stuId = doc.getInteger("stu_id");
                String name = doc.getString("stu_name");
                String cla = doc.getString("stu_class");

                MongoCursor<Document> resCursor = resCollection.find(new Document("stu_id", stuId)).iterator();
                while (resCursor.hasNext()) {
                    Document resDoc = resCursor.next();
                    int id = resDoc.getInteger("res_id");
                    String subName = resDoc.getString("sub_name");
                    int num = resDoc.getInteger("res_num");
                    String term = resDoc.getString("res_term");
                    resAccount account = new resAccount();
                    account.setId(id);
                    account.setStuId(stuId);
                    account.setName(name);
                    account.setCla(cla);
                    account.setSubName(subName);
                    account.setNum(num);
                    account.setTerm(term);
                    reslist.add(account);
                }
            }

            req.setAttribute("resList", reslist);
            req.setAttribute("pageSize", pageSize);
            req.setAttribute("lineCount", lineCount);
            req.setAttribute("pageCount", pageCount);
            req.setAttribute("pageNow", pageNow);
            req.setAttribute("lastQuery", stuName);
            req.getRequestDispatcher("/res_xinxi/cjcx.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
        }

        mongoClient.close();
    }

}