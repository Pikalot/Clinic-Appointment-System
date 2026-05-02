package termproject.cas.repository;

import org.springframework.stereotype.Repository;
import termproject.cas.model.Patient;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PatientRepository {
    private final DataSource dataSource;
    private final Map<Long, Patient> patientMap = new HashMap<>(); // to be removed
    private final MockRepository mockData; // to be removed
    private long nextId = 1L;

    public PatientRepository(DataSource dataSource, MockRepository mockData) {
        this.dataSource = dataSource;
        this.mockData = mockData;
    }

    public List<Patient> findAll() {
        return new ArrayList<>(patientMap.values());
    }

    public Patient save(Patient patient) {
        if (patient.getMrn() == null) {
            patient.setMrn(nextId++);
        }
        patientMap.put(patient.getMrn(), patient);
        return patient;
    }

    public void insert(Patient patient) {
        if (patient.getMrn() == null) {
            patient.setMrn(nextId++);
        }

        String sql = """
                INSERT INTO Patients (MRN, First_name, Middle_name, Last_name, Sex, DoB, Contact_ID, Relationship, Version)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1);
                """;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, patient.getMrn());
            statement.setString(2, patient.getFirstName());
            statement.setString(3, patient.getMiddleName());
            statement.setString(4, patient.getLastName());
            statement.setString(5, patient.getSex());
            statement.setDate(6, Date.valueOf(patient.getDob()));
            statement.setLong(7, patient.getContactId());
            statement.setString(8, patient.getRelationship());

            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Failed to insert patient", e);
        }
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

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getMiddleName());
            statement.setString(3, patient.getLastName());
            statement.setString(4, patient.getSex());
            statement.setDate(5, Date.valueOf(patient.getDob()));
            statement.setLong(6, patient.getContactId());
            statement.setString(7, patient.getRelationship());
            statement.setLong(8, patient.getMrn());
            statement.setInt(9, patient.getVersion());

            int rows = statement.executeUpdate();
            return rows > 0; // Return true if update successful
        }
        catch (SQLException e) {
            throw new RuntimeException("Failed to update patient", e);
        }
    }

    // [TO DO] replace this code with sql query
    public Patient findById(Long mrn) {
        mockData.ensureLoaded();
        return mockData.getPatientMap().get(mrn);
    }
}
