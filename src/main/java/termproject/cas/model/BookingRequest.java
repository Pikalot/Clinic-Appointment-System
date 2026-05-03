package termproject.cas.model;

public class BookingRequest {
    private long mrn;
    private long slotId;
    private int serviceId;

    public BookingRequest() {}

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

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
}
