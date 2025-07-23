package ru.v1nga.autoparts.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name="telegram_users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Unique
    @NotNull
    private long userId;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String fullName;
}
