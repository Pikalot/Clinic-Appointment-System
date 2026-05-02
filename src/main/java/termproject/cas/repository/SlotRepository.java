package termproject.cas.repository;

import org.springframework.stereotype.Repository;
import termproject.cas.assembler.SlotAssembler;
import termproject.cas.model.Slot;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class SlotRepository {
    private final DataSource dataSource;

    public SlotRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Slot> findAll() {
        String sql = SQL.FIND_ALL_SLOTS;
        Map<Long, Slot> slotMap = new HashMap<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet res = statement.executeQuery()
        ) {
            while (res.next()) {
                Slot slot = SlotAssembler.fromResultSet(res);
                slotMap.put(slot.getId(), slot);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all slots", e);
        }
        return new ArrayList<>(slotMap.values());
    }

    public List<Slot> findByStatus(String status) {
        String sql = SQL.FIND_ALL_SLOTS_BY_STATUS;
        Map<Long, Slot> slotMap = new HashMap<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, status);
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                Slot slot = SlotAssembler.fromResultSet(res);
                slotMap.put(slot.getId(), slot);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all slots by status", e);
        }
        return new ArrayList<>(slotMap.values());
    }

    public Slot findById(Long slotId) {
        String sql = SQL.FIND_SLOT_BY_ID;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, slotId);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return SlotAssembler.fromResultSet(res);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all slots", e);
        }
        return null;
    }

    public List<Slot> findByProviderId(Long providerId) {
        String sql = SQL.FIND_SLOT_BY_PROVIDER_ID;
        Map<Long, Slot> slotMap = new HashMap<>();

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
                ) {
            statement.setLong(1, providerId);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                Slot slot = SlotAssembler.fromResultSet(res);
                slotMap.put(slot.getId(), slot);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Failed to fetch slot by provider ID", e);
        }
        return new ArrayList<>(slotMap.values());
    }

//    public Slot save(Slot slot) {
//        if (slot.getId() == null) {
//            slot.setId(nextId++);
//        }
//        return slot;
//    }

    public void insert(Slot slot) {
        String sql = """
            INSERT INTO Availability_Slots (Start_time, End_time, Provider_ID)
            VALUES (?, ?, ?)
            """;

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setTimestamp(1, Timestamp.valueOf(slot.getStartTime()));
            statement.setTimestamp(2, Timestamp.valueOf(slot.getEndTime()));
            statement.setLong(3, slot.getProvider().getId());

            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Failed to insert available slot", e);
        }
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

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setTimestamp(1, Timestamp.valueOf(slot.getStartTime()));
            statement.setTimestamp(2, Timestamp.valueOf(slot.getEndTime()));
            statement.setLong(3, slot.getProvider().getId());
            statement.setString(4, slot.getStatus());
            statement.setLong(5, slot.getId());
            statement.setInt(6, slot.getVersion());

            int rows = statement.executeUpdate();
            return rows > 0; // Return true if update successful
        }
        catch (SQLException e) {
            throw new RuntimeException("Failed to update available slot", e);
        }
    }
}
