package by.clevertec.house.util;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstant {

    public static final UUID HOUSE_ONE_UUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    public static final double HOUSE_ONE_AREA = 120.5;
    public static final String HOUSE_ONE_CITY = "Minsk";
    public static final String HOUSE_ONE_COUNTRY = "Belarus";
    public static final String HOUSE_ONE_STREET = "Main Street";
    public static final String HOUSE_ONE_NUMBER = "123";
    public static final UUID HOUSE_TWO_UUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
    public static final double HOUSE_TWO_AREA = 200.0;
    public static final String HOUSE_TWO_CITY = "Moscow";
    public static final String HOUSE_TWO_COUNTRY = "Russia";
    public static final String HOUSE_TWO_STREET = "Red Square";
    public static final String HOUSE_TWO_NUMBER = "1";
    public static final UUID PERSON_ONE_UUID = UUID.fromString("550e8400-e29b-41d4-a716-446655440010");
    public static final String PERSON_ONE_NAME = "Иван";
    public static final String PERSON_ONE_SURNAME = "Иванов";
    public static final String PERSON_ONE_PASSPORT_SERIES = "MP";
    public static final String PERSON_ONE_PASSPORT_NUMBER = "1234567";
    public static final LocalDateTime PERSON_ONE_CREATE_DATE = LocalDateTime.of(2024, 2, 1, 10, 10, 10);
    public static final LocalDateTime PERSON_ONE_UPDATE_DATE = LocalDateTime.of(2024, 2, 1, 20, 20, 20);
    public static final int PAGE_NUMBER_IS_POSITIVE = 1;
    public static final int PAGE_SIZE = 15;

}
