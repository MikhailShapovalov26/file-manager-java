package ru.netology.diplom.repostory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.diplom.model.ActiveToken;
import ru.netology.diplom.model.User;

import java.util.Optional;

@Repository
public interface ActiveTokenRepository extends JpaRepository<ActiveToken, String> {

    Optional<ActiveToken> findByUser(User user);
}
