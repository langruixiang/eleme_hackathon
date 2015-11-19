package model;

public class User {
	public int id;
	public String name;
	public String password;
	public User(){
		
	}
	public User(int id,String name,String password){
		this.id = id;
		this.name = name;
		this.password = password;
	}
}
