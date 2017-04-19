package project;

public class Memory {
	public static int DATA_SIZE = 2048;
	private int[] data = new int[DATA_SIZE];
	int[] getData() {
		return data;
	}
	void setData(int[] data){
		this.data = data;
	}
	int getData(int index){
		if(index < 0 || index > DATA_SIZE){
			throw new IllegalArgumentException("Out of bounds");
		}
		return data[index];
	}
	void setData(int index, int value) {
		data[index] = value;
	}
}
