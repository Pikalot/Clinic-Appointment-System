package termproject.cas.assembler;

import termproject.cas.model.Provider;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProviderAssembler {
    public static Provider fromResultSet(ResultSet res) throws SQLException {
        // Assemble Provider
        Provider provider = new Provider();
        provider.setId(res.getLong("User_ID"));
        provider.setFirstName(res.getString("First_name"));
        provider.setMiddleName(res.getString("Middle_name"));
        provider.setLastName(res.getString("Last_name"));
        provider.setEmail(res.getString("Email"));
        provider.setClinicId(res.getLong("Clinic_ID"));
        provider.setTitle(res.getString("Title"));
        provider.setType(res.getString("Type"));

        return provider;
    }
}