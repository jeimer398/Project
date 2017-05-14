package project;

public class Memory {
	public static int DATA_SIZE = 2048;
	private int[] data = new int[DATA_SIZE];
	private int changedIndex = -1;
	
	int[] getData() {
		return data;
	}
	public void setData(int[] data){
		this.data = data;
	}
	public int getData(int index){
		if(index < 0 || index > DATA_SIZE){
			throw new ArrayIndexOutOfBoundsException("-1");
		}
		return data[index];
	}
	public void setData(int index, int value) {
		data[index] = value;
	}
	
	int getChangedIndex(){
		return changedIndex;
	}
	
	void clear(int start, int end){
		for(int i=start; i<end; i++){
			data[i] = 0;
		}
		changedIndex = -1;
	}
}
