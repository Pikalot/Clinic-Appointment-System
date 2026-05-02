package termproject.cas.assembler;

import termproject.cas.model.Patient;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientAssembler {
    public static Patient fromResultSet(ResultSet res) throws SQLException {
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

        return patient;
    }
}