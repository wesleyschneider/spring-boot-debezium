package br.com.wesleyschneider.springbootdebezium.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateToLocalDateTime {
    public static LocalDateTime convert(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.of("UTC"))
                .toLocalDateTime();
    }
}
