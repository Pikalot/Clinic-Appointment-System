package termproject.cas.assembler;

import termproject.cas.model.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SlotAssembler {
    public static Slot fromResultSet(ResultSet res) throws SQLException {
        // Assemble Provider
        Provider provider = new Provider();
        provider.setId(res.getLong("Provider_ID"));
        provider.setLastName(res.getString("Last_name"));
        provider.setClinicId(res.getLong("Clinic_ID"));
        provider.setTitle(res.getString("Title"));
        provider.setType(res.getString("Type"));

        // Assemble Clinic
        Clinic clinic = new Clinic(
                res.getInt("Clinic_ID"),
                res.getString("Clinic_name"),
                res.getString("Street"),
                res.getString("City"),
                res.getString("State"),
                res.getString("Zip_code")
        );

        // Assemble Slot
        Slot slot = new Slot();
        slot.setId(res.getLong("Slot_ID"));
        slot.setStartTime(res.getTimestamp("Start_time").toLocalDateTime());
        slot.setEndTime(res.getTimestamp("End_time").toLocalDateTime());
        slot.setProvider(provider);
        slot.setStatus(res.getString("Status"));
        slot.setClinic(clinic);

        return slot;
    }
}