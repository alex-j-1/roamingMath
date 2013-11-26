package dajohnson89;

import java.util.Collections;
import java.util.Set;

public class Graph {

    private final Set<Link> linkSet;
    private final Set<Page> pageSet;

    /**
     * For testing only. Don't use this for-realz
     */
    protected Graph() {
        linkSet = Collections.emptySet();
        pageSet = Collections.emptySet();
    }

    Graph(Set<Link> linkSet, Set<Page> pageSet) {
        this.linkSet = linkSet;
        this.pageSet = pageSet;
    }

    /**
     * functionality to get the page from a specified Page.numericValue
     * @throws IllegalArgumentException if the Graph doesn't have a page with the specified numericValue
     * @param numericValue
     */
    public Page getPageFromLong(Long numericValue) {
        for (Page p : getPageSet()) {
            if(p.getNumericValue().equals(numericValue)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Couldn't find page corresponding to numericValue: "+ numericValue);
    }

    public void addEdge(Link link) {
        this.getLinkSet().add(link);
    }

    public Set<Link> getLinkSet() {
        return linkSet;
    }

    public Set<Page> getPageSet() {
        return pageSet;
    }

    //todo[DJ]: Add more methods
}
