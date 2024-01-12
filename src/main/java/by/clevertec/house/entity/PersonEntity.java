package by.clevertec.house.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "persons", uniqueConstraints = @UniqueConstraint(columnNames = {"passport_series", "passport_number"}))
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID uuid;

    private String name;
    private String surname;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Embedded
    private PassportData passportData;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "house_id", nullable = false)
    private HouseEntity house;

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "owners")
    private Set<HouseEntity> ownedHouses;

    @PrePersist
    public void prePersist() {
        if (this.createDate == null) {
            this.createDate = LocalDateTime.now();
            this.updateDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updateDate = LocalDateTime.now();
    }
}
