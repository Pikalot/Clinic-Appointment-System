package termproject.cas.repository;

import org.springframework.stereotype.Repository;
import termproject.cas.assembler.AppointmentAssembler;
import termproject.cas.model.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class AppointmentRepository {
    private final DataSource dataSource;
    private final MockRepository mockData;
    private long nextId = 2L;

    public AppointmentRepository(DataSource dataSource, MockRepository mockData) {
        this.dataSource = dataSource;
        this.mockData = mockData;
    }

    public List<Appointment> findById(Long apptId) {
        String sql = SQL.FIND_APPOINTMENT_BY_ID;
        Map<Long, Appointment> apptMap = new HashMap<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, apptId);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                Appointment appt = AppointmentAssembler.fromResultSet(res);
                apptMap.put(appt.getId(), appt);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch appointment by ID", e);
        }
        return new ArrayList<>(apptMap.values());
    }

    public List<Appointment> findAll() {
        String sql = SQL.FIND_ALL_APPOINTMENTS;
        Map<Long, Appointment> apptMap = new HashMap<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet res = statement.executeQuery()
        ) {
            while (res.next()) {
                Appointment appt = AppointmentAssembler.fromResultSet(res);
                apptMap.put(appt.getId(), appt);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all appointments", e);
        }
        return new ArrayList<>(apptMap.values());
    }

    public List<Appointment> findByMRN(Long mRN) {
        String sql = SQL.FIND_APPOINTMENTS_BY_MRN;
        Map<Long, Appointment> apptMap = new HashMap<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, mRN);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                Appointment appt = AppointmentAssembler.fromResultSet(res);
                apptMap.put(appt.getId(), appt);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch appointment by MRN", e);
        }
        return new ArrayList<>(apptMap.values());
    }

    public Appointment save(Appointment appt) {
        mockData.ensureLoaded();

        if (appt.getId() == null) {
            appt.setId(nextId++);
        }
        mockData.getApptMap().put(appt.getId(), appt);
        return appt;
    }

    public void insert(Appointment appt) {
        mockData.ensureLoaded();

        if (appt.getId() == null) {
            appt.setId(nextId++);
        }

        mockData.getApptMap().put(appt.getId(), appt); // keep memory copy for testing/submission

        String sql = """
                INSERT INTO Appointments (Appt_ID, Appt_Status, Slot_ID, Service_ID, MRN, Version)
                VALUES (?, ?, ?, ?, ?, 1)
                """;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, appt.getId());
            statement.setString(2, appt.getStatus());
            statement.setLong(3, appt.getAvailableSlot().getId());
            statement.setInt(4, appt.getServiceId());
            statement.setLong(5, appt.getPatient().getMrn());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert appointment", e);
        }
    }

    public boolean update(Appointment appt) {
        String sql = """
                UPDATE Appointments
                SET
                    Appt_Status = ?,
                    Slot_ID = ?,
                    Service_ID = ?,
                    MRN = ?,
                    Version = Version + 1
                WHERE Appt_ID = ? AND Version = ?;
                """;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, appt.getStatus());
            statement.setLong(2, appt.getAvailableSlot().getId());
            statement.setInt(3, appt.getServiceId());
            statement.setLong(4, appt.getPatient().getMrn());
            statement.setLong(5, appt.getId());
            statement.setInt(6, appt.getVersion());

            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update appointment", e);
        }
    }
}