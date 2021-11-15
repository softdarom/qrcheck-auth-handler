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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Generated
@Data
@EqualsAndHashCode(of = {"id", "externalUserId", "active"})
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "users_external_user_id_uindex", columnList = "external_user_id", unique = true)
        }
)
@SQLDelete(sql = "UPDATE users SET active = false, updated = current_timestamp WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "active = true")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeqGenerator")
    @SequenceGenerator(name = "userSeqGenerator", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

    @Column(name = "external_user_id", nullable = false)
    private Long externalUserId;

    @Column(name = "created", updatable = false)
    private LocalDateTime created;

    @Column(name = "updated")
    private LocalDateTime updated;

    @Column(name = "active", nullable = false)
    private ActiveType active = ActiveType.ENABLED;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private Set<RefreshTokenEntity> refreshTokens = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private Set<UserTokenInfoEntity> tokenInfo = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"}))
    private Set<RoleEntity> roles = new HashSet<>();

    public void setRefreshTokens(Set<RefreshTokenEntity> refreshTokens) {
        if (Objects.nonNull(refreshTokens)) {
            refreshTokens.forEach(it -> it.setUser(this));
            this.refreshTokens.addAll(refreshTokens);
        } else {
            this.refreshTokens = new HashSet<>();
        }
    }

    public void setTokenInfo(Set<UserTokenInfoEntity> tokenInfo) {
        if (Objects.nonNull(tokenInfo)) {
            tokenInfo.forEach(it -> it.setUser(this));
            this.tokenInfo.addAll(tokenInfo);
        } else {
            this.tokenInfo = new HashSet<>();
        }
    }

    public void setRoles(Set<RoleEntity> roles) {
        if (Objects.nonNull(roles)) {
            this.roles.clear();
            roles.forEach(it -> it.addUser(this));
            this.roles.addAll(roles);
        } else {
            this.tokenInfo = new HashSet<>();
        }
    }

    @PrePersist
    private void onCreate() {
        var current = LocalDateTime.now();
        created = current;
        updated = current;
    }

    @PreUpdate
    private void onUpdate() {
        updated = LocalDateTime.now();
    }

    @PreRemove
    private void onDelete() {
        updated = LocalDateTime.now();
        active = ActiveType.DISABLED;
    }

    @Override
    public String toString() {
        return JsonHelper.asJson(this);
    }
}