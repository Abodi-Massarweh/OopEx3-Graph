
package api;

import java.util.HashMap;

public class NodeData implements node_data{
	private geo_location location;
	private static int k=0;
	private int key;
	private double Weight;
	private int tag;
	private boolean visited;
	private HashMap<node_data,edge_data> EdgesOut;
	private HashMap<node_data,edge_data> EdgesIn;
	private String info=" ";
	

	@Override
	public int getKey() {
	 return key;
	}

	@Override
	public geo_location getLocation() {
	  return location;
	}

	@Override
	public void setLocation(geo_location p) {
		this.location=p;
		
	}

	@Override
	public double getWeight() {
		return Weight;
	}

	@Override
	public void setWeight(double w) {
		this.Weight=w;
		
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

	public HashMap<node_data,edge_data> getEdgesOut() {
		return EdgesOut;
	}

	public void setEdgesOut(HashMap<node_data,edge_data> edgesout) {
		EdgesOut = edgesout;
	}
	
	public HashMap<node_data,edge_data> getEdgesIn() {
		return EdgesIn;
	}

	public void setEdgesIn(HashMap<node_data,edge_data> edgesIn) {
		EdgesIn = edgesIn;
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	public boolean isVisited() {
		return visited;
	}
	////////////////constructors///////////
	
    public NodeData() {
    	key=k;
		NodeData.k++;
		this.info = "";
		this.tag = -1;
		this.EdgesOut = new HashMap<node_data,edge_data>();
		this.EdgesIn = new HashMap<node_data,edge_data>();
		this.location = new GeoLoc();
    }
	public NodeData(int key,geo_location g) {
		this.EdgesOut = new HashMap<node_data,edge_data>();
		this.EdgesIn = new HashMap<node_data,edge_data>();
		this.key=key;
		this.Weight = Double.POSITIVE_INFINITY;
		this.tag = -1;
		this.setVisited(false);
		this.location = new GeoLoc(g);
	}
	public NodeData(node_data n) {
		EdgesOut = ((NodeData) n).getEdgesOut();
		this.EdgesIn =((NodeData) n).getEdgesIn();
		this.key=n.getKey();
		this.Weight = ((NodeData) n).getWeight();
		this.tag = n.getTag();
		this.setVisited(((NodeData) n).isVisited());
		this.location = new GeoLoc(n.getLocation());
	}
	@Override
	public String toString() {
		return key+"";
	
	}
	public boolean equals(Object obj) {
		if (obj instanceof NodeData) {
			return ((NodeData) obj).getKey()==key;
		}
		return false;
	}
}

	
