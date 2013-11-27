package dajohnson89;

import java.util.ArrayList;
import java.util.List;

public class Page {

    private boolean isDeadEnd;
    private boolean isGoal;
    private Long numericValue;
    private List<Link> outgoingList;

    public Page(Long numericValue){
        this.numericValue = numericValue;
        this.outgoingList = new ArrayList<>();
    }

    public Page(java.lang.Long numericValue, List<Link> outgoingList) {
        setNumericValue(numericValue);
        setOutgoingList(outgoingList);
    }

    public List<Link> getOutgoingList() {
        return outgoingList;
    }

    public void setOutgoingList(List<Link> outgoingList) {
        this.outgoingList = outgoingList;
    }

    public boolean isDeadEnd() {
        return isDeadEnd;
    }

    public void setIsDeadEnd(boolean isDeadEnd) {
        this.isDeadEnd = isDeadEnd;
    }

    public boolean isGoal() {
        return isGoal;
    }

    public void setIsGoal(boolean isGoal) {
        this.isGoal = isGoal;
    }

    public java.lang.Long getNumericValue() {
        return numericValue;
    }

    public void setNumericValue(java.lang.Long numericValue) {
        this.numericValue = numericValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        if (isDeadEnd != page.isDeadEnd) return false;
        if (isGoal != page.isGoal) return false;
        if (numericValue != null ? !numericValue.equals(page.numericValue) : page.numericValue != null) return false;
        if (outgoingList != null ? !outgoingList.equals(page.outgoingList) : page.outgoingList != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (isDeadEnd ? 1 : 0);
        result = 31 * result + (isGoal ? 1 : 0);
        result = 31 * result + (numericValue != null ? numericValue.hashCode() : 0);
        result = 31 * result + (outgoingList != null ? outgoingList.hashCode() : 0);
        return result;
    }
}

