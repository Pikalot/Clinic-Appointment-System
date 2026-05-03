package termproject.cas.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import termproject.cas.assembler.ServiceAssembler;
import termproject.cas.model.Service;
import java.sql.*;
import java.util.*;

@Repository
public class ServiceRepository {
    private final JdbcTemplate jdbcTemplate;

    public ServiceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Service> findAll() {
        String sql = SQL.FIND_ALL_SERVICES;
        Map<Integer, Service> serviceMap = new HashMap<>();

        jdbcTemplate.query(sql, res -> {
            Service service = ServiceAssembler.fromResultSet(res);
            serviceMap.put(service.getServiceId(), service);
        });

        return new ArrayList<>(serviceMap.values());
    }

    public List<Service> findByStatus(boolean active) {
        String sql = SQL.FIND_ALL_SERVICES_BY_STATUS;
        Map<Integer, Service> serviceMap = new HashMap<>();

        jdbcTemplate.query(sql, res -> {
            Service service = ServiceAssembler.fromResultSet(res);
            serviceMap.put(service.getServiceId(), service);
        }, active);

        return new ArrayList<>(serviceMap.values());
    }

    public Optional<Service> findById(int id) {
        String sql = SQL.FIND_SERVICE_BY_ID;

        return jdbcTemplate.query(sql, res -> {
            if (!res.next()) return Optional.empty();
            return Optional.of(ServiceAssembler.fromResultSet(res));
        }, id);
    }

    public boolean update(Service service) {
        String sql = """
                UPDATE Services
                SET
                    Name = ?,
                    Fee = ?,
                    Duration = ?,
                    Is_Active = ?,
                    version = version + 1
                WHERE Service_ID = ? AND version = ?
                """;

        int rows = jdbcTemplate.update(sql,
                service.getName(),
                service.getFee(),
                service.getDuration(),
                service.isActive(),
                service.getServiceId(),
                service.getVersion());

        return rows > 0; // Return true if update successful
    }

    public boolean insert(Service service) {
        String sql = """
            INSERT INTO Services (Name, Fee, Duration)
            VALUES (?, ?, ?)
            """;

        int rows = jdbcTemplate.update(sql,
                service.getName(),
                service.getFee(),
                service.getDuration());

        return rows > 0;
    }
}
