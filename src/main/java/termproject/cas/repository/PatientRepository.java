package termproject.cas.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import termproject.cas.assembler.PatientAssembler;
import termproject.cas.model.Patient;
import java.sql.Date;
import java.util.*;

@Repository
public class PatientRepository {
    private final JdbcTemplate jdbcTemplate;

    public PatientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    public List<Patient> findAll() {
//        return new ArrayList<>(patientMap.values());
//    }

    public Optional<Patient> findByUsernameAndPassword(String username, String password) {
        String sql = SQL.FIND_PATIENT_BY_USERNAME_PASSWORD;

        return jdbcTemplate.query(sql, res -> {
            if (res.next()) return Optional.of(PatientAssembler.fromResultSet(res));
            return Optional.empty();
        }, username, password);
    }

    public Optional<Patient> findByMRN(Long mRN) {
        String sql = SQL.FIND_PATIENT_BY_MRN;

        return jdbcTemplate.query(sql, res -> {
            if (res.next()) {
                return Optional.of(PatientAssembler.fromResultSet(res));
            }
            return Optional.empty();
        }, mRN);
    }

    public boolean insert(Patient patient) {
        String sql = """
                INSERT INTO Patients (First_name, Middle_name, Last_name, Sex, DoB, Contact_ID, Relationship)
                VALUES (?, ?, ?, ?, ?, ?, ?);
                """;

        int rows = jdbcTemplate.update(sql,
                patient.getFirstName(),
                patient.getMiddleName(),
                patient.getLastName(),
                patient.getSex(),
                Date.valueOf(patient.getDob()),
                patient.getContactId(),
                patient.getRelationship());

        return rows == 1;
    }

    public boolean update(Patient patient) {
        String sql = """
                UPDATE Patients
                SET
                    First_name = ?,
                    Middle_name = ?,
                    Last_name = ?,
                    Sex = ?,
                    DoB = ?,
                    Contact_ID = ?,
                    Relationship = ?,
                    version = version + 1
                WHERE MRN = ? AND version = ?;
                """;

        int rows = jdbcTemplate.update(sql,
                patient.getFirstName(),
                patient.getMiddleName(),
                patient.getLastName(),
                patient.getSex(),
                Date.valueOf(patient.getDob()),
                patient.getContactId(),
                patient.getRelationship(),
                patient.getMrn(),
                patient.getVersion());

        return rows == 1;
    }
}
