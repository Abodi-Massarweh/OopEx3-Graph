  
package gameClient;

import api.*;
import gameClient.util.myFrame;
import Server.Game_Server_Ex2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.*;
import java.util.List;




public class Ex2 extends JPanel implements Runnable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static myFrame _win;
    private static Arena _ar;
    private static int level_number = -1;
    private static int player_id = -1;
    private static  LinkedList<Integer> pair = new LinkedList<>();
    private static HashMap<Integer, Integer> edges = new HashMap<>();
    private static directed_weighted_graph graph;
    private static dw_graph_algorithms algo = new DWGraph_Algo();
    private static Thread client;
    private static long dt;
    private static long game_time;
    
    public static void main(String[] args) {
    	client = new Thread(new Ex2());

        //Value of terminal
        if (args.length == 2) {
            player_id = Integer.valueOf(args[0]);
            level_number = Integer.valueOf(args[1]);
            client.start();
        }
        else {
            _win = new myFrame("Start game", level_number);
            dt=100;
        	_win.initLogin();
        }

    }

    @Override
    public void run() {
        game_service game = Game_Server_Ex2.getServer(level_number);
        game.login(player_id);
        graph = load_graph(game.getGraph());
        init(game);
        
        game.startGame();
       game_time = (long) (game.timeToEnd() *  0.3333);
        int ind = 1;
        dt = 100;
        while (game.isRunning()) {
        		moveAgents(game);
            try {
                if (ind % 1 == 0) {
                    _win.repaint();
                    _win.setTimeToEnd(game.timeToEnd() / 10);
                }
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        String res = game.toString();
        System.out.println(res);
        System.exit(0);
    }

    /**
     * init the game 
     * @param game - game service
     */
    private void init(game_service game) {
        _win = new myFrame("Spongebob Runner", 1000, 900, level_number);
        String pks = game.getPokemons();
        graph = load_graph(game.getGraph());

        _ar = new Arena();
        _ar.setGraph(graph);
        _ar.setPokemons(Arena.json2Pokemons(pks));
        _win.update(_ar);
 
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject gameData = line.getJSONObject("GameServer");
            int agents_number = gameData.getInt("agents");
            System.out.println(info);
            ArrayList<CL_Pokemon> pks_list = Arena.json2Pokemons(game.getPokemons());
            for (int i = 0; i < pks_list.size(); i++) {
                Arena.updateEdge(pks_list.get(i), graph);
            }
            
            selectionSort(pks_list);
            
            // add agents
            for (int i = 0; i < agents_number; i++) {
                CL_Pokemon c = pks_list.get(i);
                game.addAgent(c.get_edge().getSrc());
                pair.add(-1);
            }
            
            
            
//            System.out.println(pks_list.get(0).get_edge().getSrc());
//            // add all agents to the game
//            dw_graph_algorithms algo = new DWGraph_Algo();
//            algo.init(graph);
            
//            if(!spawn_agents(agents_number, pks_list, algo ,game)) {
                // get w sum list of each pokemon with other pokemons
                HashMap<Double, Integer> temp_list = new HashMap<Double, Integer>();
//                HashMap<Double, Integer> min_list = pathDist_list(agents_number, pks_list, algo);
              

//            }
 
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * sort the pokemons by order
     */
    public static void selectionSort(List<CL_Pokemon> cl){  
        for (int i = 0; i < cl.size() - 1; i++)  {  
            int index = i;  
            for (int j = i + 1; j < cl.size() ; j++)	{  
                if (cl.get(j).getValue() < cl.get(index + 1).getValue()){  
                    index = j; //searching for lowest index  
                }  
            }  
            CL_Pokemon smallerNumber = cl.get(index);    
            cl.set(index, cl.get(i));
            cl.set(i, smallerNumber);
        }  
    }  




    private static void moveAgents(game_service game) {
        String lg = game.move();

        List<CL_Agent> agents_list = Arena.getAgents(lg, graph);
        _ar.setAgents(agents_list);
        String pk = game.getPokemons();
        _ar.setPokemons(Arena.json2Pokemons(pk));
        for (int i = 0; i < agents_list.size(); i++) {
            CL_Agent ag = agents_list.get(i);
            int agent_destKey = ag.getNextNode();
            int agent_srcKey = ag.getSrcNode();
            if (agent_destKey == -1) {
                pair.set(ag.getID(), -1);
                
                List<node_data> shortestPath_list = shortest_path(agent_srcKey, ag.getID(), ag.getSpeed(), game.timeToEnd());
                Iterator<node_data> ite_path = shortestPath_list.iterator();
                ite_path.next();
                node_data temp = ite_path.next();
                game.chooseNextEdge(ag.getID(), temp.getKey());
                System.out.println("Agent: " +ag.getID() + ", speed: " + ag.getSpeed() +", val: " + ag.getValue() + "   turned to node: " + temp.getKey());
            }

        }
    }

    
    
    
    
    
//    /**
//	 * @param targets- the list to visit
//	 * @return the list of the node we pass
//	 */
//	private static List<node_data> TSP(List<Integer> targets) {
//		algo.init(graph);
//		if(targets.size()==1) {
//			List<node_data> ans = new LinkedList<>();
//			ans.add(graph.getNode(targets.get(0)));
//			return ans;
//		}
//		directed_weighted_graph con = new DWGraph_DS();
//		if (algo.isConnected() == false) {
//			directed_weighted_graph sub = new DWGraph_DS();
//			sub = algo.copy();
//			for (node_data n : algo.getGraph().getV()) {// remove all the nodes that not in the targets list
//				if (!targets.contains(n.getKey())) {
//					sub.removeNode(n.getKey());
//				}
//			}
//			dw_graph_algorithms subA = new DWGraph_Algo();
//			subA.init(sub);
//			if (subA.isConnected()) {
//				con = sub;
//			} else {
//				return null;
//			}
//		} else {
//			con = algo.copy();
//		}
//		List<node_data> ans = new LinkedList<>();
//		int index = 1;
//		for (int i = 0; i < targets.size(); i++) {
//			while (index < targets.size() && con.getNode(targets.get(index)).getInfo() == "1") {
//				index++;
//			}
//			if (index == targets.size()) {
//				break;
//			}
//			List<node_data> temp = algo.shortestPath(targets.get(i), targets.get(index));
//			for (node_data n : temp) {
//				if (n.getKey() == targets.get(i) && i != 0) {
//					continue;
//				}
//				ans.add(n);
//				if (targets.contains(n.getKey())) {
//					con.getNode(n.getKey()).setInfo("1");
//				}
//			}
//
//		}
//		
//		return ans;
//	}
    

    private static List<node_data> shortest_path( int mySrc, int id, double sp, long timeToEnd) {
        algo.init(graph);

        //updates pokemon edges
        double min_distance = Double.MAX_VALUE;
        boolean going = true;
        boolean not_going = false;
        int nodeDest = 0;
        int pok_source;
        int minDindex = 0;

        // update the arena
        for (int i = 0; i < _ar.getPokemons().size(); i++) {
            Arena.updateEdge(_ar.getPokemons().get(i), graph);
            pok_source = _ar.getPokemons().get(i).get_edge().getSrc();
           
            
                //if another agent goes to this pokemon
                if(pair.contains(pok_source)) going=false;


            if (going) {
            	// find 
                if (algo.shortestPathDist(mySrc, pok_source) < min_distance) {
                    min_distance = algo.shortestPathDist(mySrc, pok_source);
                    minDindex = i;
                    nodeDest = pok_source;
                }
            }
            going = true;

        }

        List<node_data> path = algo.shortestPath(mySrc, nodeDest);
        pair.set(id, nodeDest);
        path.add(graph.getNode(_ar.getPokemons().get(minDindex).get_edge().getDest()));
		
        return path;
    }

    


	private HashMap<Double, Integer> pathDist_list(int agents_number, ArrayList<CL_Pokemon> pks_list,
			dw_graph_algorithms algo) {
    	HashMap<Double, Integer> min_list = new HashMap<Double, Integer>();
        Integer pok_src,pok_dest;

        for (int i = 0; i < agents_number; i++) {
            Double sum=0.0;
        	for(CL_Pokemon pokemon1 : pks_list) {
        		sum=0.0;
                if (pokemon1.getType() < 0) {
                    pok_src = pokemon1.get_edge().getSrc();
                }
                else pok_src = pokemon1.get_edge().getDest();
                for(CL_Pokemon pokemon2 : pks_list) {
                    if (pokemon2.getType() < 0) {
                        pok_dest = pokemon2.get_edge().getSrc();
                    }
                    else pok_dest = pokemon2.get_edge().getDest();
                	sum += algo.shortestPathDist(pok_src, pok_dest);	
                }
                min_list.put(sum, pok_src);                  
        	}
        }
        return min_list;
	}
	
	// load the graph from Json to directed weighted graph 
    private directed_weighted_graph load_graph(String s) {
        GsonBuilder gson_builder = new GsonBuilder();
        gson_builder.registerTypeAdapter(DWGraph_DS.class, new Deserializer());
        boolean flag=false;
        Gson gson = gson_builder.create();
        try {
            directed_weighted_graph g = gson.fromJson(s, DWGraph_DS.class);
            fill_edges(g);
            flag=false;
            return g;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void fill_edges(directed_weighted_graph g) {
        for (int i = 0; i < g.getV().size(); i++) {
            edges.put(i, -1);
        }
		
	}

	public static class startFrame extends JPanel {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public startFrame() {
            super();
            this.setLayout(null);
            setBackground(Color.red.darker().darker());
            
            JLabel t = new JLabel("Spongebob Runner");
            t.setFont(new Font("Dialog", Font.BOLD, 30));
            t.setForeground(Color.YELLOW);
            t.setBounds(30, 20, 350, 40);
            add(t);
            
            JLabel id_text = new JLabel("player Id");
            id_text.setBounds(50, 80, 60, 25);
            Border b = BorderFactory.createLineBorder(Color.white, 2);
            id_text.setForeground(Color.black.darker());
            id_text.setBackground(Color.gray.brighter());
            id_text.setFont(new Font("Dialog", Font.BOLD, 13));
            id_text.setBorder(b);
            id_text.setOpaque(true);
            add(id_text);
            
            JTextField id_editText = new JTextField();
            id_editText.setBounds(120,80, 165, 25);
            id_editText.setFont(new Font("Serif", Font.BOLD, 13));
            id_editText.setForeground(Color.RED.darker());
            if (player_id >= 0) id_editText.setText(player_id + "");
            // add placeholder later..
            this.add(id_editText);
            add_levels(this ,0, client,  id_editText, "level 0", 30, 140, 65, 25);
            add_levels(this ,1, client , id_editText, "level 1", 100, 140, 65, 25);
            add_levels(this ,2, client , id_editText, "level 2", 170, 140, 65, 25);
            add_levels(this ,3, client , id_editText, "level 3", 240, 140, 65, 25);
            
            add_levels(this ,4, client,  id_editText, "level 4", 30, 170, 65, 25);
            add_levels(this ,5, client , id_editText, "level 5", 100, 170, 65, 25);
            add_levels(this ,6, client , id_editText, "level 6", 170, 170, 65, 25);
            add_levels(this ,7, client , id_editText, "level 7", 240, 170, 65, 25);
            
            add_levels(this ,8, client,   id_editText, "level 8", 30, 200, 65, 25);
            add_levels(this ,9, client , id_editText, "level 9", 100, 200, 65, 25);
            add_levels(this ,10, client , id_editText, "level 10", 170, 200, 65, 25);
            add_levels(this ,11, client , id_editText, "level 11", 240, 200, 65, 25);
            
            add_levels(this ,12, client,  id_editText, "level 12", 30, 230, 65, 25);
            add_levels(this ,13, client , id_editText, "level 13", 100, 230, 65, 25);
            add_levels(this ,14, client , id_editText, "level 14", 170, 230, 65, 25);
            add_levels(this ,15, client , id_editText, "level 15", 240, 230, 65, 25);
            
            add_levels(this ,16, client,  id_editText, "level 16", 30, 260, 65, 25);
            add_levels(this ,17, client , id_editText, "level 17", 100, 260, 65, 25);
            add_levels(this ,18, client , id_editText, "level 18", 170, 260, 65, 25);
            add_levels(this ,19, client , id_editText, "level 19", 240, 260, 65, 25);
            
            add_levels(this ,20, client,  id_editText, "level 20", 30, 290, 65, 25);
            add_levels(this ,21, client , id_editText, "level 21", 100, 290, 65, 25);
            add_levels(this ,22, client , id_editText, "level 22", 170, 290, 65, 25);
            add_levels(this ,23, client,  id_editText, "level 23", 240, 290, 65, 25);

		}
		
		
        /**
         * add level buttons 
         */
    	private static void add_levels(startFrame login, int game_number, Thread client, JTextField userText, String level, int i, int j,
    			int k, int l) {
            Border button_border = BorderFactory.createLineBorder(Color.white, 2);
    		JButton start_level_button = new JButton(level);
            start_level_button.setBounds(i, j, k, l);
            start_level_button.setForeground(Color.yellow);
            start_level_button.setBackground(Color.red.darker().darker().darker());
            start_level_button.setFont(new Font("Dialog", Font.BOLD, 15));
            start_level_button.setBorder(button_border);
            
            login.add(start_level_button);
            start_level_button.addActionListener(e ->{
            	 level_number = Integer.valueOf(game_number);
            	 String user_text = userText.getText().toString().trim();
            	 if(user_text.equals("")) {
            		 System.out.println("id -> empty ");
            		 return;
            	 }
            	 player_id = Integer.parseInt(user_text);
            	 client.start();
            });
    	}
    }
}

