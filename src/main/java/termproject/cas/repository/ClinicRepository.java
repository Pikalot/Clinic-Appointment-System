package termproject.cas.repository;

import org.springframework.stereotype.Repository;
import termproject.cas.assembler.ClinicAssembler;
import termproject.cas.assembler.ProviderAssembler;
import termproject.cas.model.Clinic;
import termproject.cas.model.Provider;
import termproject.cas.model.Slot;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ClinicRepository {
    private final DataSource dataSource;
    private long nextId = 1L;

    public ClinicRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Clinic> findAll() {
        String sql = SQL.FIND_ALL_CLINICS;
        Map<Long, Clinic> clinicMap = new HashMap<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet res = statement.executeQuery()
        ) {
            while (res.next()) {
                Clinic clinic = ClinicAssembler.fromResultSet(res);
                clinicMap.put(clinic.getClinicId(), clinic);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all clinics", e);
        }
        return new ArrayList<>(clinicMap.values());
    }

    public Clinic findById(Long clinicId) {
        String sql = SQL.FIND_CLINIC_BY_ID;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, clinicId);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return ClinicAssembler.fromResultSet(res);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch clinic by id", e);
        }
        return null;
    }
}
