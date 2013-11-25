package dajohnson89;

public class Link {
    private Long sourceID;
    private Long destinationID;

    public Link(Long sourceID, Long destinationID) {
        setSourceID(sourceID);
        setDestinationID(destinationID);
    }

    public Long getSourceID() {
        return sourceID;
    }

    public void setSourceID(Long sourceID) {
        this.sourceID = sourceID;
    }

    public Long getDestinationID() {
        return destinationID;
    }

    public void setDestinationID(Long destinationID) {
        this.destinationID = destinationID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Link link = (Link) o;

        if (destinationID != null ? !destinationID.equals(link.destinationID) : link.destinationID != null) return false;
        if (sourceID != null ? !sourceID.equals(link.sourceID) : link.sourceID != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sourceID != null ? sourceID.hashCode() : 0;
        result = 31 * result + (destinationID != null ? destinationID.hashCode() : 0);
        return result;
    }
}
