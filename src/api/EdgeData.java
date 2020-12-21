
package api;

public class EdgeData implements edge_data {
	private int src,dest;
	private double Weight;
	private String info="";
	private int tag;
	

	@Override
	public int getSrc() {
	return src;
	}

	@Override
	public int getDest() {
		return dest;
	}

	@Override
	public double getWeight() {
		return Weight;
	}

	@Override
	public String getInfo() {
		return info;
	}

	@Override
	public void setInfo(String s) {
		this.info=s;
		
	}

	@Override
	public int getTag() {
	return tag;
	}

	@Override
	public void setTag(int t) {
		this.tag=t;
		
	}
	/////////constructors////
	public EdgeData() {
		this.src = 0;
		this.dest = 0;
		this.Weight = 0;
		this.info = "";
		this.tag = -1;
	}

	public EdgeData(int s, int d, double w) {
		this.src = s;
		this.dest = d;
		this.Weight = w;
	}
	public boolean equals(Object obj){
		if((obj instanceof edge_data)) {
			if((Weight==(((edge_data) obj).getWeight())&&(this.src==((edge_data) obj).getSrc())&&(this.dest==((edge_data) obj).getDest())))
			return true;
		}
		return false;
	}
	private class edgloc implements edge_location{
		private edge_data e;
		private double ratio;

		@Override
		public edge_data getEdge() {
			return e;
		}

		@Override
		public double getRatio() {
			return ratio;
		}
		
	}

}
