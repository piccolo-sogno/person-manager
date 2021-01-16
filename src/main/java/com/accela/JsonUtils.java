package com.accela;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JsonUtils {
    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    public List<Person> parseFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Person> persons = objectMapper.readValue(new File(filePath), new TypeReference<List<Person>>(){});
            return persons;
        } catch(IOException ex) {
            logger.warn("File parsing problem: " +  ex.toString());
        }
        return Collections.emptyList();
    }
}
