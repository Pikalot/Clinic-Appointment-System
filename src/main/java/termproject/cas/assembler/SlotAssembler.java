package termproject.cas.assembler;

import termproject.cas.model.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SlotAssembler {
    public static Slot fromResultSet(ResultSet res) throws SQLException {
        // Assemble Provider
        Provider provider = ProviderAssembler.fromResultSet(res);

        // Assemble Clinic
        Clinic clinic = ClinicAssembler.fromResultSet(res);

        // Assemble Slot
        Slot slot = new Slot();
        slot.setId(res.getLong("Slot_ID"));
        slot.setStartTime(res.getTimestamp("Start_time").toLocalDateTime());
        slot.setEndTime(res.getTimestamp("End_time").toLocalDateTime());
        slot.setProvider(provider);
        slot.setStatus(res.getString("Status"));
        slot.setClinic(clinic);
        slot.setVersion(res.getInt("Slot_version"));

        return slot;
    }
}