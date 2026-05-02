package termproject.cas.assembler;

import termproject.cas.model.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentAssembler {
    public static Appointment fromResultSet(ResultSet res) throws SQLException {
        // Assemble Slot
        Slot slot = SlotAssembler.fromResultSet(res);

        // Assemble Patient
        Patient patient = new Patient(
                res.getLong("MRN"),
                res.getString("Patient_FN"),
                res.getString("Patient_MN"),
                res.getString("Patient_LN"),
                res.getString("Patient_Sex"),
                res.getDate("Patient_DoB").toLocalDate()
        );
        patient.setVersion(res.getInt("Patient_version"));

        // Assemble Appointment
        Appointment appt = new Appointment(
                res.getLong("Appt_ID"),
                slot,
                patient,
                res.getInt("Version")
        );
        appt.setStatus(res.getString("Appt_Status"));
        appt.setServiceId(res.getInt("Service_ID"));
        appt.setService(res.getString("Service_Name"));
        appt.setFee(res.getDouble("Fee"));
        appt.setDuration(res.getInt("Duration"));

        return appt;
    }
}