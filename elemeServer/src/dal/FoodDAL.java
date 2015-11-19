package dal;
import model.Food;
import model.User;
import server.ConstValue;
import model.ConsumeFood;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

import java.util.LinkedList;
import java.util.List;

public class FoodDAL {
	public  static String url = "jdbc:mysql://"+ConstValue.DB_HOST+":"+ ConstValue.DB_PORT +"/"+ConstValue.DB_NAME+"?"
            + "user="+ConstValue.DB_USER+"&password=" + ConstValue.DB_PASS+"&useUnicode=true&characterEncoding=UTF8";	
	//返回所有食物
	public static List<Food> GetFood(){
		Connection conn = null;
		User user = null;
		String sql = "";
		List<Food>allFoods = new LinkedList<Food>();
		try {          
            Class.forName("com.mysql.jdbc.Driver");
//            System.out.println("成功加载MySQL驱动程序");            
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();           
            sql = "select * from food";
            ResultSet rs = stmt.executeQuery(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
            
            while (rs.next()) {
            		Food food = new Food();
            		food.id = rs.getInt(1);
                	food.stock = rs.getInt(2);
                	food.price = rs.getInt(3);      	
                	allFoods.add(food);
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
		return allFoods;
	}
	
	
		//根据ID返回所有食物
		//不存在返回null
		public Food GetFoodById(int id){
			Connection conn = null;
			User user = null;
			String sql = "";
			Food food = new Food();
			try {          
	            Class.forName("com.mysql.jdbc.Driver");
//	            System.out.println("成功加载MySQL驱动程序");            
	            conn = DriverManager.getConnection(url);
	            Statement stmt = conn.createStatement();           
	            sql = "select * from food where id = "+id;
	            ResultSet rs = stmt.executeQuery(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功	            
	            if (rs.next()) {	            		 
	            		food.id = rs.getInt(1);
	                	food.stock = rs.getInt(2);
	                	food.price = rs.getInt(3);      		                	 
	            }
	            else{
	            	return null;
	            }
	        } catch (Exception e) {
	        	food = null;
	            e.printStackTrace();
	        } finally {
	            try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
			return food;
		}
	//stock值减去count。
	//返回修改数据库的行数，-1为操作数据库错误。
	public int UpdateFood(int id,int count){
		Connection conn = null;
		User user = null;
		String sql = "";
		try {          
            Class.forName("com.mysql.jdbc.Driver");
           
            // 一个Connection代表一个数据库连接
            conn = DriverManager.getConnection(url);
            // Statement里面带有很多方法，比如executeUpdate可以实现插入，更新和删除等
            Statement stmt = conn.createStatement();            
            sql = "update food set stock=(stock-" + count+") where id = "+id ;
            int rs = stmt.executeUpdate(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
            return rs;
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
		return -1;
	}
	
	public int UpdataFoods(List<ConsumeFood> consumptionList){
		return 0;
	}
	
	
	//stock值减去count。
	//返回修改数据库的行数，-1为操作数据库错误,-2为超过库存数。
	public int UpdateFood(ConsumeFood cosumption){
		Connection conn = null;
		User user = null;
		String sql = "";
		try {          
            Class.forName("com.mysql.jdbc.Driver");            
            // 一个Connection代表一个数据库连接
            conn = DriverManager.getConnection(url);
            // Statement里面带有很多方法，比如executeUpdate可以实现插入，更新和删除等
            Statement stmt = conn.createStatement();      
            String sqlSelectForUpdate = "select stock from food where id="+cosumption.id +" for update;";
            ResultSet rSet = stmt.executeQuery(sqlSelectForUpdate);
            rSet.next();
//            System.out.println(rSet.getInt(1) - cosumption.cosumeCount);
            if( (rSet.getInt(1) - cosumption.cosumeCount) >= 0){
            	sql = "update food set stock=(stock-" + cosumption.cosumeCount+") where id = "+cosumption.id ;
                int rs = stmt.executeUpdate(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
                return rs;
            }
            else{
            	return -2;
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
		return -1;
	}
	
	
		//stock值减去count。
		//返回修改数据库的行数，-1为操作数据库错误。
		public int UpdateFood(List<ConsumeFood> cosumptions){
			Connection conn = null;
			User user = null;
			String sql = "";
			try {          
	            Class.forName("com.mysql.jdbc.Driver");
	             
	            // 一个Connection代表一个数据库连接
	            conn = DriverManager.getConnection(url);
	            // Statement里面带有很多方法，比如executeUpdate可以实现插入，更新和删除等
	            
	            Statement stmt = conn.createStatement();            
	            int rs = 0;
	            for(ConsumeFood c : cosumptions){
	            	sql = "update food set stock=(stock-" + c.cosumeCount+") where id = "+ c.id ;
		             rs += stmt.executeUpdate(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
	            }
	            return rs;
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
			return -1;
		}
}
