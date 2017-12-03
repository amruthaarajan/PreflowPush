package preflowpush;

import preflowpush.graphcode.Edge;
import preflowpush.graphcode.SimpleGraph;
import preflowpush.graphcode.Vertex;

import java.util.*;


import static preflowpush.graphcode.GraphInput.LoadSimpleGraph;

public class tcss543 {

    static SimpleGraph G = new SimpleGraph();

    static Hashtable table;

    public static Hashtable<Edge,Integer> edge_flows = new Hashtable<>();
    public static Hashtable<Vertex,Integer> vertex_height = new Hashtable<>();
    public static Hashtable<Vertex,Integer> vertex_excess = new Hashtable<>();

    public static void main(String[] args) throws Exception {

        Hashtable<String,Vertex> input = new Hashtable<>();

        table = LoadSimpleGraph(G,"/Users/amruthaa/n10-m10-cmin5-cmax10-f30.txt");
        Set<String> keys = table.keySet();
        //Set<Edge> visited = new HashSet<>();
        for(String key: keys) {
            Vertex current_vertex = (Vertex)table.get(key);
            if (key.equals("s")) {
                vertex_height.put(current_vertex,table.size());
                vertex_excess.put(current_vertex,Integer.MAX_VALUE);
            }
            else {
                vertex_height.put(current_vertex, 1);
                vertex_excess.put(current_vertex, 0);
            }

            LinkedList incident_edges_for_current= current_vertex.getIncidentEdgeList();

            for (int i = 0; i < incident_edges_for_current.size(); i++) {
                Edge temp = (Edge) incident_edges_for_current.get(i);
                if(!isResidualEdgeAlreadyAdded(temp))
                {
                    Edge e = new Edge(temp.getSecondEndpoint(),temp.getFirstEndpoint(),0.0,null);
                    temp.getFirstEndpoint().addToIncidentEdgeList(e);
                }
            }

        }
        PreflowPush((Vertex)table.get("s"),(Vertex)table.get("t"));
        System.out.printf("Flow pushed through the graph: %d\n", vertex_excess.get((Vertex)table.get("t")));

    }

    public static boolean isResidualEdgeAlreadyAdded(Edge e)
    {
        Vertex firstVertex = e.getFirstEndpoint();
        LinkedList incident_edges_for_first_vertex = firstVertex.getIncidentEdgeList();
        for(int i=0;i< incident_edges_for_first_vertex.size();i++)
        {
            if(((Edge)incident_edges_for_first_vertex.get(i)).getFirstEndpoint().equals(e.getSecondEndpoint()))
            {
                return true;
            }
        }
        return false;
    }

    public static void PreflowPush(Vertex source, Vertex sink) {
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
        int x= ((Double)e.getData()).intValue() - tcss543.edge_flows.get(e);
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
}
