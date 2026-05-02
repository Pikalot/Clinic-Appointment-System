package termproject.cas.repository;

import org.springframework.stereotype.Repository;
import termproject.cas.assembler.ProviderAssembler;
import termproject.cas.model.Provider;
import termproject.cas.model.Slot;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProviderRepository {
    private final DataSource dataSource;
    private long nextId = 1L;

    public ProviderRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Provider> findAll() {
        String sql = SQL.FIND_ALL_PROVIDERS;
        Map<Long, Provider> providerMap = new HashMap<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet res = statement.executeQuery()
        ) {
            while (res.next()) {
                Provider provider = ProviderAssembler.fromResultSet(res);
                providerMap.put(provider.getId(), provider);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all providers", e);
        }
        return new ArrayList<>(providerMap.values());
    }

    public Provider findById(Long providerId) {
        String sql = SQL.FIND_PROVIDER_BY_ID;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, providerId);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return ProviderAssembler.fromResultSet(res);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch provider by id", e);
        }
        return null;
    }

//    [to do]
//    public void insert(Provider provider) {
//        if (provider.getId() == null) {
//            provider.setId(nextId++);
//        }
//
//        String sql = """
//                """;
//
//        try (
//                Connection connection = dataSource.getConnection();
//                PreparedStatement statement = connection.prepareStatement(sql)
//        ) {
//            statement.setLong(1, provider.getId());
//
//            statement.executeUpdate();
//        }
//        catch (SQLException e) {
//            throw new RuntimeException("Failed to insert available slot", e);
//        }
//    }

    public boolean update(Slot slot) {
        return true;
    }

}
