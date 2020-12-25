package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gameClient.util.Point3D;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Ex2 implements Runnable {
    private static DWGraph_Algo graphAlgo = new DWGraph_Algo();
    private static MyFrame _win;
    private static Arena _ar;
    private static HashMap<Integer, HashMap<Integer, Integer>> routeMap = new HashMap<>();
    private static HashMap<Integer, CL_Pokemon> AgentsNextPoke = new HashMap<>();

    public static void main(String[] a) {
        Thread client = new Thread(new Ex2());
        client.start();
    }

    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen (randomly).
     *
     * @param game
     * @param gg
     * @param
     */
    private static void moveAgants(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<CL_Agent> clAgents = Arena.getAgents(lg, gg);
        _ar.setAgents(clAgents);
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);
        _ar.setPokemonsPos();
        for (int i = 0; i < clAgents.size(); i++) {
            CL_Agent ag = clAgents.get(i);
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            if (dest == -1) {
                dest = nextNode(gg, src, id);
                game.chooseNextEdge(ag.getID(), dest);
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
            }
        }
    }

    /**
     * a very simple random walk implementation!
     *
     * @param g
     * @param src
     * @param agentId
     * @return
     */
    private static int nextNode(directed_weighted_graph g, int src, int agentId) {
        dw_graph_algorithms algo = new DWGraph_Algo();
        algo.init(g);
        List<CL_Pokemon> pokList = _ar.getPokemons();
        double distance = Double.POSITIVE_INFINITY;
        int dest = -1;
        int pokdest = -1;
        int pokesrc = -1;
        double temp;
        CL_Pokemon nextPoke = null;
        CL_Agent currentAgent = _ar.getAgents().get(agentId);


        if (_ar.getAgents().get(agentId).get_curr_fruit() != null) {
            nextPoke = _ar.getAgents().get(agentId).get_curr_fruit();
            dest = routeMap.get(src).get(nextPoke.get_edge().getDest());
            if (nextPoke.get_edge().getDest() == src && nextPoke.get_edge().getSrc() != dest) {
                return nextPoke.get_edge().getSrc();
            } else
                return dest;
        }



        for (int i = 0; i < pokList.size(); i++) {
            CL_Pokemon current = pokList.get(i);
            if (!currentisVisited(current , agentId)) {
                temp = algo.shortestPathDist(src, current.get_edge().getDest());
                if (temp > 0 && temp < distance) {
                    distance = temp;
                    pokdest = current.get_edge().getDest();
                    pokesrc = current.get_edge().getSrc();
                    nextPoke = current;
                   currentAgent.set_curr_fruit(current);
                }
            }}


        for (CL_Pokemon pok : pokList
        ) {
            if (pok.get_edge() == nextPoke.get_edge()) {
                pok.setMin_ro(agentId);
                pok.setMin_dist(2);
            } else if(nextPoke.getMin_dist()!=2){
                pok.setMin_ro(-1);
            }}


        int n = HasPokeBefore(src , nextPoke);
if(routeMap.get(src).get(pokdest)!=null) {
    dest = routeMap.get(src).get(pokdest);
    if(dest==src && currentAgent.get_curr_fruit().get_edge().getDest()==src){
        return routeMap.get(src).get(currentAgent.get_curr_fruit().get_edge().getSrc());
    }
}

        return dest;
    }

    private static boolean currentisVisited(CL_Pokemon current, int agentId) {

        for (CL_Agent agent: _ar.getAgents()
             ) {
            if(agent.get_curr_fruit()==current && agent.getID()!=agentId){
                return true;
            }
        }
        return false;
    }


    private static int HasPokeBefore(int src, CL_Pokemon nextPoke) {

        for (CL_Pokemon poke: _ar.getPokemons()
             ) {
            if(poke.get_edge().getDest()==src && poke!=nextPoke){
                return poke.get_edge().getSrc();
            }
        }

        return -1;
    }


    @Override
    public void run() {
        int scenario_num = 1;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        //	int id = 999;
        //	game.login(id);
        String g = game.getGraph();
        String pks = game.getPokemons();
        directed_weighted_graph gg = graphAlgo.toGraph(g);
        makeRoutes(gg);



        init(game);

        for (CL_Agent nextagent : _ar.getAgents()){
            AgentsNextPoke.put(nextagent.getID() , null);
        }

        game.startGame();
        _win.setTitle("Ex2 - OOP: (NONE trivial Solution) " + game.toString());
        int ind = 0;
        long dt = 120;
        double dtspeed=averageSpeed();
        while (game.isRunning()) {
            moveAgants(game, gg);
            try {
                if (ind % 1 == 0) {
                    _win.repaint();
                }
               double speed = averageSpeed();

                if(speed>1){dt=100;}
                //if(speed>1.5){dt=70;}
                if(speed>2){dt=90;}
                if(speed>3){dt=80;}
              //  if(speed>3){dt=50;}
              /*  if(dtspeed<speed) {
                    dtspeed = speed;
                    if (dt > 100) {
                        dt = dt - (dt / 4 * _ar.getAgents().size());
                    }
                    else dt=dt-(dt/12*_ar.getAgents().size());
                }*/
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }

    private Double averageSpeed() {
        double averagespeed=0;
        for (CL_Agent agent:_ar.getAgents()
             ) {
            averagespeed=averagespeed+agent.getSpeed();
        }

        averagespeed=averagespeed/_ar.getAgents().size();

        return averagespeed;
    }


    private directed_weighted_graph rebuildGraph(String g) {
        directed_weighted_graph temp = new DWGraph_DS();
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(DWGraph_DS.class, new rebuildGraphDecoder());
            Gson gson = builder.create();

            FileReader reader = new FileReader(g);
            temp = gson.fromJson(reader, DWGraph_DS.class);

            return temp;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }

    private void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = graphAlgo.toGraph(g);

        graphAlgo.init(gg);


        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
        _win = new MyFrame(_ar, "test Ex2");
        _win.setSize(1000, 700);


        _win.show();
        String info = game.toString();
        JSONObject line;

        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            List<CL_Pokemon> cl_fs = _ar.getPokemons();
            for (int a = 0; a < cl_fs.size(); a++) {
                _ar.updateEdge(cl_fs.get(a), gg);
            }
            for (int a =0 ; a<cl_fs.size() ; a++){
                    int ind = a % cl_fs.size();
                    CL_Pokemon c = cl_fs.get(ind);
                    if (c.getMin_ro() != -5) {
                        int nn = c.get_edge().getDest();
                        if (c.getType() < 0) {
                            nn = c.get_edge().getSrc();
                        }
                        c.setMin_ro(-5);
                        game.addAgent(nn);
                    }
                }

            _ar.setAgents(_ar.getAgents(game.getAgents(), gg));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void chooseStartNode(int[] startnodes) {
        double distancemin = Double.POSITIVE_INFINITY;
        double distancemax = 0;


        }




    private void makeRoutes(directed_weighted_graph gg) {
        dw_graph_algorithms d = new DWGraph_Algo();
        d.init(gg);

        for (node_data x : gg.getV()
        ) {
            routeMap.put(x.getKey(), new HashMap<>());
            Iterator<node_data> iter = gg.getV().iterator();
            while (iter.hasNext()) {
                node_data current = iter.next();
                List<node_data> route = d.shortestPath(x.getKey(), current.getKey());
                double routeWeight = d.shortestPathDist(x.getKey() , current.getKey());
                if (route.size() > 1) {
                    node_data routeNode = new DWGraph_DS.Node_data(route.get(1).getKey());
                    routeNode.setWeight(routeWeight);
                    routeMap.get(x.getKey()).put(current.getKey(), routeNode.getKey());

                } else {
                    routeMap.get((x.getKey())).put(current.getKey(), null);
                }
            }
        }

        String json = d.toString();

    }
}

