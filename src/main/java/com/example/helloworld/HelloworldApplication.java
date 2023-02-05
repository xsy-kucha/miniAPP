package com.example.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import javax.smartcardio.ATR;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
// import java.lang.Thread;

@SpringBootApplication
public class HelloworldApplication {

    @RestController
    class HelloworldController {
        @CrossOrigin(origins = "*")
        @RequestMapping(value = "/getOpenId", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
        @ResponseBody
        public String getOpenId(@RequestBody Map<String, String> paramlist) {
            String code = paramlist.get("code");
            String http ="https://api.weixin.qq.com/sns/jscode2session?appid=wx0e2e13b55106bdd6&secret=ac57b381b7c26bfa41b6b60daf2b8edb&js_code=" + code + "&grant_type=authorization_code";
            try {
                URL url = new URL(http);
                URLConnection conn = null;
                conn = url.openConnection();
                InputStream is = conn.getInputStream() ;
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String text = br.readLine() ;
                System.out.println(text);
                System.out.println(text.substring(text.indexOf("openid\":\"") + 9,text.indexOf("\"}")));
                return text.substring(text.indexOf("openid\":\"") + 9, text.indexOf("\"}"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        @CrossOrigin(origins = "*")
        @RequestMapping(value = "/insert", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
        @ResponseBody
        public int insert(@RequestBody Map<String, String> paramlist) {
            String driver ="com.mysql.cj.jdbc.Driver";
            String url ="jdbc:mysql://sh-cynosdbmysql-grp-dpkro7ma.sql.tencentcdb.com:28243/springboot_demo";
            String username= "root";
            String password="xR3xN4cG";
            try {
                //4、执行SQL
                String sql = "insert into info(name,sex,age,height,academic,workName,nativePlace,workPlace,salary,haveCar,haveHouse,car,house,profile,createTime,updateTime,havemarry,photo,number) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                //1、加载驱动
                Class.forName(driver);
                //2、创建连接
                Connection conn= DriverManager.getConnection(url,username,password);
                //3、创建Statement：执行SQL
                Date date = new Date();
                PreparedStatement pst=conn.prepareStatement(sql);
                pst.setString(1,paramlist.get("name"));
                pst.setString(2,paramlist.get("sex"));
                pst.setString(3,paramlist.get("age"));
                pst.setString(4,paramlist.get("height"));
                pst.setString(5,paramlist.get("academic"));
                pst.setString(6,paramlist.get("workName"));
                pst.setString(7,paramlist.get("nativePlace"));
                pst.setString(8,paramlist.get("workPlace"));
                pst.setString(9,paramlist.get("salary"));
                pst.setString(10,paramlist.get("haveCar"));
                pst.setString(11,paramlist.get("haveHouse"));
                pst.setString(12,paramlist.get("car"));
                pst.setString(13,paramlist.get("house"));
                pst.setString(14,paramlist.get("profile"));
                pst.setString(15,date.toString());
                pst.setString(16,date.toString());
                pst.setString(17,paramlist.get("haveMarry"));
                pst.setString(18,paramlist.get("photo"));
                pst.setString(19,paramlist.get("number"));
                //执行SQL返回影响行数
                int rows =pst.executeUpdate();
                System.out.println(">>>>>rows:"+rows);

                //5、关闭释放连接
                pst.close();
                conn.close();
                return  rows;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }


        @CrossOrigin(origins = "*")
        @RequestMapping(value = "/queryAll", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
        @ResponseBody
        public List<Map<String, String>> queryAll(@RequestBody Map<String, Integer> paramlist) {
            int pageId =paramlist.get("pageId");
            int len = paramlist.get("len");
            List<Map<String,String>> res = new ArrayList<>();
            //默认page长度为10
            String driver ="com.mysql.cj.jdbc.Driver";
            String url ="jdbc:mysql://sh-cynosdbmysql-grp-dpkro7ma.sql.tencentcdb.com:28243/springboot_demo";
            String username= "root";
            String password="xR3xN4cG";
            try {
                //(name,sex,age,height,academic,workName,nativePlace,workPlace,salary,haveCar,haveHouse,car,house,profile,createTime,updateTime,havemarry)
                //4、执行SQL
                String sql = "select * from info where 1=1 limit ?, ?";
                //1、加载驱动
                Class.forName(driver);
                //2、创建连接
                Connection conn= DriverManager.getConnection(url,username,password);
                //3、创建Statement：执行SQL
                Date date = new Date();
                PreparedStatement pst=conn.prepareStatement(sql);
                pst.setInt(1,pageId*len);
                pst.setInt(2,len);
                //执行SQL返回影响行数
                ResultSet rst =pst.executeQuery();
                while(rst.next()){
                    Map<String, String> temp = new HashMap<>();
                    temp.put("id", String.valueOf(rst.getInt(1)));
                    temp.put("name", rst.getString(2));
                    temp.put("age", rst.getString(3));
                    temp.put("height", rst.getString(4));
                    temp.put("academic", rst.getString(5));
                    temp.put("nativePlace", rst.getString(6));
                    temp.put("workPlace", rst.getString(7));
                    temp.put("salary", rst.getString(8));
                    temp.put("haveCar", rst.getString(9));
                    temp.put("haveHouse", rst.getString(10));
                    temp.put("car", rst.getString(11));
                    temp.put("house", rst.getString(12));
                    temp.put("profile", rst.getString(13));
                    temp.put("haveMarry", rst.getString(14));
                    temp.put("createTime", rst.getString(15));
                    temp.put("updateTime", rst.getString(16));
                    temp.put("sex", rst.getString(17));
                    temp.put("workName", rst.getString(18));
                    temp.put("photo", rst.getString(19));
                    temp.put("number", rst.getString(20));
                    res.add(temp);
                    System.out.println(rst.getInt(1));
                }

                //5、关闭释放连接
                pst.close();
                conn.close();
                rst.close();
                return res;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }




        @CrossOrigin(origins = "*")
        @RequestMapping(value = "/querySift", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
        @ResponseBody
        public List<Map<String, String>> querySift(@RequestBody Map<String, String> paramList) {
            System.out.println(paramList);
            ArrayList<String> academicList = new ArrayList<>(Arrays.asList("初中", "高中", "中专", "大专", "本科", "研究生", "博士", "博士后"));
            int pageId = Integer.parseInt(paramList.get("pageId"));
            int len = Integer.parseInt(paramList.get("len"));


            //学历筛选
            String academicMax = (paramList.get("academicMax").equals("最高学历")) ? "博士后" : paramList.get("academicMax");
            String academicMin = (paramList.get("academicMin").equals("最低学历")) ? "初中" : paramList.get("academicMin");
            String academicQueryString = "";
            int firstAddFlag = 0;
            if(paramList.get("academicMax").equals("最高学历") && paramList.get("academicMin").equals("最低学历")){
                academicQueryString = "";
            }
            else{
                academicQueryString = " academic in ( ";
                System.out.println(academicList.indexOf(academicMin));
                System.out.println(academicList.indexOf(academicMax));
                int flag = 0;
                for(int i = academicList.indexOf(academicMin); i <= academicList.indexOf(academicMax); i++){
                    if(flag == 0){
                        academicQueryString = academicQueryString  + "'" + academicList.get(i) + "'";
                        flag = 1;
                    }
                    else{
                        academicQueryString = academicQueryString  + ",'" + academicList.get(i) + "'";
                    }
                }
                academicQueryString = academicQueryString + ")";
            }

            //年龄筛选
            String ageMax = (paramList.get("ageMax").equals("最大值")) ? "1980" : paramList.get("ageMax");
            String ageMin = (paramList.get("ageMin").equals("最小值")) ? "2010" : paramList.get("ageMin");
            String ageQueryString = "";
            if(ageMin.equals("1980") && ageMin.equals("2010")){
                ageQueryString = "";
            }
            else{
                int flag = 0;
                ageQueryString = " age in ( ";
                for(int i = Integer.parseInt(ageMax); i <= Integer.parseInt(ageMin); i++){
                    if(flag == 0){
                        flag = 1;
                        ageQueryString = ageQueryString +  "'" + i + "'";
                    }
                    else{
                        ageQueryString = ageQueryString + ",'" + i + "'";
                    }
                }
                ageQueryString = ageQueryString + ")";
            }


            //身高筛选
            String heightMax = (paramList.get("heightMax").equals("最大值")) ? "199" : paramList.get("heightMax");
            String heightMin = (paramList.get("heightMin").equals("最小值")) ? "145" : paramList.get("heightMin");
            String heightQueryString = "";
            if(heightMin.equals("145") && heightMin.equals("199")){
                heightQueryString = "";
            }
            else{
                int flag = 0;
                heightQueryString = " height in ( ";
                for(int i = Integer.parseInt(heightMin); i <= Integer.parseInt(heightMax); i++){
                    if(flag == 0){
                        flag = 1;
                        heightQueryString = heightQueryString +  "'" + i + "'";
                    }
                    else{
                        heightQueryString = heightQueryString + ",'" + i + "'";
                    }
                }
                heightQueryString = heightQueryString + ")";
            }

            //地区筛选
            String nativePlace = paramList.get("nativePlace");
            String nativePlaceQueryString = "";
            ArrayList<String> nativePlaceList = new ArrayList<>(Arrays.asList(nativePlace.split("-")));
            if(nativePlaceList.get(0).equals("选择省")){
                nativePlaceQueryString = "";
            }
            else{
                nativePlaceQueryString = " nativePlace like '%";
                for(int i = 0; i < nativePlaceList.size(); i++){
                    if(nativePlaceList.get(i).equals("全部")){
                        nativePlaceQueryString = nativePlaceQueryString + "%' ";
                        break;
                    }
                    else{
                        nativePlaceQueryString = nativePlaceQueryString + nativePlaceList.get(i);
                    }
                }
            }

            //性别筛选
            String sex = paramList.get("sex");
            String sexQueryString = "";
            if( sex.equals("")){
                sexQueryString = "";
            }
            else{
                sexQueryString = "sex = '" + sex + "'";
            }



            String sql = "select * from info ";
            if (academicQueryString.length() != 0){
                if(firstAddFlag == 0){
                    sql = sql + " where " + academicQueryString;
                    firstAddFlag = 1;
                }
                else{
                    sql = sql + " and " + academicQueryString;
                }
            }

            if(ageQueryString.length() != 0){
                if(firstAddFlag == 0){
                    sql = sql + " where " + ageQueryString;
                    firstAddFlag = 1;
                }
                else{
                    sql = sql + " and " + ageQueryString;
                }
            }

            if(heightQueryString.length() != 0){
                if(firstAddFlag == 0){
                    sql = sql + " where " + heightQueryString;
                    firstAddFlag = 1;
                }
                else{
                    sql = sql + " and " + heightQueryString;
                }
            }

            if(nativePlaceQueryString.length() != 0){
                if(firstAddFlag == 0){
                    sql = sql + " where " + nativePlaceQueryString;
                    firstAddFlag = 1;
                }
                else{
                    sql = sql + " and " + nativePlaceQueryString;
                }
            }

            if(sexQueryString.length() != 0){
                if(firstAddFlag == 0){
                    sql = sql + " where " + sexQueryString;
                    firstAddFlag = 1;
                }
                else{
                    sql = sql + " and " + sexQueryString;
                }
            }

            List<Map<String,String>> res = new ArrayList<>();
            //默认page长度为10
            String driver ="com.mysql.cj.jdbc.Driver";
            String url ="jdbc:mysql://sh-cynosdbmysql-grp-dpkro7ma.sql.tencentcdb.com:28243/springboot_demo";
            String username= "root";
            String password="xR3xN4cG";
            try {
                System.out.println(sql);
                //(name,sex,age,height,academic,workName,nativePlace,workPlace,salary,haveCar,haveHouse,car,house,profile,createTime,updateTime,havemarry)
                //4、执行SQL
//                String sql = "select * from info where id >= ? and id < ?";
                //1、加载驱动
                Class.forName(driver);
                //2、创建连接
                Connection conn= DriverManager.getConnection(url,username,password);
                //3、创建Statement：执行SQL
                Date date = new Date();
                PreparedStatement pst=conn.prepareStatement(sql);
                //执行SQL返回影响行数
                ResultSet rst =pst.executeQuery();
                while(rst.next()){
                    Map<String, String> temp = new HashMap<>();
                    temp.put("id", String.valueOf(rst.getInt(1)));
                    temp.put("name", rst.getString(2));
                    temp.put("age", rst.getString(3));
                    temp.put("height", rst.getString(4));
                    temp.put("academic", rst.getString(5));
                    temp.put("nativePlace", rst.getString(6));
                    temp.put("workPlace", rst.getString(7));
                    temp.put("salary", rst.getString(8));
                    temp.put("haveCar", rst.getString(9));
                    temp.put("haveHouse", rst.getString(10));
                    temp.put("car", rst.getString(11));
                    temp.put("house", rst.getString(12));
                    temp.put("profile", rst.getString(13));
                    temp.put("haveMarry", rst.getString(14));
                    temp.put("createTime", rst.getString(15));
                    temp.put("updateTime", rst.getString(16));
                    temp.put("sex", rst.getString(17));
                    temp.put("workName", rst.getString(18));
                    temp.put("photo", rst.getString(19));
                    temp.put("number", rst.getString(20));
                    res.add(temp);
                    System.out.println(rst.getInt(1));
                }

                //5、关闭释放连接
                pst.close();
                conn.close();
                rst.close();
                return res;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

	public static void main(String[] args) {
		SpringApplication.run(HelloworldApplication.class, args);
	}

}
