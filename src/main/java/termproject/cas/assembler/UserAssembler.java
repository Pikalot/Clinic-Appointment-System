package termproject.cas.assembler;

import termproject.cas.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAssembler {
    public static User fromResultSet(ResultSet res) throws SQLException {
        // Assemble User
        User user = new User();
        user.setId(res.getLong("User_ID"));
        user.setFirstName(res.getString("First_name"));
        user.setMiddleName(res.getString("Middle_name"));
        user.setLastName(res.getString("Last_name"));
        user.setEmail(res.getString("Email"));
        user.setClinicId(res.getLong("Clinic_ID"));
        user.setVersion(res.getInt("Version"));

        return user;
    }
}