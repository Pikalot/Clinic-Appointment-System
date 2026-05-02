package termproject.cas.assembler;

import termproject.cas.model.Clinic;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClinicAssembler {
    public static Clinic fromResultSet(ResultSet res) throws SQLException {
        // Assemble Clinic
        Clinic clinic = new Clinic(
                res.getInt("Clinic_ID"),
                res.getString("Clinic_name"),
                res.getString("Street"),
                res.getString("City"),
                res.getString("State"),
                res.getString("Zip_code")
        );
        clinic.setVersion(res.getInt("Clinic_version"));
        return clinic;
    }
}