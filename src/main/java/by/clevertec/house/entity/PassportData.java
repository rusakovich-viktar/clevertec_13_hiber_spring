package by.clevertec.house.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class PassportData {
    @Column(name = "passport_series", nullable = false)
    private String passportSeries;
    @Column(name = "passport_number", nullable = false)
    private String passportNumber;
}
