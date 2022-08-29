package ru.softdarom.qrcheck.auth.handler.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Generated
@Data
@EqualsAndHashCode(of = {"id", "name"})
@Entity
@Table(
        name = "microservices",
        indexes = {
                @Index(name = "microservice_name_uniq", columnList = "name", unique = true)
        }
)
@SQLDelete(sql = "update microservices set active = false where id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "active = true")
public class MicroserviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "microserviceSeqGenerator")
    @SequenceGenerator(name = "microserviceSeqGenerator", sequenceName = "microservice_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active", nullable = false)
    private ActiveType active = ActiveType.ENABLED;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "microservice", orphanRemoval = true)
    private Set<ApiKeyEntity> apiKeys = new HashSet<>();

    public void setApiKeys(Set<ApiKeyEntity> apiKeys) {
        if (Objects.isNull(apiKeys)) {
            this.apiKeys.clear();
        } else {
            this.apiKeys.addAll(apiKeys);
            apiKeys.forEach(it -> it.setMicroservice(this));
        }
    }

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }
}