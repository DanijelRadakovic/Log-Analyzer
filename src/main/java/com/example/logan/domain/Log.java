package com.example.logan.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Log implements Writable {

    private static final String DELIMITER = "|";
    private static final String FILE_FORMAT = "%s" + DELIMITER + "%s" + DELIMITER + "%s";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private LocalDateTime timestamp;
    private String service;
    private String content;

    @Override
    public String toFile() {
        return String.format(FILE_FORMAT, DATE_FORMATTER.format(timestamp), service, content);
    }

    public static Log parse(String s) throws Exception {
        StringTokenizer tokenizer = new StringTokenizer(s, DELIMITER);
        if (tokenizer.countTokens() != 3) throw new Exception("Invalid log format");
        return new Log(
                LocalDateTime.parse(tokenizer.nextToken(), DATE_FORMATTER),
                tokenizer.nextToken(),
                tokenizer.nextToken()
        );
    }
}
