package termproject.cas.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import termproject.cas.assembler.ProviderAssembler;
import termproject.cas.model.Provider;
import java.util.*;

@Repository
public class ProviderRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProviderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Provider> findAll() {
        String sql = SQL.FIND_ALL_PROVIDERS;
        Map<Long, Provider> providerMap = new HashMap<>();

        jdbcTemplate.query(sql, res -> {
            Provider provider = ProviderAssembler.fromResultSet(res);
            providerMap.put(provider.getId(), provider);
        });

        return new ArrayList<>(providerMap.values());
    }

    public Optional<Provider> findById(Long providerId) {
        String sql = SQL.FIND_PROVIDER_BY_ID;

        return jdbcTemplate.query(sql, res -> {
            if (!res.next()) return Optional.empty();
            return Optional.of(ProviderAssembler.fromResultSet(res));
        }, providerId);
    }
}
