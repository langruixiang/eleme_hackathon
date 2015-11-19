package dal;

import model.Food;
import model.User;
import server.ConstValue;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
 
public class UserDAL {
	public  String url = "jdbc:mysql://"+ConstValue.DB_HOST+":"+ ConstValue.DB_PORT +"/"+ConstValue.DB_NAME+"?"
            + "user="+ConstValue.DB_USER+"&password=" + ConstValue.DB_PASS+"&useUnicode=true&characterEncoding=UTF8";
	
	//
	public User GetUser(String name,String password){
		Connection conn = null;
		User user = new User();
		user.id = -1;
		user.name = "";
		user.password = "";
		String sql = "";
		try {          
            Class.forName("com.mysql.jdbc.Driver");
//            System.out.println("成功加载MySQL驱动程序");
            // 一个Connection代表一个数据库连接
            conn = DriverManager.getConnection(url);
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return user;
	}
	
	
	public Boolean IsExistUser(String name,String password){
		Connection conn = null;
		User user = new User();
		String sql = "";
		try {          
            Class.forName("com.mysql.jdbc.Driver");
                        // 一个Connection代表一个数据库连接
            conn = DriverManager.getConnection(url);
            // Statement里面带有很多方法，比如executeUpdate可以实现插入，更新和删除等
            Statement stmt = conn.createStatement();            
            sql = "select * from user where name=\""+name+"\" and password=\"" +password+"\"";
            ResultSet rs = stmt.executeQuery(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
            return rs.next();            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return false;
	}
}
