package preflowpush;

import preflowpush.graphcode.Edge;
import preflowpush.graphcode.SimpleGraph;
import preflowpush.graphcode.Vertex;

import java.util.*;


import static preflowpush.graphcode.GraphInput.LoadSimpleGraph;

public class tcss543 {

    static SimpleGraph G = new SimpleGraph();

//    static Hashtable table;

    public static Hashtable<Edge,Integer> edge_flows = new Hashtable<>();
    public static Hashtable<Vertex,Integer> vertex_height = new Hashtable<>();
    public static Hashtable<Vertex,Integer> vertex_excess = new Hashtable<>();

    public static void main(String[] args) {

        Hashtable<String,Vertex> input = new Hashtable<>();

//        table = LoadSimpleGraph(G,"/Users/amruthaa/g1.txt");
//        Set<String> keys = table.keySet();
//        for(String key: keys) {
//            if (key.equals("s")) {
//                vertex_height.put((Vertex)table.get(key),table.size());
//                vertex_excess.put((Vertex)table.get(key),Integer.MAX_VALUE);
//            }
//            else {
//                vertex_height.put((Vertex) table.get(key), 1);
//                vertex_excess.put((Vertex) table.get(key), 0);
//            }
//        }
//        PreflowPush((Vertex)table.get("s"),(Vertex)table.get("t"));
//        System.out.printf("Flow pushed through the graph: %d\n", vertex_excess.get((Vertex)table.get("t")));

        Vertex source = new Vertex(null,"s");
        Vertex sink = new Vertex(null,"t");


        Vertex a = new Vertex(null,"a");
        Vertex b = new Vertex(null,"b");
        Vertex c = new Vertex(null,"c");
        Vertex d = new Vertex(null,"d");
        Vertex e = new Vertex(null,"e");
        new Edge(a, source, "0",null);
        new Edge(source, a, "5",null);
        new Edge(b, source, "0",null);
        new Edge(source, b, "3",null);
        new Edge(c, source, "0",null);
        new Edge(source, c, "12",null);
        new Edge(c, a, "0",null);
        new Edge(d, a, "0",null);
        new Edge(d, c, "0",null);
        new Edge(e, c, "0",null);
        new Edge(e, b, "0",null);
        new Edge(a, c, "2",null);
        new Edge(a, d, "6",null);
        new Edge(c, d, "1",null);
        new Edge(c, e, "2",null);
        new Edge(b, e, "4",null);
        new Edge(sink, c, "0",null);
        new Edge(sink, d, "0",null);
        new Edge(sink, e, "0",null);
        new Edge(c, sink, "2",null);
        new Edge(d, sink, "8",null);
        new Edge(e, sink, "11",null);


        input.put("s",source);
        input.put("a", a);
        input.put("b", b);
        input.put("c", c);
        input.put("d",d);
        input.put("e", e);
        input.put("t",sink);

        Set<String> keys = input.keySet();
        for(String key: keys) {
            if (key.equals("s")) {
                vertex_height.put((Vertex)input.get(key),input.size());
                vertex_excess.put((Vertex)input.get(key),Integer.MAX_VALUE);
            }
            else {
                vertex_height.put((Vertex) input.get(key), 1);
                vertex_excess.put((Vertex) input.get(key), 0);
            }
        }

        PreflowPush((Vertex)input.get("s"),(Vertex)input.get("t"));
        System.out.printf("Flow pushed through the graph: %d\n", vertex_excess.get((Vertex)input.get("t")));

    }

    public static Edge returnEdgeBetweenVertexs(Vertex v1, Vertex v2)
    {
        Iterator it = v1.getIncidentEdgeList().iterator();
        while (it.hasNext()) {
            Edge e = (Edge) it.next();
            if(e.getSecondEndpoint().equals(v2))
            {
                return e;
            }
        }
        return null;
    }

    public static void PreflowPush(Vertex source, Vertex sink) {
        // source.height should be the number of Vertexs, sink.height should be 1
        LinkedList<Vertex> q = new LinkedList<Vertex>();
        q.add(source);
        while (!q.isEmpty()) {
            Vertex Vertex = q.remove();
            int minHeight = Integer.MAX_VALUE;
            LinkedList incident_edges = Vertex.getIncidentEdgeList();
            for (int i = 0; i < incident_edges.size(); i++) {
                if (vertex_height.get(getSecond(incident_edges,i)) < minHeight
                        && remaining((Edge)incident_edges.get(i)) > 0)
                    minHeight = vertex_height.get(getSecond(incident_edges,i));
            }
            if (minHeight != Integer.MAX_VALUE && minHeight >= vertex_height.get(Vertex))
                vertex_height.put(Vertex,minHeight + 1);
            for (int i = 0; i < incident_edges.size(); i++) {
                if (vertex_height.get(getSecond(incident_edges,i)) < vertex_height.get(Vertex)) {
                    int pushedFlow = remaining((Edge)incident_edges.get(i));
                    if (pushedFlow > vertex_excess.get(Vertex))
                        pushedFlow = vertex_excess.get(Vertex);
                    if (pushedFlow > 0) {
                        int value1 = edge_flows.get(incident_edges.get(i)) + pushedFlow;
                        tcss543.edge_flows.put((Edge)incident_edges.get(i),value1);
                        Edge residual_edge = returnEdgeBetweenVertexs(getSecond(incident_edges,i),getFirst(incident_edges,i));
                        int value2 = edge_flows.get(residual_edge) - pushedFlow;
                        tcss543.edge_flows.put(residual_edge,value2);
                        int value3 = vertex_excess.get(getSecond(incident_edges,i)) + pushedFlow;
                        vertex_excess.put(getSecond(incident_edges,i),value3);
                        int value4 = vertex_excess.get(Vertex) - pushedFlow;
                        vertex_excess.put(Vertex,value4);

                        if (!isSourceOrSink(getSecond(incident_edges,i)))
                            q.add(getSecond(incident_edges,i));
                        if (vertex_excess.get(Vertex) <= 0)
                            break;
                    }
                }
            }
            if (vertex_excess.get(Vertex) > 0 && !isSourceOrSink(Vertex))
                q.add(Vertex);
        }
    }

    public static boolean isSourceOrSink(Vertex v)
    {
        if(v.getName().equals("s") || v.getName().equals("t"))
        {
            return true;
        }
        return false;
    }

    private static int remaining(Edge e) {
//        int x= ((Double)e.getData()).intValue() - tcss543.edge_flows.get(e);
        int x= Integer.parseInt(e.getData().toString()) - tcss543.edge_flows.get(e);
        return x;
    }

    private static Vertex getFirst(LinkedList edges,int i)
    {
        return ((Edge) edges.get(i)).getFirstEndpoint();
    }

    private static Vertex getSecond(LinkedList edges,int i)
    {
        return ((Edge) edges.get(i)).getSecondEndpoint();
    }

}
