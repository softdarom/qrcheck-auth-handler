package ru.softdarom.qrcheck.auth.handler.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import ru.softdarom.qrcheck.auth.handler.model.base.ActiveType;
import ru.softdarom.qrcheck.auth.handler.model.base.ProviderType;
import ru.softdarom.qrcheck.auth.handler.util.JsonHelper;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Generated
@Data
@EqualsAndHashCode(of = {"id", "token", "provider", "active"})
@Entity
@Table(
        name = "refresh_tokens",
        indexes = {
                @Index(name = "refresh_tokens_token_provider_uniq", columnList = "token, provider", unique = true),
        }
)
@SQLDelete(sql = "update refresh_tokens set active = false where id = ?", check = ResultCheckStyle.COUNT)
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refreshTokenSeqGenerator")
    @SequenceGenerator(name = "refreshTokenSeqGenerator", sequenceName = "refresh_token_seq", allocationSize = 1)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "issued", nullable = false)
    private LocalDateTime issued;

    @Column(name = "provider", nullable = false, updatable = false)
    private ProviderType provider;

    @Column(name = "active", nullable = false)
    private ActiveType active = ActiveType.ENABLED;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, mappedBy = "refreshToken", orphanRemoval = true)
    private Set<AccessTokenEntity> accessTokens = new HashSet<>();

    public void setAccessTokens(Set<AccessTokenEntity> accessTokens) {
        if (Objects.nonNull(accessTokens)) {
            accessTokens.forEach(it -> it.setRefreshToken(this));
            this.accessTokens.addAll(accessTokens);
        } else {
            this.accessTokens = new HashSet<>();
        }
    }

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }
}