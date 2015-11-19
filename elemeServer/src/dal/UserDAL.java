package dal;

import model.Food;
import model.User;
import server.ConstValue;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
 
public class UserDAL {
	public static String url = "jdbc:mysql://"+ConstValue.DB_HOST+":"+ ConstValue.DB_PORT +"/"+ConstValue.DB_NAME+"?"
            + "user="+ConstValue.DB_USER+"&password=" + ConstValue.DB_PASS+"&useUnicode=true&characterEncoding=UTF8";
	
	//
	public User GetUser(String name,String password){
		MySQLPool pool = MySQLPool.getInstance();
		Connection conn = pool.getConnection();
		User user = new User();
		user.id = -1;
		user.name = "";
		user.password = "";
		String sql = "";
		try {          
            // Statement里面带有很多方法，比如executeUpdate可以实现插入，更新和删除等
            Statement stmt = conn.createStatement();            
            sql = "select * from user where name=\""+name+"\" and password=\"" +password+"\"";
            ResultSet rs = stmt.executeQuery(sql);//executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
                while (rs.next()) {
                	user.id = rs.getInt(1);
                	user.name = rs.getString(2);
                	user.password = rs.getString(3);       	
                }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	pool.releaseConnection(conn);
        }
		return user;
	}
	
	public static List<User> GetAllUser(){
		MySQLPool pool = MySQLPool.getInstance();
		Connection conn = pool.getConnection();
		List<User> userList = new LinkedList<User>();
		String sql = "";
		try {                      
            Statement stmt = conn.createStatement();            
            sql = "select * from user";
            ResultSet rs = stmt.executeQuery(sql);//executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
               while (rs.next()) {                	
                	userList.add(new User(rs.getInt(1), rs.getString(2),rs.getString(3)));  	
             }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	pool.releaseConnection(conn);
        }
		return userList;
	}
	
	public Boolean IsExistUser(String name,String password){
		MySQLPool pool = MySQLPool.getInstance();
		Connection conn = pool.getConnection();
		String sql = "";
		try {          
            Statement stmt = conn.createStatement();            
            sql = "select * from user where name=\""+name+"\" and password=\"" +password+"\"";
            ResultSet rs = stmt.executeQuery(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
            return rs.next();            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	pool.releaseConnection(conn);
        }
		return false;
	}
}
