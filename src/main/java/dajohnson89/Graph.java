package dajohnson89;

import java.util.Set;

public class Graph {

    private final Set<Link> linkSet;
    private final Set<Page> pageSet;
    private Page goalPage = null;

    Graph(Set<Link> linkSet, Set<Page> pageSet) {
        this.linkSet = linkSet;
        this.pageSet = pageSet;

        for (Page page : pageSet) {
            if (page.isGoal()) {
                goalPage = page;
            }
        }
    }

    /**
     * Functionality to get the page from a specified Page.numericValue
     * WARNING: May return null, if no match is found.
     * @throws IllegalArgumentException if the Graph doesn't have a page with the specified numericValue
     * @param numericValue
     */
    public Page getPageFromLong(Long numericValue) {
        for (Page p : getPageSet()) {
            if(p.getNumericValue().equals(numericValue)) {
                return p;
            }
        }
        return null;
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

    public Page getGoalPage() {
        return goalPage;
    }
}
