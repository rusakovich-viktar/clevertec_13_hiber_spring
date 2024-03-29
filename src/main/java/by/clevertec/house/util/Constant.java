package by.clevertec.house.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constant {

    @UtilityClass
    public class ErrorMessages {

        public static final String VALIDATION_ERROR = "Validation error";
        public static final String INVALID_ARGUMENTS = "Invalid arguments provided";
        public static final String ENTITY_NOT_FOUND = "Entity not found";
        public static final String INTERNAL_SERVER_ERROR = "Internal server error";

    }

    @UtilityClass
    public class Attributes {

        public static final String AREA = "area";
        public static final String COUNTRY = "country";
        public static final String CITY = "city";
        public static final String STREET = "street";
        public static final String NUMBER = "number";
    }

}
