


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


class DWGraph_AlgoTest {

	directed_weighted_graph graph;
	dw_graph_algorithms algo;
	LinkedList<node_data> check_path;
	java.util.List<node_data> shortestPath;
	private static Random _rnd = null;
	boolean t,f;
	double path_dest;

	
	
	/**
	 * Empty graph
	 */
	@Test
	void test0() {
        algo.init(graph);
        assertTrue(algo.isConnected());
    }
	
    /**
     * Graph with single node
     */
	@Test
	void test1() {
		geo_location ge = new GeoLoc(0,1,2);
		node_data n0 =new NodeData(0,ge);
		graph.addNode(n0);
        algo.init(graph);
        assertTrue(algo.isConnected());
    }
	
    /**
     * graph with two vertices and no edges
     */
	@Test
	void test2() {
		geo_location ge0 = new GeoLoc(0,1,2);
		node_data n0 =new NodeData(0,ge0);
		geo_location ge1 = new GeoLoc(2,5,6);
		node_data n1 =new NodeData(1,ge1);// adds two nodes with 0,1 keys
		graph.addNode(n0);
		graph.addNode(n1);
        algo.init(graph);
        f = algo.isConnected();
        assertFalse(f);
    }
    /**
     * graph with two nodes and a single edge - connected
     */
	@Test
	void test3() {
		geo_location ge0 = new GeoLoc(0,1,2);
		node_data n0 =new NodeData(0,ge0);
		geo_location ge1 = new GeoLoc(2,5,6);
		node_data n1 =new NodeData(1,ge1);// adds two nodes with 0,1 keys
		graph.addNode(n0);
		graph.addNode(n1);// adds two nodes with 0,1 keys
		graph.connect(0, 1, 8);
        algo.init(graph);
        f = algo.isConnected();
        assertFalse(f);
        
    }
	
    /**
     * graph with 5 vertices 
     * test connectivity when remove edge or node from the graph 
     * with copy and save/load functions 
     */
	@Test
	void test4() {

		build_graph();
		// testing copy 
		graph = algo.copy(); // deep copy the graph 
		algo.init(graph);  // init the graph after deep copy
        t = algo.save("savedGraph"); 
		t &= algo.isConnected();
		graph.removeEdge(1, 2);
		t &= algo.isConnected();

		// testing if it has been saved the graph before editing and check if still connected
        t &= algo.load("savedGraph");
        t&=algo.isConnected(); 
        
        
        assertTrue(t);

    }
	
	/**
	 * check connectively to the graph with 5 nodes , it's connected also after remove edge
	 * 
	 */
	@Test
	void test4_2(){
		build_graph();
		t = algo.isConnected(); 
		graph.removeEdge(1, 2); // now node with key 6 is not connected to any node..
		t &= algo.isConnected(); 
        assertTrue(t);
	}
	
	
	/**
	 * sample test for shortest path and shortest path dest functions.
	 * the graph is empty 
	 */
	@Test
	void test5() {
		check_path = new LinkedList<node_data>();
        algo.init(graph);  
		// find path with not exist vertices in the graph
        assertTrue(algo.shortestPathDist(0, 1) == -1);
        assertTrue(algo.shortestPath(1, 0) == null);
	}
	
	void test5_2() {
		check_path = new LinkedList<node_data>();
        algo.init(graph);  
        
		// path with one node
        geo_location ge0 = new GeoLoc(0,1,2);
		node_data n0 =new NodeData(0,ge0);
		graph.addNode(n0);
		check_path.add(graph.getNode(0));
		assertTrue(algo.shortestPath(0, 0).equals(check_path));
        assertTrue(algo.shortestPathDist(0, 0) == 0);
	}
	
