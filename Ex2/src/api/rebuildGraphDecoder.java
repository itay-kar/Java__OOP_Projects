package api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class rebuildGraphDecoder implements JsonDeserializer<directed_weighted_graph> {

    @Override
    public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        directed_weighted_graph temp = new DWGraph_DS();
        JsonObject jsonObj = jsonElement.getAsJsonObject();
        JsonObject Verticals = jsonObj.get("V").getAsJsonObject();

        for (
                Map.Entry<String, JsonElement> set : Verticals.entrySet()) {
            String hashKey = set.getKey();
            JsonElement nodeValue = set.getValue();
            int key = nodeValue.getAsJsonObject().get("key").getAsInt();
            int tag = nodeValue.getAsJsonObject().get("tag").getAsInt();
            String info = nodeValue.getAsJsonObject().get("info").getAsString();
            double weight = nodeValue.getAsJsonObject().get("node_weight").getAsDouble();
            JsonElement location_info = nodeValue.getAsJsonObject().get("location");

            node_data n = new DWGraph_DS.Node_data(key, tag, weight, info);
            temp.addNode(n);
        }

        JsonObject Edges = jsonObj.get("Nodes").getAsJsonObject();

        for (Map.Entry<String, JsonElement> set : Edges.entrySet()) {
            String edgeKey = set.getKey();
            JsonObject current = Edges.get(set.getKey()).getAsJsonObject();
            for (Map.Entry<String, JsonElement> set2 : current.entrySet()) {
                JsonElement edgeValue = set2.getValue();
                int src = edgeValue.getAsJsonObject().get("src").getAsInt();
                int dest = edgeValue.getAsJsonObject().get("dest").getAsInt();
                double weight = edgeValue.getAsJsonObject().get("weight").getAsDouble();
                String info = edgeValue.getAsJsonObject().get("info").getAsString();
                int tag = edgeValue.getAsJsonObject().get("tag").getAsInt();
                edge_data e = new DWGraph_DS.Edge_data(src, dest, weight, info, tag);
                temp.connect(src, dest, weight);
            }
        }
        return temp;
    }
}
