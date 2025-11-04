package ru.netology.diplom.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;

@Entity
@Table(name = "active_tokens",
        uniqueConstraints = @UniqueConstraint(columnNames = {"token", "user_id"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActiveToken {

    @Id
    @Column(name= "token")
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotFound
    private User user;


}
