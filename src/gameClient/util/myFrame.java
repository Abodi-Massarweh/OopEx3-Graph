package gameClient.util;

import api.*;
import gameClient.Arena;
import gameClient.CL_Agent;
import gameClient.CL_Pokemon;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class myFrame extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Arena _arena;
    private Range2Range _w2f;
    private int game_number;
    private float time;
    
    
    public myFrame(String str, int level) {
        super(str);
        this.setSize(new Dimension( 350, 400));
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game_number = level;
        this.setVisible(true);
    }

    public myFrame(String str, int i, int j, int level_number) {
        super(str);
        this.setSize(new Dimension( i, j));
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game_number = level_number;
        this.setVisible(true);
	}

	public void update(Arena ar) {
        this._arena = ar;
        updateFrame();
        this.revalidate();
    }

    private void updateFrame() {
        Range rx = new Range(20, this.getWidth() - 50);
        Range ry = new Range(this.getHeight() - 150, 150);
        Range2D frame = new Range2D(rx, ry);
        if (this._arena != null) {
            directed_weighted_graph g = _arena.getGraph();
            _w2f = Arena.w2f(g, frame);
            this.revalidate();
            this.setVisible(true);
        }
    }
    public void paint(Graphics g) {
    	
        this.add(new gamePanel(this._w2f));
        updateFrame();
        this.revalidate();
        
    }

    public void initLogin() {
    	gameClient.Ex2.startFrame panel = new gameClient.Ex2.startFrame();
        this.add(panel);
        this.setVisible(true);
        panel.setVisible(true);
    }

    public void setTimeToEnd(long timeTo) {
        time = (float) timeTo / 100;
    }

    
    
    private class gamePanel extends JPanel {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Range2Range _w2f;

        public gamePanel(Range2Range _w2f) {
            if (_w2f != null)
                this._w2f = _w2f;
            this.revalidate();
        }

        public void paintComponent(Graphics g) {
            Graphics2D gg = (Graphics2D) g;
            super.paintComponent(g);
            
            // add background image
            BufferedImage img = null;
            ImageObserver observer = null;
			try { img = ImageIO.read(new File("data/background.png"));} catch (IOException e) {}
			g.drawImage(img, 0, 0, 1300, 800, observer);
			
			
            if(_arena!=null && _w2f != null) {
                draw_graph(gg);
                drawPokemons(gg);
                drawAgents(gg);
                draw_scoreText(gg);
            }
            
            this.revalidate();
            this.setVisible(true);
        }

        public void draw_scoreText(Graphics g) {
            int grade = 0;
                List<CL_Agent> agents_list = _arena.getAgents();
                for(int agent_id=0;agents_list != null && agent_id < agents_list.size();agent_id++) {
                	g.setColor(Color.black);
                    g.drawString("Agent id: " + agents_list.get(agent_id).getID() + "  -", 10, 20 + agent_id * 21);
                    g.drawString("score:  " + agents_list.get(agent_id).getValue(), 115, 20 + agent_id * 21);
                    grade += agents_list.get(agent_id).getValue();
                }

                
            // score text   
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.magenta.darker().darker().darker());
            g2.drawString("Game number: " + game_number, 10, 615);
            g2.drawString("Total score: " + grade, 10, 590);
            g2.setColor(Color.black);
            
            // time text
            Graphics2D g3 = (Graphics2D) g;
            g3.setColor(Color.magenta.darker().darker().darker());
            g3.drawString("Remaining time:  " +time, 10, 650);
            g3.setColor(Color.blue.darker().darker().darker());
            
        }
        
        // draw the graph (nodes + edges) using draw_node and draw_edge functions
        private void draw_graph(Graphics g) {
        		// Finally!! draw the edges
                for(node_data n : _arena.getGraph().getV()) {
                    g.setColor(Color.black);
                    if(this._w2f != null)
                    draw_node(n, g);
                    // Finally!! draw the edges
                    for(edge_data e : _arena.getGraph().getE(n.getKey())) {
                        g.setColor(Color.black);
                        if (this._w2f != null) 
                        draw_edge(e, g);
                    }
                }
        }
        
        // draw jellyfish icons for each pokemon  
        private void drawPokemons(Graphics g) {
            BufferedImage img = null;
                List<CL_Pokemon> Pokemons_list = _arena.getPokemons();
                if (Pokemons_list != null) {
                	 // draw each pokemon in the list 
                    for(CL_Pokemon p : Pokemons_list) {
                        Point3D pokemon_location = p.getLocation(); // get the location of each pokemon..
                        if (pokemon_location != null) {
                        if (p.getType() < 0) {
                        	// add pokemon icon  
                            try { img = ImageIO.read(new File("data/jellyfish.png")); } catch (IOException e) {}
                        }
                            if (this._w2f != null) 
                                g.drawImage(img, (int) _w2f.world2frame(pokemon_location).x() - 21, (int) _w2f.world2frame(pokemon_location).y() - 21, this); 
                        }
                    }
                }
        }
        
     // draw spongebob icons for each agent 
        private void drawAgents(Graphics g) {
                List<CL_Agent> agents_list = _arena.getAgents();
                for(int i=0; i < agents_list.size() && agents_list.get(i).getLocation() != null;i++) {
                    geo_location agent_location = agents_list.get(i).getLocation();
                    
                    geo_location f_location = this._w2f.world2frame(agent_location);
                    
                    // adding spongebob icon to each pokemon location using geo location
                    BufferedImage img = null;
                    try { img = ImageIO.read(new File("data/spongebob.png"));} catch (IOException e) {}
                    g.drawImage(img, (int) f_location.x() - 15, (int) f_location.y() - 15, this);
                }
            this.revalidate();
        }
        
        // draw edge line using src and dest for given edge..
        private void draw_edge(edge_data e, Graphics g) {
            directed_weighted_graph gg = _arena.getGraph();
            geo_location s = gg.getNode(e.getSrc()).getLocation();
            geo_location d = gg.getNode(e.getDest()).getLocation();
            geo_location s0 = this._w2f.world2frame(s);
            geo_location d0 = this._w2f.world2frame(d);
            ((Graphics2D) g).setStroke(new BasicStroke(3));
            g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
            g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
            this.paintComponents(g);
            this.revalidate();
        }
        
        // draw fill oval using given node position
        private void draw_node(node_data n, Graphics g) {
            if ( n.getLocation() != null) {
                geo_location fp = this._w2f.world2frame( n.getLocation());
                g.fillOval((int) fp.x() - 5, (int) fp.y() - 5, 7, 7);
                g.setFont(new Font("David", Font.PLAIN, 15));
                g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 2 * 5);
            }
        }

    }
}
