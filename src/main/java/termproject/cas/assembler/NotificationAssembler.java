package termproject.cas.assembler;
import termproject.cas.model.*;

public class NotificationAssembler {
    public static NotificationRequest toRequest(Appointment appt) {
        NotificationRequest req = new NotificationRequest();

        req.setPatient(appt.getPatient().getFirstName());
        req.setProvider(appt.getAvailableSlot().getProvider().getFirstName());
        req.setDate(appt.getAvailableSlot().getStartTime().toString());
        req.setContact(appt.getPatient().getPrimaryEmail());

        return req;
    }
}
