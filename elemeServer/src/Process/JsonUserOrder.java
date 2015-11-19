package Process;

import java.util.List;

public class JsonUserOrder {
	private String id;
	private List<JsonSubFood> items;
	private int total;
	
	public JsonUserOrder(String id,List<JsonSubFood> items,String total){
		this.id = id;
		this.items = items;
		this.total = Integer.parseInt(total);
	}
}
