package dajohnson89;

import java.util.Collections;
import java.util.Set;

public class Graph<V,E> {

    private final Set<E> edgeSet;
    private final Set<V> vertexSet;

    /**
     * For testing only. Don't use this for-realz
     */
    protected Graph() {
        edgeSet = Collections.emptySet();
        vertexSet = Collections.emptySet();
    }

    Graph(Set<E> edgeSet, Set<V> vertexSet) {
        this.edgeSet = edgeSet;
        this.vertexSet = vertexSet;
    }

    public void addEdge(E edge) {
        this.getEdgeSet().add(edge);
    }

    public Set<E> getEdgeSet() {
        return edgeSet;
    }

    public Set<V> getVertexSet() {
        return vertexSet;
    }

    //todo[DJ]: Add more methods
}
