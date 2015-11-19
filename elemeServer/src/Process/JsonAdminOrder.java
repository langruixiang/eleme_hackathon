package Process;

import java.util.List;

public class JsonAdminOrder {
	private String id;
	private String user_id;
	private List<JsonSubFood> items;
	private int total;
	
	public JsonAdminOrder(String id,String user_id,List<JsonSubFood> items,String total){
		this.id = id;
		this.user_id = user_id;
		this.items = items;
		this.total = Integer.parseInt(total);
	}
	
    public String getID(){
    	return this.id;
    }
	
	public List<JsonSubFood> getItems() {
		return items;
	}

	public int getTotal() {
		return total;
	}

	@Override
	public String toString(){
		return "id:" + id + ";user_id:" + user_id + ";items:" + items.toString() + ";total:" + total + "\n"; 
	}
}
