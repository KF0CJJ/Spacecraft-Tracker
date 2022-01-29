
public class SpaceCraft {
	private String name;
	private String nickName;
	private int freq;
	public SpaceCraft(String name, String nickName, int freq) {
		this.name = name;
		this.nickName = nickName;
		this.freq = freq;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getFreq() {
		return freq;
	}
	public void setFreq(int freq) {
		this.freq = freq;
	}
	
}
