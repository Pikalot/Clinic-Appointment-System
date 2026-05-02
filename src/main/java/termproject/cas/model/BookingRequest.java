package termproject.cas.model;

public class BookingRequest {
    private long mrn;
    private long slotId;

    public BookingRequest() {}

    public BookingRequest(long mrn, long slotId) {
        this.setMrn(mrn);
        this.setSlotId(slotId);
    }

    public long getMrn() {
        return mrn;
    }

    public void setMrn(long mrn) {
        this.mrn = mrn;
    }

    public long getSlotId() {
        return slotId;
    }

    public void setSlotId(long slotId) {
        this.slotId = slotId;
    }
}
