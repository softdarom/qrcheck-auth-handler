package ru.softdarom.qrcheck.auth.handler.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import javax.persistence.*;

@Generated
@Data
@EqualsAndHashCode(of = {"id", "provider", "sub"})
@Entity
@Table(
        name = "user_token_info",
        indexes = {
                @Index(name = "user_token_info_user_id_provider_uniq", columnList = "user_id, provider", unique = true)
        }
)
public class UserTokenInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userTokenInfoSeqGenerator")
    @SequenceGenerator(name = "userTokenInfoSeqGenerator", sequenceName = "user_token_info_seq", allocationSize = 1)
    private Long id;

    @Column(name = "sub", nullable = false, updatable = false)
    private String sub;

    @Column(name = "provider", nullable = false, updatable = false)
    private ProviderType provider;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }
}