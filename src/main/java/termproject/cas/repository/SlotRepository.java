package termproject.cas.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import termproject.cas.assembler.SlotAssembler;
import termproject.cas.model.Slot;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class SlotRepository {
    private final JdbcTemplate jdbcTemplate;

    public SlotRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Slot> findAll() {
        String sql = SQL.FIND_ALL_SLOTS;
        Map<Long, Slot> slotMap = new HashMap<>();

        jdbcTemplate.query(sql, res -> {
            Slot slot = SlotAssembler.fromResultSet(res);
            slotMap.put(slot.getId(), slot);
        });

        return new ArrayList<>(slotMap.values());
    }

    public List<Slot> findByStatus(String status) {
        String sql = SQL.FIND_ALL_SLOTS_BY_STATUS;
        Map<Long, Slot> slotMap = new HashMap<>();

        jdbcTemplate.query(sql, res -> {
            Slot slot = SlotAssembler.fromResultSet(res);
            slotMap.put(slot.getId(), slot);
        }, status);

        return new ArrayList<>(slotMap.values());
    }

    public Optional<Slot> findById(Long slotId) {
        String sql = SQL.FIND_SLOT_BY_ID;

        return jdbcTemplate.query(sql, res -> {
            if (!res.next()) return Optional.empty();
            return Optional.of(SlotAssembler.fromResultSet(res));
        }, slotId);
    }

    public List<Slot> findByProviderId(Long providerId) {
        String sql = SQL.FIND_SLOT_BY_PROVIDER_ID;
        Map<Long, Slot> slotMap = new HashMap<>();

        jdbcTemplate.query(sql, res -> {
            Slot slot = SlotAssembler.fromResultSet(res);
            slotMap.put(slot.getId(), slot);
        }, providerId);

        return new ArrayList<>(slotMap.values());
    }

//    public Slot save(Slot slot) {
//        if (slot.getId() == null) {
//            slot.setId(nextId++);
//        }
//        return slot;
//    }

    public boolean insert(Slot slot) {
        String sql = """
            INSERT INTO Availability_Slots (Start_time, End_time, Provider_ID)
            VALUES (?, ?, ?)
            """;

        int rows = jdbcTemplate.update(sql,
                Timestamp.valueOf(slot.getStartTime()),
                Timestamp.valueOf(slot.getEndTime()),
                slot.getProvider().getId());

         return rows == 1;
    }

    public boolean update(Slot slot) {
        String sql = """
                UPDATE Availability_Slots
                SET
                    Start_time = ?,
                    End_time = ?,
                    Provider_ID = ?,
                    Status = ?,
                    version = version + 1
                WHERE Slot_ID = ? AND version = ?
                """;

        int rows = jdbcTemplate.update(sql,
                Timestamp.valueOf(slot.getStartTime()),
                Timestamp.valueOf(slot.getEndTime()),
                slot.getProvider().getId(),
                slot.getStatus(),
                slot.getId(),
                slot.getVersion());

        return rows == 1;
    }

    public boolean hasOverlap(Long providerId, LocalDateTime start, LocalDateTime end) {
        String sql = """
            SELECT COUNT(*) FROM Availability_Slots
            WHERE Provider_ID = ?
            AND Start_time < ?
            AND End_time > ?
            AND Status != 'Cancelled'
            """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, providerId, end, start);
        return count != null && count > 0;
    }
}
