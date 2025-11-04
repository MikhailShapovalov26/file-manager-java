package ru.netology.diplom.repostory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.netology.diplom.dto.ListAllFile;
import ru.netology.diplom.model.Storage;
import ru.netology.diplom.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageRepository extends JpaRepository<Storage, String> {

    boolean findByUser_Id(Long userId);

    Optional<Storage> findByUserAndFileName(User user, String fileName);

    User user(User user);

    @Query("SELECT new ru.netology.diplom.dto.ListAllFile(s.fileName, s.sizeToByte) " +
            "FROM Storage s WHERE s.user = :user")
    List<ListAllFile> findAllByUser(User user);

}
