package api;

import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class _DWGraphAlgoTest {

    public static directed_weighted_graph graphCreator(int v, int e, int seed) {
        DecimalFormat round = new DecimalFormat("##.##");
        round.setRoundingMode(RoundingMode.DOWN);
        Random rand = new Random(seed);
        directed_weighted_graph g = new DWGraph_DS();
        int node1, node2;
        double tag;

        while (g.nodeSize() < v) {
            DWGraph_DS.Node_data x = new DWGraph_DS.Node_data();
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

    @Test
    void init() {
        dw_graph_algorithms check = new DWGraph_Algo();
        directed_weighted_graph root = graphCreator(3, 2, 1);
        check.init(root);

        assertEquals(root, check.getGraph());
    }

    @Test
    void getGraph() {
        dw_graph_algorithms check = new DWGraph_Algo();
        directed_weighted_graph root = graphCreator(2, 1, 1);
        check.init(root);

        assertEquals(root, check.getGraph());
    }

    @Test
    void copy() {
        dw_graph_algorithms check = new DWGraph_Algo();
        directed_weighted_graph root = graphCreator(100, 100, 1);
        check.init(root);

        directed_weighted_graph root2 = check.copy();

        assertEquals(root, root2);

    }


    @Test
    void isConnected() {
        dw_graph_algorithms check = new DWGraph_Algo();
        directed_weighted_graph root = graphCreator(5, 5, 1);
        root.connect(4, 1, 2);
        root.connect(2, 1, 2);
        root.connect(3, 1, 2);
        root.connect(1, 2, 2);
        root.connect(1, 3, 2);
        root.connect(1, 4, 2);
        root.connect(1, 0, 2);
        root.connect(0, 1, 2);

        check.init(root);

        boolean f = check.isConnected();
        assertTrue(f);

        root.removeEdge(4, 1);
        root.connect(0, 4, 1);
        assertFalse(check.isConnected());

        root.connect(4, 1, 1);
        root.removeEdge(1, 4);
        assertTrue(check.isConnected());


    }

    @Test
    void shortestPathDist() {
        dw_graph_algorithms check = new DWGraph_Algo();
        directed_weighted_graph root = graphCreator(10, 20, 1);

        check.init(root);
        double d = (8.36 + 7.45 + 0.08 + 8.8);
        assertEquals(d, check.shortestPathDist(1, 8), 0.00000000000001);
    }

    @Test
    void shortestPath() {

        dw_graph_algorithms check = new DWGraph_Algo();
        directed_weighted_graph root = graphCreator(10, 20, 1);
        check.init(root);
        List<node_data> sp = check.shortestPath(1, 8);
        int[] checkKey = {1, 6, 2, 5, 9, 8};
        int i = 0;
        for (node_data n : sp) {
            //assertEquals(n.getTag(), checkTag[i]);
            assertEquals(n.getKey(), checkKey[i]);
            i++;
        }

        ;
    }

    @Test
    void save_load() {
        directed_weighted_graph g0 = graphCreator(500, 10000, 1);
        dw_graph_algorithms ag0 = new DWGraph_Algo();
        ag0.init(g0);
        String str = "Try.json";

        ag0.save(str);
        directed_weighted_graph g1 = graphCreator(500, 10000, 1);
        assertTrue(ag0.load(str));
        assertNotSame(g0,g1);
        assertEquals(g0, g1);
        g0.removeNode(0);
        assertNotEquals(g0, g1);
    }
}
