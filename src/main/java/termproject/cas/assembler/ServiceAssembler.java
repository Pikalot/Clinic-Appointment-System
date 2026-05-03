package termproject.cas.assembler;

import termproject.cas.model.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceAssembler {
    public static Service fromResultSet(ResultSet res) throws SQLException {
        Service service = new Service(
                res.getInt("Service_ID"),
                res.getString("Name"),
                res.getDouble("Fee"),
                res.getInt("Duration")
        );
        service.setVersion(res.getInt("Version"));
        service.setActive(res.getBoolean("Is_Active"));

        return service;
    }
}
