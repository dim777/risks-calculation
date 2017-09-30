package ru.techlab.risks.calculation.services.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.techlab.risks.calculation.model.BaseCustomer;
import ru.techlab.risks.calculation.repository.CustomerRepository;
import ru.xegex.risks.libs.ex.customer.CustomerNotFoundEx;

import java.util.Optional;

/**
 * Created by rb052775 on 30.09.2017.
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public BaseCustomer getCustomer(String id) throws CustomerNotFoundEx {
        Optional<BaseCustomer> customer = customerRepository.findById(id);
        return customer.orElseThrow(() -> new CustomerNotFoundEx("No customer found"));
    }
}
