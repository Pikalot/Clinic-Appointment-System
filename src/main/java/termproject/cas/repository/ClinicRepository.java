package termproject.cas.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import termproject.cas.assembler.ClinicAssembler;
import termproject.cas.model.Clinic;
import java.util.*;

@Repository
public class ClinicRepository {
    private final JdbcTemplate jdbcTemplate;

    public ClinicRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Clinic> findAll() {
        String sql = SQL.FIND_ALL_CLINICS;
        Map<Long, Clinic> clinicMap = new HashMap<>();

        jdbcTemplate.query(sql, res -> {
            Clinic clinic = ClinicAssembler.fromResultSet(res);
            clinicMap.put(clinic.getClinicId(), clinic);
        });

        return new ArrayList<>(clinicMap.values());
    }

    public Optional<Clinic> findById(Long clinicId) {
        String sql = SQL.FIND_CLINIC_BY_ID;

        return jdbcTemplate.query(sql, res -> {
            if (!res.next()) return Optional.empty();
            return Optional.of(ClinicAssembler.fromResultSet(res));
        }, clinicId);
    }
}
