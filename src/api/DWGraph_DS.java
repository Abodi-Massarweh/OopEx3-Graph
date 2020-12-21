
package api;

import java.util.Collection;
import java.util.HashMap;



public class DWGraph_DS implements directed_weighted_graph{
	private int mc;
	private int edgeSize;
	private HashMap<Integer, node_data> Vertices;

	@Override
	public node_data getNode(int key) {
		return this.Vertices.get(key);
	}
	

	@Override
	public edge_data getEdge(int src, int dest) {
	   if (getNode(src)==null||getNode(dest)==null) return null;
	   if(!((NodeData)getNode(src)).getEdgesOut().containsKey(getNode(dest)))return null;
	   return ((NodeData)getNode(src)).getEdgesOut().get(getNode(dest));
	}

	@Override
	public void addNode(node_data n) {
	 if(Vertices.containsKey(n.getKey()))return;
	  Vertices.put(n.getKey(), n);
	  mc++;
	}

	@Override
	public void connect(int src, int dest, double w) {
		if(getNode(src)==null || getNode(dest)==null) return;
		if(src==dest) return;
		if(!((NodeData)getNode(src)).getEdgesOut().containsKey(getNode(dest))) { 
		edgeSize++ ;
		mc++;
		}
		else{
		if(((NodeData)getNode(src)).getEdgesOut().get(getNode(dest)).getWeight()!=w) mc++;
		}
		EdgeData e = new EdgeData(src,dest,w);
		((NodeData)getNode(src)).getEdgesOut().put(getNode(dest),e);
		((NodeData)getNode(dest)).getEdgesIn().put(getNode(src),e);

	}

	@Override
	public Collection<node_data> getV() {
		return Vertices.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		if(getNode(node_id)==null)return null;
		return ((NodeData)getNode(node_id)).getEdgesOut().values();
	}

	@Override
	public node_data removeNode(int key) {
		node_data removedData = Vertices.get(key);
		if(removedData == null) {
			return removedData;
		}
		else {
			if(((NodeData) removedData).getEdgesOut()!= null){
				for(edge_data temp : getE(key)) {
					removeEdge(key, temp.getDest());// remove the key from his neighbors
				}
			}
			if(((NodeData) removedData).getEdgesIn()!= null) {
				Collection<edge_data> in= ((NodeData)getNode(key)).getEdgesIn().values();
				for(edge_data e: in) {
					removeEdge(e.getSrc(),key);
				}
			}
			
			
			Vertices.remove(key);//remove the node
			mc++;
		}
		return removedData;
	}


	@Override
	public edge_data removeEdge(int src, int dest) {
	if(getNode(src)==null||getNode(dest)==null)return null;
   if(((NodeData)getNode(src)).getEdgesOut().containsKey(getNode(dest))) {
	edge_data remove =((NodeData)getNode(src)).getEdgesOut().get(getNode(dest));
	if(remove!=null){
		((NodeData)getNode(src)).getEdgesOut().remove(getNode(dest));
		mc++;
		edgeSize--;
		return remove;
	}
   }
	return null;
	}

	@Override
	public int nodeSize() {
		return Vertices.size();
	}

	@Override
	public int edgeSize() {
		return edgeSize;
	}

	@Override
	public int getMC() {
		return mc;
	}
	public DWGraph_DS() {
		Vertices=new HashMap<Integer, node_data>();
		edgeSize=0;
		mc=0;
	}
	public boolean equals(Object g1) {
		if(g1 instanceof directed_weighted_graph) {
		DWGraph_DS g0 = (DWGraph_DS) g1;
		for(node_data n1 : this.getV()) {
			if(!g0.Vertices.containsValue(n1)) return false;
		}	
		for(node_data n1 : ((DWGraph_DS) g1).getV()) {
			if(!this.Vertices.containsValue(n1)) return false;
	    }
	 	 if(!checkedges(this.Vertices,g0)||!checkedges(g0.Vertices,this)) return false;
		}	
		return true;
	}
	private boolean checkedges(HashMap<Integer,node_data> g1, DWGraph_DS g2) {
		Collection<Integer> col1 = g1.keySet();
		for(int n : col1) {
			for(node_data n1 : ((NodeData) g1.get(n)).getEdgesOut().keySet()) {
				double weight = ((NodeData) g1.get(n)).getEdgesOut().get(n1).getWeight();
				if((g2.getEdge(n, n1.getKey())==null)||
					g2.getEdge(n, n1.getKey()).getWeight()!=weight)return false;
		
			}	
		}
		return true;
	}

}
