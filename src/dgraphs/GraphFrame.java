package dgraphs;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.mxPartitionLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.*;

public class GraphFrame extends JPanel {

    public mxGraphComponent createGraphFromCsvData(Vector<Vector<Object>> vector_OuterData) {
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        String temp = "";
        Object v1 = null, v2 = null;
        try {
            for (int i = 0; i < vector_OuterData.size(); i++) {
                if (vector_OuterData.get(i) != null) {
                    if (i == 0) {
                        temp = vector_OuterData.get(i).get(0).toString();
                        v1 = graph.insertVertex(parent, null, vector_OuterData.get(i).get(0), 30, 20 + (i * 40), 30, 30);
                        v2 = graph.insertVertex(parent, null, vector_OuterData.get(i).get(1), 240, 20 + (i * 30), 30, 30);
                        graph.insertEdge(parent, null, vector_OuterData.get(i).get(2), v1, v2);
                    } else {
                        if (temp.equalsIgnoreCase(vector_OuterData.get(i).get(0).toString())) {
//                          v2 = graph.insertVertex(parent, null, vector_OuterData.get(i).get(1), 240, 20 + (i * 30), 30, 30);
                            graph.insertEdge(parent, null, vector_OuterData.get(i).get(2), v1, v1);
                        } else {
                            temp = vector_OuterData.get(i).get(0).toString();
                            v1 = graph.insertVertex(parent, null, vector_OuterData.get(i).get(0), 30, 20 + (i * 40), 30, 30);
                            v2 = graph.insertVertex(parent, null, vector_OuterData.get(i).get(1), 240, 20 + (i * 30), 30, 30);
                            graph.insertEdge(parent, null, vector_OuterData.get(i).get(2), v1, v2);
                        }
                    }

                }
            }
            graph.setBorder(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            graph.getModel().endUpdate();
        }
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        return graphComponent;
    }

    mxGraphComponent createGraphFromCsvData(DirectedGraphStructure<Integer> graphStructure, String graphLayoutType) {
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        int VERTEX_WIDTH = 30;
        int VERTEX_HEIGHT = 30;
        int DEFAULT_X = 60;
        int DEFAULT_Y = 60;

        try {
            Iterator<Integer> iterator = graphStructure.getNeighbors().keySet().iterator();
            Vector<Integer> vector_VertexDrawn = new Vector<>();
            System.out.println("*** \n " + graphStructure.getNeighbors().keySet().toArray() + "\n" + graphStructure.getNeighbors().keySet().toString());
            Object srcVertex = null, dstVertex = null;
            while (iterator.hasNext()) {
                int vertexValue = iterator.next();
                srcVertex = (mxCell) ((mxGraphModel) graph.getModel()).getCell("V" + vertexValue);
                if (!vector_VertexDrawn.contains(vertexValue)) {
                    srcVertex = graph.insertVertex(parent, "V" + vertexValue, vertexValue, DEFAULT_X, DEFAULT_Y, VERTEX_WIDTH, VERTEX_HEIGHT, "ROUNDED;strokeColor=blue;fillColor=#61ED74");
                    vector_VertexDrawn.add(vertexValue);
                    System.out.println(" Added Vertex to Graph Canvas: " + vertexValue);
                }
                List<Integer> list_InBoundNeighbours = graphStructure.inboundNeighbors(vertexValue);
                List<Integer> list_OutBoundNeighbours = graphStructure.outboundNeighbors(vertexValue);
                for (Integer list_OutBoundNeighbour : list_OutBoundNeighbours) {
                    if (graphStructure.contains(list_OutBoundNeighbour)) {
                        if (!vector_VertexDrawn.contains(list_OutBoundNeighbour)) {
                            dstVertex = graph.insertVertex(parent, "V" + list_OutBoundNeighbour, list_OutBoundNeighbour, DEFAULT_X, DEFAULT_Y, VERTEX_WIDTH, VERTEX_HEIGHT, "ROUNDED;strokeColor=blue;fillColor=#61ED74");
                            vector_VertexDrawn.add(list_OutBoundNeighbour);
                            graph.insertEdge(parent, null, graphStructure.getCost(vertexValue, list_OutBoundNeighbour), srcVertex, dstVertex);
                        } else if (vertexValue == list_OutBoundNeighbour) { // For Self-Pointing Nodes
                            graph.insertEdge(parent, null, graphStructure.getCost(vertexValue, list_OutBoundNeighbour), srcVertex, srcVertex);
                        } else {
                            dstVertex = (mxCell) ((mxGraphModel) graph.getModel()).getCell("V" + list_OutBoundNeighbour);
                            graph.insertEdge(parent, null, graphStructure.getCost(vertexValue, list_OutBoundNeighbour), srcVertex, dstVertex);
                        }

                        System.out.println("Edge :[" + vertexValue + "," + list_OutBoundNeighbour + "]");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
//        setGraphLayout(graph, "Circle");
//        setGraphLayout(graph, "Hierarchial");
//        setGraphLayout(graph, "Parallel Edge");
//        setGraphLayout(graph, "Tree");
//        setGraphLayout(graph, "Edge Label");
        setGraphLayout(graph, graphLayoutType);
        graph.setCellsMovable(true);
        graphComponent.getViewport().setBackground(Color.WHITE);

        // add rubberband zoom
        new mxRubberband(graphComponent) {

            @Override
            public void mouseReleased(MouseEvent e) {
                // get bounds before they are reset
                Rectangle rect = bounds;
                // invoke usual behaviour
                super.mouseReleased(e);
                if (rect != null) {
                    double newScale = 1;
                    Dimension graphSize = new Dimension(rect.width, rect.height);
                    Dimension viewPortSize = graphComponent.getViewport().getSize();

                    int gw = (int) graphSize.getWidth();
                    int gh = (int) graphSize.getHeight();

                    if (gw > 0 && gh > 0) {
                        int w = (int) viewPortSize.getWidth();
                        int h = (int) viewPortSize.getHeight();

                        newScale = Math.min((double) w / gw, (double) h / gh);
                    }
                    // zoom to fit selected area
                    graphComponent.zoom(newScale);
                    // make selected area visible 
                    graphComponent.getGraphControl().scrollRectToVisible(new Rectangle((int) (rect.x * newScale), (int) (rect.y * newScale), (int) (rect.width * newScale), (int) (rect.height * newScale)));

                }

            }

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {
                        // zoom to fit selected area
                        graphComponent.zoomOut();
                }
            }

        };
//        mxConstants.DEFAULT_HOTSPOT = 1;
        DirectedGraphWindow.setGraphOutline(new mxGraphOutline(graphComponent));
        return graphComponent;
    }

    private void setGraphLayout(mxGraph graph, String layoutName) {
        switch (layoutName) {
            case "Hierarchial":
                setHierarchialLayout(graph);
                break;

            case "Parallel Edge":
                setParallelEdgeLayout(graph);
                break;

            case "Tree":
                setCompactTreeLayout(graph);
                break;

            case "Circle":
                setCircleLayout(graph);
                break;

            case "Edge Label":
                setEdgeLabelLayout(graph);
                break;

            case "Fast Organic":
                setOrganicLayout(graph);
                break;

            case "Partition":
                setPartitionLayout(graph);
                break;

            default:
                setOrganicLayout(graph);
                break;
        }

        graph.setAutoSizeCells(true);
//      mxConstants.STYLE_EDGE = mxEdgeStyle.SegmentConnector.toString();
        graph.setEdgeLabelsMovable(false);
    }

    private void setHierarchialLayout(mxGraph graph) {
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        //changes the default vertex style in place
        layout.setIntraCellSpacing(50);
        layout.setInterHierarchySpacing(100);
        layout.setParallelEdgeSpacing(40);
        layout.setFineTuning(true);
        layout.execute(graph.getDefaultParent());
    }

    private void setParallelEdgeLayout(mxGraph graph) {
        mxParallelEdgeLayout layout = new mxParallelEdgeLayout(graph, 50);
        layout.execute(graph.getDefaultParent());
    }

    private void setCompactTreeLayout(mxGraph graph) {
        mxCompactTreeLayout mTreeLayout = new mxCompactTreeLayout(graph);
        mTreeLayout.setNodeDistance(60);
        mTreeLayout.setEdgeRouting(true);
        mTreeLayout.execute(graph.getDefaultParent());
    }

    private void setCircleLayout(mxGraph graph) {
        mxGraphView mView = new mxGraphView(graph);
        mView.setScale(100);
        new mxCircleLayout(graph, 60).execute(graph.getDefaultParent());
    }

    private void setEdgeLabelLayout(mxGraph graph) {
        mxEdgeLabelLayout layout = new mxEdgeLabelLayout(graph);
        layout.execute(graph.getDefaultParent());
    }

    private void setDefaultLayout(mxGraph graph) {
        mxGraphView mView = new mxGraphView(graph);
        mView.setScale(100);
        new mxHierarchicalLayout(graph).execute(graph.getDefaultParent());
        new mxParallelEdgeLayout(graph).execute(graph.getDefaultParent());
    }

    private void setOrganicLayout(mxGraph graph) {
        mxFastOrganicLayout layout = new mxFastOrganicLayout(graph);
        layout.setForceConstant(60);
        layout.execute(graph.getDefaultParent());
        layout.setMinDistanceLimit(20);
        graph.alignCells(mxConstants.ALIGN_CENTER);
    }

    private void setPartitionLayout(mxGraph graph) {

        mxPartitionLayout mPartitionLayout = new mxPartitionLayout(graph, false, 60);
        mPartitionLayout.execute(graph.getDefaultParent());

    }

    //<editor-fold defaultstate="collapsed" desc="main Method">
    //    public static void main(String[] args) {
//        mxGraph graph = new mxGraph();
//        Object parent = graph.getDefaultParent();
//        graph.getModel().beginUpdate();
//        try {
//            Object v1 = graph.insertVertex(parent, null, "Wolf", 20, 20, 80,
//                    30);
//            Object v2 = graph.insertVertex(parent, null, "Pack", 240, 150,
//                    80, 30);
//            graph.insertEdge(parent, null, "Edge", v1, v2);
//            graph.setBorder(0);
//        } finally {
//            graph.getModel().endUpdate();
//        }
//        mxGraphComponent graphComponent = new mxGraphComponent(graph);
//        GraphFrame panel = new GraphFrame();
//        JFrame frame = new JFrame();
//        panel.add(graphComponent, new GridLayout(1, 1));
//        panel.setPreferredSize(new Dimension(600, 600));
//        frame.setLayout(new GridLayout(1, 1));
//        frame.add(graphComponent);
//        frame.setVisible(true);
//        frame.pack();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
//</editor-fold>
}
