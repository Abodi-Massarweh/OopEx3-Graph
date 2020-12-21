
package api;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import com.google.gson.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;


public class DWGraph_Algo implements dw_graph_algorithms {
	private directed_weighted_graph gr;

	@Override
	public void init(directed_weighted_graph g) {
		this.gr=g;
		
	}

	@Override
	public directed_weighted_graph getGraph() {
		return gr;
	}

	@Override
	public directed_weighted_graph copy() {
		directed_weighted_graph copygr= new DWGraph_DS();
		for (node_data n : gr.getV()) {
			if (n != null) { // create new node for deep copy
				node_data u = new NodeData(n.getKey(), n.getLocation());
				copygr.addNode(u);
			}
		}
		for(node_data node : gr.getV()){
			for(edge_data e : gr.getE(node.getKey())) {
				if(e!=null) {
					copygr.connect(e.getSrc(), e.getDest(),e.getWeight());
				}
			}
		}
		return copygr;
	}

	@Override
	public boolean isConnected() {
		if (this.getGraph().nodeSize() == 0 || this.getGraph().nodeSize() == 1) {//if there is not vertices or there is one vertex it's mean that the graph is connected
			return true;
		}
		directed_weighted_graph transGraph=Transpose();
		node_data first = gr.getV().iterator().next();//to find the first node
		for(node_data n1 : gr.getV()) {
			n1.setInfo(" ");
		}
		for(node_data n1 : transGraph.getV()) {
			n1.setInfo(" ");
		}
		VisitVertex(first, first, gr);
		for(node_data n : gr.getV()) {
			if(!n.getInfo().equals("1"))//the vertex is not visited
				return false;
		}
		VisitVertex(first, first,transGraph);
		for(node_data n : transGraph.getV()) {
			if(!n.getInfo().equals("1"))//the vertex is not visited
				return false;
		}
		
		return true;
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		if(src==dest)return 0;
		if(gr.getNode(src)==null || gr.getNode(dest)==null)return -1;
		dijkstra(src, dest);//to find the minimum weight 
		if(((NodeData)gr.getNode(dest)).getWeight()==Double.POSITIVE_INFINITY) return -1;
		return ((NodeData)gr.getNode(dest)).getWeight();//return minimum weight
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		List<node_data> path = new LinkedList<node_data>();
		node_data temp = gr.getNode(dest);
		if(src==dest) {
			path.add(temp);
			return path;
		}
		if(gr.getNode(src)==null || gr.getNode(dest)==null)return null;
		dijkstra(src, dest);
		try {
			while (temp.getKey() != src) {// begin from dest-node to src-node
				path.add(temp);
				temp = gr.getNode((int)temp.getTag());
			}
			path.add(temp);// to add src node
			int index = path.size() - 1;
			List<node_data> rev = new LinkedList<node_data>();
			rev.add(temp);
			index--;
			while (index != 0) {
				rev.add(path.get(index));
				index--;
			}
			rev.add(path.get(0));
			return rev;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean save(String file) {
		 JsonObject gr_gson= new JsonObject();
		 int nodes_size=-1;
	     JsonArray nodes= new JsonArray();
	     JsonArray edges= new JsonArray();
	       for(node_data n: gr.getV()) {
	            JsonObject gson= new JsonObject();
	            gson.addProperty("id", n.getKey());
	            if(nodes_size<nodes.size()) {
	            	nodes_size=nodes.size()*2;
	            }
	            String position= n.getLocation().x()+","+n.getLocation().y()+","+n.getLocation().z();
	            gson.addProperty("pos", position);
	            if(nodes_size>nodes.size()) {
	            	nodes_size=nodes.size();
	            }
	            nodes.add(gson);
	        }
	       nodes_size=nodes.size();
	        gr_gson.add("Nodes", nodes);
	        for(node_data n: (((DWGraph_DS)(gr)).getV())) {
	        	for (edge_data e : gr.getE(n.getKey())) {
		            if(nodes_size<nodes.size()) {
		            	nodes_size=nodes.size()*2;
		            }
	        	     JsonObject gson= new JsonObject();
	        	    gson.addProperty("src", e.getSrc());
	 	            gson.addProperty("dest", e.getDest());
		            if(nodes_size>nodes.size()) {
		            	nodes_size=nodes.size();
		            }
	 	            gson.addProperty("w", n.getWeight());
	 	            edges.add(gson);
				}
	        }
	          gr_gson.add("Edges", edges);       
	      		try {
	      			double w=0;
	                Gson gson = new Gson();
	      			PrintWriter pw = new PrintWriter(new File(file));
	      			if(w<2) {
	      				w=nodes.size()*2;
	      			}
	      			pw.write(gson.toJson(gr_gson));
	      			pw.close();
	      		} 
	      		catch (FileNotFoundException e) {
	      			e.printStackTrace();
	      			return false;
	      		}
	            return true;
	}
	

	@Override
	public boolean load(String file) {
		int sum=0;
	    GsonBuilder gsonbuild = new GsonBuilder().registerTypeAdapter(DWGraph_DS.class, new Deserializer());
        Gson gson = gsonbuild.create();
    	try
    	{			
    	FileReader read = new FileReader(file);
        for (int j = 0; j < this.gr.edgeSize(); j++) {
			sum++;
		}
        if(sum<100) {
        	sum=this.gr.edgeSize();
        }
    	gr = gson.fromJson(read,DWGraph_DS.class);
    	} 
    	catch (FileNotFoundException e) {
    	e.printStackTrace();
         return false;
                }
    	return true;
	}
////////////private funtions//////////
	
	private directed_weighted_graph Transpose() {
		directed_weighted_graph T=new DWGraph_DS();
		for (node_data u : gr.getV()) {
			node_data q = new NodeData(u.getKey(), u.getLocation());
			T.addNode(q);
		}
		for (node_data n : gr.getV()) {
			if (gr.getE(n.getKey()) != null) {
				for (edge_data e : gr.getE(n.getKey())) {
					T.connect(e.getDest(), e.getSrc(), e.getWeight());
				}
			}
		}
		return T;
	}
	private void VisitVertex(node_data n, node_data start,directed_weighted_graph g) {
		if (g.getE(n.getKey()) != null) {
			for (edge_data e : g.getE(n.getKey())) {
				if (!g.getNode(e.getDest()).getInfo().equals("1")) {
					n.setInfo("1");
					g.getNode(e.getDest()).setInfo("1");
					VisitVertex(g.getNode(e.getDest()), start,g);//do recursivly to neighbour of the node
				}
			}
		}
	}
	private void dijkstra(int src, int dest) {
		for(node_data n1 : gr.getV()) {
			((NodeData)n1).setTag(-1);
			((NodeData)n1).setWeight(Double.POSITIVE_INFINITY);
			((NodeData)n1).setVisited(false);
		}
		Collection<node_data> ni = gr.getV();
		MinHeap h = new MinHeap();
		((NodeData) gr.getNode(src)).setWeight(0);
		for (node_data n : ni) {
			h.insert(n);//add vertices to heap
		}
		while (h.getList().size() != 0) {
			node_data u = (node_data) h.extractMin();
			if (gr.getE(u.getKey()) != null) {
			Collection<edge_data> ni2 = gr.getE(u.getKey());
			for (edge_data e : ni2) {
			node_data v = (NodeData)gr.getNode(e.getDest());
			if (!((NodeData)v).isVisited()) {
			double w = ((NodeData) u).getWeight() + e.getWeight();
				if (w < ((NodeData) v).getWeight()) {
					((NodeData) v).setWeight(w);
					v.setTag(u.getKey());// to know the previous node with the lowest cost
					h.buildHeap();
						}
					}
				}
				((NodeData) u).setVisited(true);
			}
		}
	}
	
	
	
}
	
