package ru.softdarom.qrcheck.auth.handler.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import ru.softdarom.qrcheck.auth.handler.model.base.RoleType;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Generated
@Data
@EqualsAndHashCode(of = "name")
@Entity
@Table(
        name = "roles",
        indexes = @Index(name = "roles_name_uniq", columnList = "name", unique = true)
)
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roleSeqGenerator")
    @SequenceGenerator(name = "roleSeqGenerator", sequenceName = "role_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false, updatable = false)
    private RoleType name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users = new HashSet<>();

    public void addUser(UserEntity user) {
        if (Objects.nonNull(user)) {
            this.users.add(user);
        }
    }

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }

}