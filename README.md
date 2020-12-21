### OopEx2-Graph
***Part 1***
- [ ] ***1.File list:*** In this project we build weighted directed graph
node_data :interface that represent the set of operations applicable on a node (vertex) in weighted undirectional graph.
NodeData : class that implements "node_info" interface.
Directed_weighted_graph: interface that represent an directional weighted graph.
WGraph_DS :class that implements "weighted_graph" interface.
dw_graph_algorithms: interface represents the "regular" Graph Theory algorithms(shrotest path,check if the graph is connected).
DWGraph_Algo: class that implements "weighted_graph_algorithms" interface.
- [ ] ***2.Algorithms:*** Dijkstra's algorithm (or Dijkstra's Shortest Path First algorithm, SPF algorithm) is an algorithm for finding the shortest paths between nodes in a graph.
Analysis for dijkstra's: a.Mark all nodes unvisited. Create a set of all the unvisited nodes called the unvisited set.
b.Assign to every node a tentative distance value: set it to zero for our initial node and to infinity for all other nodes. Set the initial node as current.
c.For the current node, consider all of its unvisited neighbours and calculate their tentative distances through the current node. Compare the newly calculated tentative distance to the current assigned value and assign the smaller one
d.When we are done considering all of the unvisited neighbours of the current node, mark the current node as visited and remove it from the unvisited set. A visited node will never be checked again.
e.If the destination node has been marked visited (when planning a route between two specific nodes) or if the smallest tentative distance among the nodes in the unvisited set is infinity (when planning a complete traversal; occurs when there is no connection between the initial node and remaining unvisited nodes), then stop. The algorithm has finished.
f.Otherwise, select the unvisited node that is marked with the smallest tentative distance, set it as the new "current node", and go back to step e.
- [ ]  ***3.Data structures:*** in NodeData class : HashMap<node_data,edge_data> EdgesOut represents the edges that going out from the node , the key of HashMap is the node id of the node_data.
HashMap<node_data,edge_data> EdgesIn represents the edges that entering to the node , the key of HashMap is the node id of the node_data.
in DWGraph_DS class : HashMap<Integer,node_data> represents the vertices in the graph, the key of HashMap is the node id of the node_data.
- [ ] ***4.Algorithims:*** isConnected: we use kosaraju’s that do transpose to the graph and we send the graph and reversed graph to recursive help function to visit all vertices in graph and check if we visit all vertices if one of vertices is not visited then the graph is not connected otherwise the graph is connected.
shortestpath: according to dijkstra, the explain is(a-f).
shortestpathdist: doing dijkstra's between the src and the dest and return the weight of the vertex dest.
![graaaaaph](https://user-images.githubusercontent.com/58177069/102723823-9220e100-4313-11eb-9819-c8b1a3056963.png)
### ***Part 2***
Welcome to The SpongeBob game!
Description:
the SpongeBob is a game, writing in Java, that allows Spongebob to eat jellyfish(pokemon) as much as can in minimum moves, before the time will end!
This program has 23 deferent scenarios and contain graph, number of jellyfish on the graph, number of jellyfish and the time to play.
For the beginning – you have to enter your id and after that you have to choose arena by clicking on button that entered you to game.


<img width="247" alt="1 (1)" src="https://user-images.githubusercontent.com/58177069/102723857-dd3af400-4313-11eb-8fac-b58f1c7b44ae.PNG">

The algorithm to make Spongebob to catch jellyfish(pakmans) as much as can in minimum moves:
We send the approach agent to the pokemon by using shortestpath function
At the same time we will not allow several agents to going to the same pokemon in order to get efficiency and score as many points in the game as possible.

<img width="960" alt="2" src="https://user-images.githubusercontent.com/58177069/102723883-1f643580-4314-11eb-974f-804ec90afc6e.PNG">



