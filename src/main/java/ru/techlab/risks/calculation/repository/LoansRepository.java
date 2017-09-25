package ru.techlab.risks.calculation.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import ru.techlab.risks.calculation.model.SimpleLoan;

import java.util.stream.Stream;

/**
 * Created by rb052775 on 21.08.2017.
 */

@Repository
public interface LoansRepository extends CassandraRepository<SimpleLoan> {
    @Query("SELECT * from cdc.sddubydddt1 WHERE ddact = 'N' and dddt1 >= ?0 and dddt1 <= ?1")
    Stream<SimpleLoan> findSimpleLoansByStartDateBetween(double from, double till);

    @Query("SELECT * from cdc.sddubydddt1 WHERE ddact = 'Y' and dddt1 >= ?0 and dddt1 <= ?1")
    Stream<SimpleLoan> findActiveSimpleLoansByStartDateBetween(double from, double till);

    @Query("SELECT * from cdc.sddubydddt1 WHERE ddact = 'Y'")
    Stream<SimpleLoan> findAllActiveSimpleLoans();

    @Query("SELECT * from cdc.sddubydddt1 WHERE ddact = 'Y' AND ddasv2 = '000'")
    Stream<SimpleLoan> findAllActiveAndNonPortfolioSimpleLoans();
}
