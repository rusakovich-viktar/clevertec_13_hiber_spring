package by.clevertec.house.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Embeddable
public class PassportData {
    private String passportSeries;
    private String passportNumber;
}
