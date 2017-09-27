package ru.techlab.risks.calculation.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import ru.techlab.risks.calculation.model.BaseAccount;

import java.util.Optional;

/**
 * Created by rb052775 on 22.08.2017.
 */
@Repository
public interface AccountRepository extends CassandraRepository<BaseAccount> {
    @Query("SELECT * FROM SCPF WHERE SCAB = ?0 AND SCAN = ?1 AND SCAS = ?2")
    Optional<BaseAccount> findByBranchAndAccountNumberAndAccountSuffix(String branch, String accountNumber, String accountSuffix);
}
