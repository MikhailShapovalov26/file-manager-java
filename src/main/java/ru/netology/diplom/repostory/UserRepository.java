package ru.netology.diplom.repostory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.netology.diplom.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    boolean findByLoginAndPassword(String login, String password);

    User findByLogin(String admin);

    Iterable<String> searchUserById(Long id);
}