    /**
     * graph with 5 vertices 
     * testing shortest path and shortest path dest functions 
     */
	@Test
	void test6() {
		build_graph();
		check_path.add(graph.getNode(0));
		check_path.add(graph.getNode(3));
		check_path.add(graph.getNode(4));
		check_path.add(graph.getNode(2));
		
        shortestPath = algo.shortestPath(0, 2);
        path_dest =  6;// 1+2+3=6
        t = shortestPath.equals(check_path);
        t &= algo.shortestPathDist(0, 2) == path_dest;
        
        assertTrue(t);
    }
	/**
	 * test shortest path after update the weight to edge 
	 * with connect 2 vertices with different weight
	 */
	@Test
	void test7() {
		build_graph();
        graph.connect(4, 2, 12); // update the weight to 2
        check_path.add(graph.getNode(0));
        check_path.add(graph.getNode(1));
        check_path.add(graph.getNode(2));
       
        
        // check the path from 2 to 0   (0 <-> 4 <-> 3 <-> 2)
        shortestPath = algo.shortestPath(0, 2);
        path_dest = 13; // 5+8
        t &= shortestPath.equals(check_path);
        t &= algo.shortestPathDist(0, 2) == path_dest;
        
        assertTrue(t);
	}
	
	/**
	 * build graph with 1m nodes and 30m edges 
	 */
	@Test
	 void test8() {
	        long start = new Date().getTime();
	        int no = 1000*1000, ed = 10000*1000;
	        graph = graph_creator(no, ed, 3);
	        long end = new Date().getTime();
	        double dt = (end-start)/1000.0;
	        boolean t = dt<10;
	        assertEquals(t, true);
	 }
	
	
	
/////////////////////////////////// private functions ////////////////////////////////////////
	
	
	/**
	 * reset the graph after each test
	 */
	@BeforeEach
	void init_graph() {
		 graph = new DWGraph_DS();
		algo = new  DWGraph_Algo();		
		t=f=true;
	}
	
	/**
	 * add 5 vertices to the graph 
	 * then connect the graph with complex edges and weight 
	 * 
	 */
	void build_graph() {
		graph =new DWGraph_DS();
		shortestPath = new LinkedList<node_data>();
		check_path = new LinkedList<node_data>();
		path_dest =0;
		
		geo_location ge0 = new GeoLoc(0,1,2);
		node_data n0 =new NodeData(0,ge0);
		geo_location ge1 = new GeoLoc(2,5,6);
		node_data n1 =new NodeData(1,ge1);
		geo_location ge2 = new GeoLoc(4,1,2);
		node_data n2 =new NodeData(2,ge2);
		geo_location ge3 = new GeoLoc(2,3,8);
		node_data n3 =new NodeData(3,ge3);
		geo_location ge4 = new GeoLoc(8,1,2);
		node_data n4 =new NodeData(4,ge4);
	
		
		graph.addNode(n0);
		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		graph.addNode(n4);

	
		graph.connect(0, 1, 5);
		graph.connect(1, 2, 8);
		graph.connect(2, 4, 4);
		graph.connect(4, 3, 2);
		graph.connect(1, 0, 3);
		graph.connect(0, 3, 1);
		graph.connect(3, 0, 6);
		graph.connect(3, 4, 2);
		graph.connect(4, 2, 3);
	
        algo.init(graph);  
	}  

	
	
	
    private static DWGraph_DS graph_creator(int v_size, int e_size, int seed) {
    	DWGraph_DS g = new DWGraph_DS();
        _rnd = new Random(seed);
        for(int i=0;i<v_size;i++) {
        	node_data n = new NodeData();
            g.addNode(n);
        }
        	int[] nodes = nodes(g);
            int a = nextRnd(0,v_size);
            int b = nextRnd(0,v_size);
            double c = nextRnd(1,999);
            int i = nodes[a];
            int j = nodes[b];
            g.connect(i,j,c);
        return g;
    }
    
    
    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        int ans = (int)v;
        return ans;
    }
    private static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max-min;
        double ans = d*dx+min;
        return ans;
    }
    
    private static int[] nodes(DWGraph_DS g) {
        int size = g.nodeSize();
        Collection<node_data> V = g.getV();
        node_data[] nodes = new node_data[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }

}
