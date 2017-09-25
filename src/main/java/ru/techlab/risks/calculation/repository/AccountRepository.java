package ru.techlab.risks.calculation.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import ru.techlab.risks.calculation.model.SimpleAccount;

import java.util.Optional;

/**
 * Created by rb052775 on 22.08.2017.
 */
@Repository
public interface AccountRepository extends CassandraRepository<SimpleAccount> {
    @Query("SELECT * from cdc.scpf WHERE scab = ?0 and scan = ?1 and scas = ?2 ALLOW FILTERING")
    Optional<SimpleAccount> findByBranchAndAccountNumberAndAccountSuffix(String branch, String accountNumber, String accountSuffix);
}
