package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

public class CL_Agent {
    public static final double EPS = 0.0001;
    private int _id;
    private geo_location _pos;
    private double _speed;
    private edge_data _curr_edge;
    private node_data _curr_node;
    private directed_weighted_graph _gg;
    private CL_Pokemon current_pokemon;
    private long _sg_dt;
    private double _value;


    public CL_Agent(directed_weighted_graph g, int start_node) {
        _gg = g;
        setCoins(0);
        this._curr_node = _gg.getNode(start_node);
        _pos = _curr_node.getLocation();
        _id = -1;
        _sg_dt=0;
        setSpeed(0);
    }

        public void update(String json) {
        JSONObject line;
        try {
            line = new JSONObject(json);
            JSONObject agent_read = line.getJSONObject("Agent");
            int id = agent_read.getInt("id");
            if (id == this.getID() || this.getID() == -1) {
                if (this.getID() == -1) {
                    _id = id;
                }
                double speed = agent_read.getDouble("speed");
                this.setSpeed(speed);
                String p = agent_read.getString("pos");
                Point3D posA = new Point3D(p);
                this._pos = posA;
				int src = agent_read.getInt("src");
				this.setCurrNode(src);
                int dest = agent_read.getInt("dest");
                this.setNextNode(dest);
                double value = agent_read.getDouble("value");
                this.setCoins(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Override
    public int getSrcNode() {
        return this._curr_node.getKey();
    }

    private void setCoins(double v) {
        _value = v;
    }

    public boolean setNextNode(int nextNode) {
    	long x = _sg_dt;
    	x=x+1;
        boolean ans = false;
        int src = this._curr_node.getKey();
        this._curr_edge = _gg.getEdge(src, nextNode);
        if (_curr_edge != null) {
            ans = true;
        }
        return ans;
    }

    public void setCurrNode(int src) {
        this._curr_node = _gg.getNode(src);
    }

    public boolean isMoving() {
        return this._curr_edge != null;
    }

    @Override
    public String toString() {
        int d = this.getNextNode();
        String ans = "{\"Agent\":{"
                + "\"id\":" + this._id + ","
                + "\"value\":" + this._value + ","
                + "\"src\":" + this._curr_node.getKey() + ","
                + "\"dest\":" + d + ","
                + "\"speed\":" + this.getSpeed() + ","
                + "\"pos\":\"" + _pos.toString() + "\""
                + "}"
                + "}";
        return ans;
    }

    /*public String toString1() {
        String ans=""+this.getID()+","+_pos+", "+isMoving()+","+this.getValue();
        return ans;
    }*/
    public int getID() {
        return this._id;
    }

    public geo_location getLocation() {
        return _pos;
    }

    public double getValue() {
        return this._value;
    }

    public int getNextNode() {
        int ans;
        if (this._curr_edge == null) {
            ans = -1;
        } else {
            ans = this._curr_edge.getDest();
        }
        return ans;
    }

    public double getSpeed() {
        return this._speed;
    }

    public void setSpeed(double v) {
        this._speed = v;
    }

    public CL_Pokemon get_curr_pok() {
        return current_pokemon;
    }

    public void set_curr_pok(CL_Pokemon curr_fruit) {
        this.current_pokemon = curr_fruit;
    }

    public void set_SDT(long ddtt) {
        long ddt = ddtt;
        if (this._curr_edge != null) {
            double w = get_curr_edge().getWeight();
            geo_location dest = _gg.getNode(get_curr_edge().getDest()).getLocation();
            geo_location src = _gg.getNode(get_curr_edge().getSrc()).getLocation();
            double de = src.distance(dest);
            double dist = _pos.distance(dest);
            if (this.get_curr_pok().get_edge() == this.get_curr_edge()) {
                dist = current_pokemon.getLocation().distance(this._pos);
            }
            double norm = dist / de;
            double dt = w * norm / this.getSpeed();
            ddt = (long) (1000.0 * dt);
        }
        this.set_sg_dt(ddt);
    }

    public edge_data get_curr_edge() {
        return this._curr_edge;
    }

    public void set_sg_dt(long _sg_dt) {
        this._sg_dt = _sg_dt;
    }
}
