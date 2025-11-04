package ru.netology.diplom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


@Entity
@Table(name = "storage",
 uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "filename"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "document_id")
    private Integer documentId;

    @Column(name = "filename")
    private String fileName;


    @Column(name = "upload_dir")
    private String uploadDir;

    @Column(name = "size_to_byte")
    private Integer sizeToByte;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    @NotFound
    private User user;
}
