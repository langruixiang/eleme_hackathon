package Process;

public class JsonSubFood {
	int food_id;
	int count;
	
	public JsonSubFood(String food_id,String count){
		this.food_id = Integer.parseInt(food_id);
		this.count = Integer.parseInt(count);
	}
	
	@Override
	public String toString(){
		return ";food_id:" + food_id + ";count:" + count;
	}
}
