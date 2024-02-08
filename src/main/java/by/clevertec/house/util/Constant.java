package by.clevertec.house.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {

    @UtilityClass
    public class Attributes {

        public static final String AREA = "area";
        public static final String COUNTRY = "country";
        public static final String CITY = "city";
        public static final String STREET = "street";
        public static final String NUMBER = "number";
        //Чтобы пользователь начинал с первой страницы, а не с нулевой
        public static final int VALUE_ONE_TO_SIMPLIFY_UI = 1;
        public static final String NAME = "name";
        public static final String SURNAME = "surname";
        public static final String ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss";
        public static final String SEX = "sex";
        public static final String PASSPORT_DATA = "passportData";
        public static final String HOUSE_UUID = "houseUuid";
    }

}
