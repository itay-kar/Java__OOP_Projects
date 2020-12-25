package api;

import com.google.gson.*;
import gameClient.util.Point3D;

import java.lang.reflect.Type;
import java.util.Map;

public class GraphDecoder implements JsonDeserializer<directed_weighted_graph> {

    @Override
    public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        directed_weighted_graph graph = new DWGraph_DS();
        JsonObject str_graph = jsonElement.getAsJsonObject();
        JsonArray Verticals = str_graph.get("Nodes").getAsJsonArray();
        JsonArray Edges = str_graph.get("Edges").getAsJsonArray();

        for (int i=0 ; i<Verticals.size() ; i++){
            JsonElement nodeValue = Verticals.get(i).getAsJsonObject();
            int key = nodeValue.getAsJsonObject().get("id").getAsInt();
            String x = nodeValue.getAsJsonObject().get("pos").getAsString();
            Point3D position = new Point3D(x);
            node_data n = new DWGraph_DS.Node_data(key, position);
            graph.addNode(n);
        }

        for (int i = 0 ; i<Edges.size() ; i++) {
            JsonElement edgeValue = Edges.get(i).getAsJsonObject();

                int src = edgeValue.getAsJsonObject().get("src").getAsInt();
                int dest = edgeValue.getAsJsonObject().get("dest").getAsInt();
                double weight = edgeValue.getAsJsonObject().get("w").getAsDouble();
                graph.connect(src, dest, weight);
            }

        return graph;
    }}

