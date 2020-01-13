
public class Line {
	
	private boolean v;
	private String tag;
	private int time;
	private String blockOffset;
	
	public Line(){
		this.setV(false);
		this.setTag("");
		this.setTime(0);
		this.setBlockOffset("");
		
	}
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public boolean isV() {
		return v;
	}
	
	public void setV(boolean v) {
		this.v = v;
	}

	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	
	public String getBlockOffset() {
		return blockOffset;
	}
	
	public void setBlockOffset(String blockOffset) {
		this.blockOffset = blockOffset;
	}
	
	public void updateLine(boolean v, String tag, int time, String blockOffset ) {
		setV(v);
		setTag(tag);
		setTime(time);
		setBlockOffset(blockOffset);
	}
	
	public String toString() {
		String vBit="0";
		if(isV()) {
			vBit="1";
		}else {
			vBit="0";
		}
		String str = getTag() + "\t" + getTime() + "\t" + vBit + "\t" + getBlockOffset();
		
		return str;
	}
	
}
