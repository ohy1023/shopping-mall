package store.myproject.onlineshop.domain.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import store.myproject.onlineshop.domain.customer.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByEmailAndTel(String email, String tel);

    Optional<Customer> findByNickName(String nickName);

}
