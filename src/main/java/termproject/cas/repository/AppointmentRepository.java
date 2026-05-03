package termproject.cas.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import termproject.cas.assembler.AppointmentAssembler;
import termproject.cas.model.*;
import java.util.*;

@Repository
public class AppointmentRepository {
    private final JdbcTemplate jdbcTemplate;


    public AppointmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Appointment> findById(Long apptId) {
        String sql = SQL.FIND_APPOINTMENT_BY_ID;

        return jdbcTemplate.query(sql, res -> {
            if (!res.next()) return Optional.empty();
            return Optional.of(AppointmentAssembler.fromResultSet(res));
        }, apptId);
    }

    public List<Appointment> findAll() {
        String sql = SQL.FIND_ALL_APPOINTMENTS;
        Map<Long, Appointment> apptMap = new HashMap<>();

        jdbcTemplate.query(sql, res -> {
            Appointment appt = AppointmentAssembler.fromResultSet(res);
            apptMap.put(appt.getId(), appt);
        });

        return new ArrayList<>(apptMap.values());
    }

    public List<Appointment> findByMRN(Long mRN) {
        String sql = SQL.FIND_APPOINTMENTS_BY_MRN;
        Map<Long, Appointment> apptMap = new HashMap<>();

        jdbcTemplate.query(sql, res -> {
            Appointment appt = AppointmentAssembler.fromResultSet(res);
            apptMap.put(appt.getId(), appt);
        }, mRN);

        return new ArrayList<>(apptMap.values());
    }

//    public Appointment save(Appointment appt) {
//        apptMap.put(appt.getId(), appt);
//        return appt;
//    }

    public boolean insert(Appointment appt) {
        String sql = """
                INSERT INTO Appointments (Appt_Status, Slot_ID, Service_ID, MRN)
                VALUES (?, ?, ?, ?)
                """;

        int rows = jdbcTemplate.update(sql,
                appt.getStatus(),
                appt.getAvailableSlot().getId(),
                appt.getServiceId(),
                appt.getPatient().getMrn());

        return rows == 1;
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

        int rows = jdbcTemplate.update(sql,
                appt.getStatus(),
                appt.getAvailableSlot().getId(),
                appt.getServiceId(),
                appt.getPatient().getMrn(),
                appt.getId(),
                appt.getVersion());

        return rows == 1;
    }
}