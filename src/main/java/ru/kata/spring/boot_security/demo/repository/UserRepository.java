package ru.kata.spring.boot_security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String userName);

    List<User> findAll(); //    List<User> listUsers();

    Optional<User> findById(Long id); //User getById(int id);

    <S extends User> S save(S entity); //void save(User user);

    void deleteById(Long id);//void delete(int id);

    @Query("select u from User u left join fetch u.roles where u.username=:username")
    User getUserByUsername(@Param("username") String username);
}
