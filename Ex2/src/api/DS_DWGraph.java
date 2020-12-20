package api;

import gameClient.util.Point3D;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class DS_DWGraph implements directed_weighted_graph, Serializable {
    static int counter = 0;
    HashMap<Integer, node_data> V = new HashMap<>();
    HashMap<Integer, HashMap<Integer, edge_data>> E = new HashMap<>();
    int Mc;
    private int nodeSize;
    private int edgeSize;

    public DS_DWGraph() {
        this.V = new HashMap<>();
        this.E = new HashMap<>();
        counter = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DS_DWGraph)) return false;
        DS_DWGraph that = (DS_DWGraph) o;
        return
                nodeSize == that.nodeSize &&
                        edgeSize == that.edgeSize &&
                        Objects.equals(V, that.V) &&
                        Objects.equals(E, that.E);
    }

    @Override
    public int hashCode() {
        return Objects.hash(V, E, nodeSize, edgeSize);
    }

    @Override
    public node_data getNode(int key) {
        return V.getOrDefault(key, null);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        if (E.containsKey(src) && E.get(src).containsKey(dest)) {
            return E.get(src).get(dest);
        } else return null;
    }

    @Override
    public void addNode(node_data n) {
        V.putIfAbsent(n.getKey(), n);
        E.put(n.getKey(), new HashMap<>());
        nodeSize++;
        Mc++;
    }


    @Override
    public void connect(int src, int dest, double w) {
        if (src == dest) {
            return;
        }

        if (V.containsKey(src) && V.containsKey(dest)) {
            if (E.get(src).containsKey(dest) && E.get(src).get(dest).getWeight() != w) {
                E.get(src).put(dest, new Edge_data(src, dest, w));
                Mc++;
            } else if (!E.get(src).containsKey(dest)) {
                E.get(src).put(dest, new Edge_data(src, dest, w));
                Mc++;
                edgeSize++;
            }
        }
    }

    @Override
    public Collection<node_data> getV() {
        return V.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        return E.get(node_id).values();
    }

    @Override
    public node_data removeNode(int key) {
        node_data temp = V.get(key);
        V.remove(key);

        for (HashMap<Integer, edge_data> current : E.values()) {
            if (current.containsKey(key)) {
                current.remove(key);
                edgeSize--;
                Mc++;
            }
        }

        int x = E.get(key).size();
        E.remove(key);
        nodeSize--;
        edgeSize = edgeSize - x;
        Mc++;
        return temp;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        edge_data temp = null;
        if (E.containsKey(src) && E.get(src).containsKey(dest)) {
            temp = E.get(src).get(dest);
            E.get(src).remove(dest);
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

        public Node_data(int key , int tag , double weight , String s ){
            this.key=key;
            this.tag=tag;
            node_weight=weight;
            info=s;
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
        private final int src;
        private final int dest;
        private double weight;
        private String info = "White";
        private int tag = 0;

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
    }
}
