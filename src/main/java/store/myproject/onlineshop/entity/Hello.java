package store.myproject.onlineshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Hello {

    @Id
    @GeneratedValue
    private Long id;
}
