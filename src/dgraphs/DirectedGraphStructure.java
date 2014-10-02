package dgraphs;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.*;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import org.jgrapht.ext.JGraphModelAdapter;

public class DirectedGraphStructure<V> {

    private JGraphModelAdapter m_jgAdapter;

    /**
     * @return the neighbors
     */
    public Map<V, List<Edge<V>>> getNeighbors() {
        return neighbors;
    }

    /**
     * @param neighbors the neighbors to set
     */
    public void setNeighbors(Map<V, List<Edge<V>>> neighbors) {
        this.neighbors = neighbors;
    }

    public static class Edge<V> {

        private V vertex;
        private int cost;

        public Edge(V v, int c) {
            vertex = v;
            cost = c;
        }

        public V getVertex() {
            return vertex;
        }

        public int getCost() {
            return cost;
        }

        @Override
        public String toString() {
            return "Edge [vertex=" + vertex + ", cost=" + cost + "]";
        }

    }

    /**
     * A Map is used to map each vertex to its list of adjacent vertices.
     */
    private Map<V, List<Edge<V>>> neighbors = new HashMap<V, List<Edge<V>>>();

    private int nr_edges;

    /**
     * String representation of graph.
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        for (V v : getNeighbors().keySet()) {
            s.append("\n    " + v + " -> " + getNeighbors().get(v));
        }
        return s.toString();
    }

    /**
     * Add a vertex to the graph. Nothing happens if vertex is already in graph.
     *
     * @param vertex
     */
    public void add(V vertex) {
        if (getNeighbors().containsKey(vertex)) {
            return;
        }
        getNeighbors().put(vertex, new ArrayList<Edge<V>>());
    }

    public int getNumberOfEdges() {
        int sum = 0;
        for (List<Edge<V>> outBounds : getNeighbors().values()) {
            sum += outBounds.size();
        }
        return sum;
    }

    /**
     * True iff graph contains vertex.
     *
     * @param vertex
     * @return
     */
    public boolean contains(V vertex) {
        return getNeighbors().containsKey(vertex);
    }

    /**
     * Add an edge to the graph; if either vertex does not exist, it's added.
     * This implementation allows the creation of multi-edges and self-loops.
     *
     * @param from
     * @param to
     * @param cost
     */
    public void add(V from, V to, int cost) {
        this.add(from);
        this.add(to);
        getNeighbors().get(from).add(new Edge<V>(to, cost));
    }

    public int outDegree(int vertex) {
        return getNeighbors().get(vertex).size();
    }

    public int inDegree(V vertex) {
        return inboundNeighbors(vertex).size();
    }

    public List<V> outboundNeighbors(V vertex) {
        List<V> list = new ArrayList<V>();
        for (Edge<V> e : getNeighbors().get(vertex)) {
            list.add(e.vertex);
        }
        return list;
    }

    public List<V> inboundNeighbors(V inboundVertex) {
        List<V> inList = new ArrayList<V>();
        for (V to : getNeighbors().keySet()) {
            for (Edge e : getNeighbors().get(to)) {
                if (e.vertex.equals(inboundVertex)) {
                    inList.add(to);
                }
            }
        }
        return inList;
    }

    public boolean isEdge(V from, V to) {
        for (Edge<V> e : getNeighbors().get(from)) {
            if (e.vertex.equals(to)) {
                return true;
            }
        }
        return false;
    }

    public int getCost(V from, V to) {
        for (Edge<V> e : getNeighbors().get(from)) {
            if (e.vertex.equals(to)) {
                return e.cost;
            }
        }
        return -1;
    }

    private void positionVertexAt(Object vertex, int x, int y) {
        DefaultGraphCell cell = m_jgAdapter.getVertexCell(vertex);
        Map attr = cell.getAttributes();
        Rectangle b = (Rectangle) GraphConstants.getBounds(attr);
        GraphConstants.setBounds(attr, new Rectangle(x, y, b.width, b.height));
        Map cellAttr = new HashMap();
        cellAttr.put(cell, attr);
        m_jgAdapter.edit(cellAttr, null, null, null);
    }

}
