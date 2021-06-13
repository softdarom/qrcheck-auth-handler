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

@Generated
@Data
@EqualsAndHashCode(of = {"id", "token", "provider", "active"})
@Entity
@Table(
        name = "access_tokens",
        indexes = {
                @Index(name = "access_tokens_token_index", columnList = "token"),
                @Index(name = "access_tokens_token_provider_uniq", columnList = "token, provider", unique = true)
        }
)
@SQLDelete(sql = "UPDATE access_tokens SET active = false WHERE id = ?", check = ResultCheckStyle.COUNT)
public class AccessTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accessTokenSeqGenerator")
    @SequenceGenerator(name = "accessTokenSeqGenerator", sequenceName = "access_token_seq", allocationSize = 1)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "issued", nullable = false)
    private LocalDateTime issued;

    @Column(name = "expires", nullable = false)
    private LocalDateTime expires;

    @Column(name = "provider", nullable = false, updatable = false)
    private ProviderType provider;

    @Column(name = "active", nullable = false)
    private ActiveType active = ActiveType.ENABLED;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "refresh_token_id")
    private RefreshTokenEntity refreshToken;

    @PreRemove
    private void onDelete() {
        active = ActiveType.DISABLED;
    }

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }
}