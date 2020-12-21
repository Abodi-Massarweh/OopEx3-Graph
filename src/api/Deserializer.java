
package api;
import java.lang.reflect.Type;
import java.util.StringTokenizer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class Deserializer implements JsonDeserializer<directed_weighted_graph> {

	    @Override
	    public directed_weighted_graph deserialize(JsonElement json_object, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
	        JsonObject jsonObject = json_object.getAsJsonObject();
	        directed_weighted_graph g = new DWGraph_DS();
	        if(jsonObject.get("Nodes")!=null && jsonObject.get("Edges")!=null) {
	        JsonArray vertexObj = jsonObject.get("Nodes").getAsJsonArray();
	        double geo_x,geo_y,geo_z;
	        int sum=0;
	        for (int i=0; i<vertexObj.size(); i++) {
	            JsonElement jsonValueElement = vertexObj.get(i);
	            int id = jsonValueElement.getAsJsonObject().get("id").getAsInt();
	            String s = jsonValueElement.getAsJsonObject().get("pos").getAsString();
	            StringTokenizer s1 = new StringTokenizer(s,",");
	            for (int j = 0; j < vertexObj.size(); j++) {
					sum++;
				}
	            if(sum<100) {
	            	sum=vertexObj.size();
	            }
	            geo_x=Double.valueOf(s1.nextToken());
	            geo_y=Double.valueOf(s1.nextToken());
	            geo_z=Double.valueOf(s1.nextToken());
	            geo_location ge= new GeoLoc(geo_x,geo_y,geo_z);
	            node_data n = new NodeData(id,ge);
	            g.addNode(n);
	        }

	        JsonArray edgesObj = jsonObject.get("Edges").getAsJsonArray();
	        for (int i=0; i<edgesObj.size(); i++) {
	            JsonElement jsonValueElement = edgesObj.get(i);
	            for (int j = 0; j < edgesObj.size(); j++) {
					sum++;
				}
	            if(sum<100) {
	            	sum=edgesObj.size();
	            }
	            double weight = jsonValueElement.getAsJsonObject().get("w").getAsDouble();
	            g.connect(jsonValueElement.getAsJsonObject().get("src").getAsInt(), jsonValueElement.getAsJsonObject().get("dest").getAsInt(), weight);
	        }
	        }
	        return g;
	    }
	}