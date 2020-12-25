package api;

import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class _DWGraphDSTest {
directed_weighted_graph small_graph;

    @NotNull
    public static directed_weighted_graph graphCreator(int v, int e, int seed) {
        DecimalFormat round = new DecimalFormat("##.##");
        round.setRoundingMode(RoundingMode.DOWN);
        Random rand = new Random(seed);
        directed_weighted_graph g = new DWGraph_DS();
        int node1, node2;
        double tag;


        while (g.nodeSize() < v) {
            node_data x = new DWGraph_DS.Node_data();
            g.addNode(x);
        }

        while (g.edgeSize() < e) {
            node1 = (int) (v * rand.nextDouble());
            node2 = (int) (v * rand.nextDouble());
            tag = (rand.nextDouble() * v);

            g.connect(node1, node2, (
                    Double.valueOf(round.format(tag))));
        }

        return g;
    }


    @org.junit.jupiter.api.Test
    void getNode() {
        node_data x = new DWGraph_DS.Node_data();
        int check = x.getKey();
        small_graph =  graphCreator(3 , 2 , 1);
        small_graph.addNode(x);

        assertEquals(x.getKey() , check );

        small_graph.removeNode(2);
        assertNull(small_graph.getNode(2));
    }

    @org.junit.jupiter.api.Test
    void getEdge() {
        small_graph=graphCreator(3,4,1);

        assertNotNull(small_graph.getEdge(2,1));
        small_graph.removeEdge(2,1);

        assertNull(small_graph.getEdge(2,1));


    }

    @org.junit.jupiter.api.Test
    void addNode() {
        small_graph=graphCreator(3,4,1);
        node_data n = new DWGraph_DS.Node_data();
        small_graph.addNode(n);

        assertEquals(n , small_graph.getNode(3));
    }

    @org.junit.jupiter.api.Test
    void connect() {
        small_graph=graphCreator(3,0,1);
        small_graph.connect(0,1,2);
        assertEquals(small_graph.getEdge(0,1).getWeight() ,2 );
        small_graph.removeEdge(0,1);
        assertEquals(small_graph.getEdge(0,1),null );

    }

    @org.junit.jupiter.api.Test
    void getV() {
        small_graph=graphCreator(10,14,2);

        assertEquals(small_graph.getV().size() , 10);
    }

    @org.junit.jupiter.api.Test
    void getE() {
        small_graph=graphCreator(10,14,2);
        assertEquals(small_graph.getE(2).size() , 2);

    }

    @org.junit.jupiter.api.Test
    void removeNode() {
        small_graph=graphCreator(10,14,2);
        small_graph.removeNode(2);

        assertEquals(small_graph.nodeSize(),9);
        assertEquals(small_graph.edgeSize(),9);


    }

    @org.junit.jupiter.api.Test
    void removeEdge() {
        small_graph=graphCreator(10,14,2);
small_graph.removeEdge(9,2);

assertEquals(13 , small_graph.edgeSize());
    }

    @org.junit.jupiter.api.Test
    void nodeSize() {
        small_graph= graphCreator(10,2,4);
        assertEquals(10,small_graph.nodeSize());
        small_graph.removeNode(2);
        assertEquals(9,small_graph.nodeSize());

    }

    @org.junit.jupiter.api.Test
    void edgeSize() {
        small_graph= graphCreator(10,20,4);
        assertEquals(small_graph.edgeSize(),20);

        small_graph.removeEdge(2,3);
assertEquals(small_graph.edgeSize(),19);
    }

    @org.junit.jupiter.api.Test
    void getMC() {
        small_graph=graphCreator(10,30,4);
        int check = small_graph.getMC();
        small_graph.removeEdge(2,3);
        assertEquals(check+1 , small_graph.getMC());
    }
}


