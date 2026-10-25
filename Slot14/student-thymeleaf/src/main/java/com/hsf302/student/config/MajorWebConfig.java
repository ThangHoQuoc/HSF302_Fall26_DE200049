package com.hsf302.student.config;

import com.hsf302.student.entity.Major;
import com.hsf302.student.service.MajorService;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.ParseException;
import java.util.Locale;

@Configuration
public class MajorWebConfig implements WebMvcConfigurer {

    private final MajorService majorService;

    public MajorWebConfig(MajorService majorService) {
        this.majorService = majorService;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new Formatter<Major>() {
            @Override
            public Major parse(String text, Locale locale) throws ParseException {
                if (text == null || text.isBlank()) {
                    return null;
                }
                try {
                    Long id = Long.valueOf(text.trim());
                    return majorService.findById(id)
                            .orElseThrow(() -> new ParseException("Unknown major id", 0));
                } catch (NumberFormatException exception) {
                    throw new ParseException("Invalid major id", 0);
                }
            }

            @Override
            public String print(Major major, Locale locale) {
                return major.getId() == null ? "" : major.getId().toString();
            }
        });
    }
}
