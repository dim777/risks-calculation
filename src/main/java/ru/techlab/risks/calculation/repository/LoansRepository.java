package ru.techlab.risks.calculation.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import ru.techlab.risks.calculation.model.BaseLoan;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by rb052775 on 21.08.2017.
 */
@Repository
public interface LoansRepository extends CassandraRepository<BaseLoan> {
//    List<BaseLoan> findAll();

    @Query("SELECT * FROM SDDU WHERE DDACT = 'N' AND DDDT1 >= ?0 AND DDDT1 <= ?1")
    Stream<BaseLoan> findSimpleLoansByStartDateBetween(double from, double till);

    @Query("SELECT * FROM SDDU WHERE DDACT = 'Y' AND DDDT1 >= ?0 AND DDDT1 <= ?1")
    Stream<BaseLoan> findActiveSimpleLoansByStartDateBetween(double from, double till);

    @Query("SELECT * FROM SDDU WHERE DDACT = 'Y'")
    Stream<BaseLoan> findAllActiveSimpleLoans();

    @Query("SELECT * FROM SDDU WHERE DDACT = 'Y' AND DDASV2 = '000'")
    List<BaseLoan> findAllActiveAndNonPortfolioBaseLoans();

    @Query("SELECT * FROM SDDU WHERE DDACT = 'Y' AND DDASV2 = '000' AND DDABD = ?0 AND DDAND = ?1 AND DDASD = ?2")
    Optional<BaseLoan> findActiveAndNonPortfolioSimpleLoansByLoanId(String branch, String loanAccountNumber, String loanAccountSuffix);
}
