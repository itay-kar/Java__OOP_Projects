package api;

import gameClient.util.Point3D;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class DWGraph_DS implements directed_weighted_graph, Serializable {
    static int counter = 0;
    HashMap<Integer, node_data> Nodes = new HashMap<>();
    HashMap<Integer, HashMap<Integer, edge_data>> Edges = new HashMap<>();
    int Mc;
    private int nodeSize;
    private int edgeSize;

    public DWGraph_DS() {
        this.Nodes = new HashMap<>();
        this.Edges = new HashMap<>();
        counter = 0;
    }

   /* public DWGraph_DS(String g) {
        JsonObject Graphreader = new JsonObject().getAsJsonObject(g);
        Graphreader.get("Nodes").getAsJsonObject();}
   */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DWGraph_DS)) return false;
        DWGraph_DS that = (DWGraph_DS) o;
        return
                nodeSize == that.nodeSize &&
                        edgeSize == that.edgeSize &&
                        Objects.equals(Nodes, that.Nodes) &&
                        Objects.equals(Edges, that.Edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Nodes, Edges, nodeSize, edgeSize);
    }

    @Override
    public node_data getNode(int key) {
        return Nodes.getOrDefault(key, null);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        if (Edges.containsKey(src) && Edges.get(src).containsKey(dest)) {
            return Edges.get(src).get(dest);
        } else return null;
    }

    @Override
    public void addNode(node_data n) {
        Nodes.putIfAbsent(n.getKey(), n);
        Edges.put(n.getKey(), new HashMap<>());
        nodeSize++;
        Mc++;
    }


    @Override
    public void connect(int src, int dest, double w) {
        if (src == dest) {
            return;
        }

        if (Nodes.containsKey(src) && Nodes.containsKey(dest)) {
            if (Edges.get(src).containsKey(dest) && Edges.get(src).get(dest).getWeight() != w) {
                Edges.get(src).put(dest, new Edge_data(src, dest, w));
                Mc++;
            } else if (!Edges.get(src).containsKey(dest)) {
                Edges.get(src).put(dest, new Edge_data(src, dest, w));
                Mc++;
                edgeSize++;
            }
        }
    }

    @Override
    public Collection<node_data> getV() {
        return Nodes.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        return Edges.get(node_id).values();
    }

    @Override
    public node_data removeNode(int key) {
        node_data temp = Nodes.get(key);
        Nodes.remove(key);

        for (HashMap<Integer, edge_data> current : Edges.values()) {
            if (current.containsKey(key)) {
                current.remove(key);
                edgeSize--;
                Mc++;
            }
        }

        int x = Edges.get(key).size();
        Edges.remove(key);
        nodeSize--;
        edgeSize = edgeSize - x;
        Mc++;
        return temp;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        edge_data temp = null;
        if (Edges.containsKey(src) && Edges.get(src).containsKey(dest)) {
            temp = Edges.get(src).get(dest);
            Edges.get(src).remove(dest);
            edgeSize--;
            Mc++;
        }
        return temp;

    }

    @Override
    public int nodeSize() {
        return nodeSize;
    }

    @Override
    public int edgeSize() {
        return edgeSize;
    }

    @Override
    public int getMC() {
        return Mc;
    }


    public static class Node_data implements node_data, Serializable {
        Point3D location;
        int key;
        private int tag;
        private double node_weight;
        private String info;


        public  Node_data() {
            key = counter++;
            info = "White";
            tag=0;
            node_weight=0;
            location = new Point3D(0,0);
        }

        public Node_data(int key, int tag, double weight, String s){
            this.key=key;
            this.tag=tag;
            node_weight=weight;
            info=s;
            this.setLocation(location);
        }

        public Node_data(int id) {
            this.key=id;
        }

        public Node_data(int id , Point3D pos){
            this.key=id;
            this.setLocation(pos);
        }

        @Override
        public int getKey() {
            return key;
        }

        @Override
        public geo_location getLocation() {

            return location;
        }

        @Override
        public void setLocation(geo_location p) {
            {
                location = new Point3D(p.x(), p.y(), p.z());
            }

        }

        @Override
        public double getWeight() {
            return node_weight;
        }

        @Override
        public void setWeight(double w) {
            this.node_weight = w;

        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        @Override
        public int getTag() {
            return tag;
        }

        @Override
        public void setTag(int t) {
            this.tag = t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node_data)) return false;
            Node_data node_data = (Node_data) o;
            return key == node_data.key &&
                    tag == node_data.tag &&
                    Double.compare(node_data.node_weight, node_weight) == 0 &&
                    Objects.equals(location, node_data.location) &&
                    Objects.equals(info, node_data.info);
        }

        @Override
        public int hashCode() {
            return Objects.hash(location, key, tag, node_weight, info);
        }
    }

    public static class Edge_data implements edge_data, Serializable {
        private int src;
        private int dest;
        private double weight;
        private String info = "White";
        private int tag = 0;

        public Edge_data(){}

        public Edge_data(int x, int y, double w) {
            this.src = x;
            this.dest = y;
            this.weight = w;
        }

        public Edge_data(int src, int dest, double weight, String info, int tag) {
            this.src=src;
            this.dest=dest;
            this.weight=weight;
            this.info=info;
            this.tag=tag;
        }

        public Edge_data(edge_data edge) {
            this.src=edge.getSrc();
            this.dest=edge.getDest();
            this.weight=edge.getWeight();
        }


        @Override
        public int getSrc() {
            return this.src;
        }

        @Override
        public int getDest() {
            return this.dest;
        }

        @Override
        public double getWeight() {
            return this.weight;
        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        @Override
        public int getTag() {
            return tag;
        }

        @Override
        public void setTag(int t) {
            this.tag = t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Edge_data)) return false;
            Edge_data edge_data = (Edge_data) o;
            return src == edge_data.src &&
                    dest == edge_data.dest &&
                    Double.compare(edge_data.weight, weight) == 0 &&
                    tag == edge_data.tag &&
                    Objects.equals(info, edge_data.info);
        }

        @Override
        public int hashCode() {
            return Objects.hash(src, dest, weight, info, tag);
        }

        public edge_data convert(edge_data e) {
            edge_data temp = new Edge_data(0,0,0);


            return temp;
        }
    }
}
