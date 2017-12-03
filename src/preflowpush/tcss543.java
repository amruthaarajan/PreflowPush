package preflowpush;

import preflowpush.graphcode.SimpleGraph;

import static preflowpush.graphcode.GraphInput.LoadSimpleGraph;

public class tcss543 {


    public static void main(String[] args) throws Exception {
        SimpleGraph G = new SimpleGraph();
        PreFlowPush p= new PreFlowPush(LoadSimpleGraph(G,args[0]));
        int maxFlow = p.computeMaxFlow();
        System.out.printf("Flow for the graph using Preflow Push: %d\n", maxFlow);

    }







}
