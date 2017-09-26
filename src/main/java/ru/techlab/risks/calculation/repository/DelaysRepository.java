package ru.techlab.risks.calculation.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import ru.techlab.risks.calculation.model.SimpleDelay;
import ru.techlab.risks.calculation.model.SimpleLoan;

import java.util.stream.Stream;

/**
 * Created by rb052775 on 26.09.2017.
 */
@Repository
public interface DelaysRepository extends CassandraRepository<SimpleDelay> {
    @Query("SELECT * from srru WHERE RRABD||RRAND||RRASD = ?0||?1||?2")
    Stream<SimpleDelay> findSimpleDelayByLoan(String branch, String loanAccountNumber, String loanAccountSuffix);
}
