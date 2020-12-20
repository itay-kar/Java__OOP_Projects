package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.*;
import java.util.*;

public class Algo_DWGraph implements dw_graph_algorithms {
    directed_weighted_graph graph = new DS_DWGraph();
    Queue<node_data> nodeQueue;
    PriorityQueue<node_data> edgesQueue;
    Iterator<edge_data> edgeSearch;
    Iterator<node_data> nodeSearch;


    @Override
    public void init(directed_weighted_graph g) {
        this.graph = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return this.graph;
    }

    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph temp = new DS_DWGraph();

        for (node_data current_node : graph.getV()
        ) {
            temp.addNode(current_node);
        }

        for (node_data current_node : graph.getV()
        ) {
            for (edge_data current_edge : graph.getE(current_node.getKey())
            ) {
                temp.connect(current_edge.getSrc(), current_edge.getDest(), current_edge.getWeight());
                temp.getEdge(current_edge.getSrc(), current_edge.getDest()).setTag(current_edge.getTag());
            }
        }

        return temp;
    }

    public directed_weighted_graph copy2() {
        directed_weighted_graph temp = new DS_DWGraph();

        for (node_data current_node : graph.getV()
        ) {
            temp.addNode(current_node);
        }

        for (node_data current_node : graph.getV()
        ) {
            for (edge_data current_edge : graph.getE(current_node.getKey())
            ) {
                temp.connect(current_edge.getDest(), current_edge.getSrc(), current_edge.getWeight());
                temp.getEdge(current_edge.getDest(), current_edge.getSrc()).setTag(current_edge.getTag());
            }
        }

        return temp;
    }

    @Override
    public boolean isConnected() {
        boolean f;
        directed_weighted_graph temp = this.copy2();
        nodeSearch = this.graph.getV().iterator();

        node_data n = nodeSearch.next();
        f = check_a(n);
        if (!f) {
            return false;
        }


        while (nodeSearch.hasNext()) {
            node_data current = nodeSearch.next();
            f = check_b(temp, current.getKey(), n.getKey());
            if (!f) {
                return false;
            }
        }

        return true;

    }

    private boolean check_b(directed_weighted_graph temp, int src, int dest) {
        setInfo("White");
        if (temp.nodeSize() <= 1) {
            return true;
        }
        nodeQueue = new LinkedList<>();
        nodeQueue.add(temp.getNode(src));

        while (!nodeQueue.isEmpty()) {
            node_data current = nodeQueue.poll();
            current.setInfo("Black");
            edgeSearch = this.graph.getE(current.getKey()).iterator();

            while (edgeSearch.hasNext()) {
                edge_data current_edge = edgeSearch.next();
                node_data next = temp.getNode(current_edge.getDest());
                if (next.getKey() == dest) {
                    return true;
                }

                if (next.getInfo().equals("White")) {
                    nodeQueue.add(next);
                    next.setInfo("Grey");
                }
            }
        }
        return false;

    }


    private boolean check_a(node_data n) {
        if (graph.nodeSize() <= 1) {
            return true;
        }
        nodeQueue = new LinkedList<>();
        nodeQueue.add(n);
        int nodes_left = graph.nodeSize() - 1;
        setInfo("White");
        while (!nodeQueue.isEmpty()) {
            node_data current = nodeQueue.poll();
            current.setInfo("Black");
            edgeSearch = this.graph.getE(current.getKey()).iterator();

            while (edgeSearch.hasNext()) {
                edge_data current_edge = edgeSearch.next();
                node_data next = this.graph.getNode(current_edge.getDest());

                if (next.getInfo().equals("White")) {
                    nodeQueue.add(next);
                    next.setInfo("Grey");
                    nodes_left--;
                    if (nodes_left == 0) {
                        return true;
                    }
                }
            }
        }
        return false;

    }


    private void setInfo(String white) {
        for (node_data current : graph.getV()
        ) {
            current.setInfo(white);
        }
    }


    @Override
    public double shortestPathDist(int src, int dest) {
        setInfo("White");
        edgesQueue = new PriorityQueue<>(Comparator.comparingDouble(node_data::getWeight));
        edgesQueue.add(this.graph.getNode(src));

        while (!edgesQueue.isEmpty()) {
            node_data current = edgesQueue.poll();
            current.setInfo("Black");

            for (edge_data currentEdge : this.graph.getE(current.getKey())
            ) {
                if (currentEdge.getDest() == dest) {
                    return current.getWeight() + currentEdge.getWeight();
                }
                node_data n = this.graph.getNode(currentEdge.getDest());

                if (n.getInfo().equals("White")) {
                    n.setWeight(current.getWeight() + currentEdge.getWeight());
                    edgesQueue.add(n);
                    n.setInfo("Grey");
                }

                if (n.getInfo().equals("Grey")) {
                    if (n.getWeight() > (current.getWeight() + currentEdge.getWeight())) {
                        n.setWeight(current.getWeight() + currentEdge.getWeight());
                        edgesQueue.add(n);
                    }
                }

            }
        }

        return -1;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {

        setInfo("White");
        edgesQueue = new PriorityQueue<>(Comparator.comparingDouble(node_data::getWeight));
        edgesQueue.add(this.graph.getNode(src));

        while (!edgesQueue.isEmpty()) {
            node_data current = edgesQueue.poll();
            current.setInfo("Black");

            for (edge_data currentEdge : this.graph.getE(current.getKey())
            ) {
                if (currentEdge.getDest() == dest) {
                    this.graph.getNode(dest).setTag(current.getKey());
                    this.graph.getNode(dest).setWeight(current.getWeight()+currentEdge.getWeight());
                    edgesQueue.clear();
                    break;
                }
                node_data n = this.graph.getNode(currentEdge.getDest());

                if (n.getInfo().equals("White")) {
                    n.setWeight(current.getWeight() + currentEdge.getWeight());
                    edgesQueue.add(n);
                    n.setTag(current.getKey());
                    n.setInfo("Grey");
                }

                if (n.getInfo().equals("Grey")) {
                    if (n.getWeight() > (current.getWeight() + currentEdge.getWeight())) {
                        n.setWeight(current.getWeight() + currentEdge.getWeight());
                        edgesQueue.add(n);
                        n.setTag(current.getKey());
                    }
                }

            }
        }

        node_data n = graph.getNode(dest);
        List<node_data> path = new LinkedList<>();

        while (n.getKey() != src) {
            path.add(n);
            n = graph.getNode(n.getTag());
        }
        path.add(graph.getNode(src));

        List<node_data> templist = new LinkedList<>();


        for (int i=0 , j = path.size()-1 ; j>=0 ; i++ , j-- ){
            templist.add(i,path.get(j));
        }

        return templist;
    }

    @Override
    public boolean save(String file) {
        Gson graph_json= new GsonBuilder().setPrettyPrinting().create();
        String json = graph_json.toJson(graph);



        try {
            FileWriter x = new FileWriter(file);
            x.write(json);
            x.close();


            /**
             // write object to file
             PrintWriter graph_Out =new PrintWriter(new File(file));
             graph_Out.write(json);
             graph_Out.close();*/
            return true;

        }

          catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String toString() {
        Gson graph_json= new GsonBuilder().setPrettyPrinting().create();
        String json = graph_json.toJson(graph);
        return json;
    }

    @Override
    public boolean load(String file) {

            try {
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(DS_DWGraph.class, new graphJsonDecoder());
                Gson gson = builder.create();

                FileReader reader = new FileReader(file);
                directed_weighted_graph temp = gson.fromJson(reader, DS_DWGraph.class);
                init(temp);
                return true;
            }

            catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
    }



}
