package ru.softdarom.qrcheck.auth.handler.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.ApiKeyType;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import javax.persistence.*;
import java.util.UUID;

@Generated
@Data
@EqualsAndHashCode(of = {"id", "key", "type"})
@Entity
@Table(name = "api_keys")
@SQLDelete(sql = "UPDATE api_keys SET active = false WHERE id = ?", check = ResultCheckStyle.COUNT)
public class ApiKeyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "apiKeySeqGenerator")
    @SequenceGenerator(name = "apiKeySeqGenerator", sequenceName = "api_key_seq", allocationSize = 1)
    private Long id;

    @Column(name = "key", nullable = false, updatable = false)
    private UUID key;

    @Column(name = "type", nullable = false, updatable = false)
    private ApiKeyType type;

    @Column(name = "active", nullable = false)
    private ActiveType active = ActiveType.ENABLED;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "microservice_id")
    private MicroserviceEntity microservice;

    @PreRemove
    private void onDelete() {
        active = ActiveType.DISABLED;
    }

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }
}