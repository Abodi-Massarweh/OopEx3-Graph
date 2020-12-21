package api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DWGraph_DSTest {
	static directed_weighted_graph graph = new DWGraph_DS();

	@Test
	/////test add node
	void testaddnode() {
		geo_location ge = new GeoLoc(0,1,2);
		node_data n0 =new NodeData(0,ge);
		graph.addNode(n0);
		node_data expected  = n0;
		node_data actual = graph.getNode(0);
		assertEquals(expected, actual);
	}
	////check connect and get edge
	@Test
	void testAddEdge() {
		geo_location ge = new GeoLoc(0,1,2);
		node_data n0 =new NodeData(0,ge);
		geo_location ge1 = new GeoLoc(0,6,2);
		node_data n1 =new NodeData(2,ge1);
		graph.addNode(n0);
		graph.addNode(n1);
		edge_data e = new EdgeData(0, 2, 20);
		graph.connect(0, 2, 20);
		edge_data actual = graph.getEdge(0, 2);
		edge_data expected = e;
		assertEquals(expected.getSrc(), actual.getSrc());
		assertEquals(expected.getDest(), actual.getDest());
		assertEquals(expected.getWeight(), actual.getWeight());
	}
	@Test
	void RemoveNodeTest() {
		graph.removeNode(2);// after we removed,we checked
		node_data actual = graph.getNode(2);
		node_data expected = null;
		
		assertEquals(expected, actual);
	}
	@Test
	void RemoveEdgeTest() {
		graph.removeEdge(0, 2);
		edge_data actual = graph.getEdge(0, 2);//now actual is null
		edge_data expected  = null;
		
		assertEquals(expected, actual);
	}
	@Test
	void edgeSizeTest() {
		int actual = graph.edgeSize();//after removed the edge,there is no edges
		int expected = 0;
		assertEquals(expected, actual);
	}
	@Test
	void nodeSizeTest() {
		int actual = graph.getV().size();
		int expected=1;
		assertEquals(actual,expected);
	}
	
	

}
