package ru.v1nga.autoparts.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name="cart_items",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "part_id"})
)
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
    private PartEntity part;

    @Min(1)
    private int quantity;
}
