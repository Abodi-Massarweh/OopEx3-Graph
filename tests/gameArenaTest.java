import api.DWGraph_DS;
import api.NodeData;
import api.directed_weighted_graph;
import api.node_data;
import gameClient.Arena;
import gameClient.CL_Agent;
import gameClient.CL_Pokemon;
import gameClient.util.Point3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class gameArenaTest {

	directed_weighted_graph graph;
	dw_graph_algorithms algo;
	LinkedList<node_data> check_path;
	java.util.List<node_data> shortestPath;
	private static Random _rnd = null;
	boolean t,f;
	double path_dest;
    private Arena ar= new Arena();
    private directed_weighted_graph g1;
    List<CL_Pokemon> pks_list= new ArrayList<>();


    @Test
    void test0() {
    	build_graph();
    	pks_list= new ArrayList<>();
        for(int i=0; i<5; i++){
            CL_Pokemon pok= new CL_Pokemon(new Point3D(i,i+1,0),1, i+3, g1.getEdge(i+1,i));
            build_graph();
            pks_list.add(pok);
        }
        ar.setPokemons(pks_list); // add pokemons to the arena
        assertTrue(pks_list.equals(ar.getPokemons()));
    }

    @Test
    void test1() {
    	build_graph();
    	boolean flag = true;
    	pks_list= new ArrayList<>();
        for(int i = 1; i < 5; i++){
            CL_Agent agent= new CL_Agent(g1,i);
           flag =false;
            pks_list.add(agent);
        }
        ar.setAgents(pks_list);
        assertTrue(pks_list.equals( ar.getAgents()));
    }

    /**
    * reset the graph after each test
    */
    @BeforeEach
    void init_graph() {
    graph = new DWGraph_DS();
    algo = new  DWGraph_Algo();		
    t=f=true;
    }

    @Test
    void test3() {
        graph = new DWGraph_DS();
        algo = new  DWGraph_Algo();		
    	build_graph();
        ar.setGraph(g1);
        assertTrue(g1.equals(ar.getGraph()));
    }
    
    
/////////////////////////////////// private functions ////////////////////////////////////////
	
	

/**
* add 5 vertices to the graph 
* then connect the graph with complex edges and weight 
* 
*/
void build_graph() {
pks_list= new ArrayList<>();
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