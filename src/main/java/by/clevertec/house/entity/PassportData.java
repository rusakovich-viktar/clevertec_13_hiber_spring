package by.clevertec.house.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class PassportData {
    private String passportSeries;
    private String passportNumber;
}
