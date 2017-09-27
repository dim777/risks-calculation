package ru.techlab.risks.calculation.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import ru.techlab.risks.calculation.model.BaseDelay;

import java.util.stream.Stream;

/**
 * Created by rb052775 on 26.09.2017.
 */
@Repository
public interface DelaysRepository extends CassandraRepository<BaseDelay> {
    @Query("SELECT * FROM SRRU WHERE RRABD = ?0 AND RRAND = ?1 AND RRASD = ?2")
    Stream<BaseDelay> findSimpleDelayByLoan(String branch, String loanAccountNumber, String loanAccountSuffix);
}
