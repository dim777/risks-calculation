package ru.techlab.risks.calculation.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import ru.techlab.risks.calculation.model.BaseAccount;
import ru.techlab.risks.calculation.model.BaseCustomer;

import java.util.List;
import java.util.Optional;

/**
 * Created by rb052775 on 30.09.2017.
 */
@Repository
public interface CustomerRepository extends CassandraRepository<BaseCustomer> {
    Optional<BaseCustomer> findById(String id);
}
