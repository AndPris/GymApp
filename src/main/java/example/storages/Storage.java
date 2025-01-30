package example.storages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Storage<T> {
    private static final Logger logger = LogManager.getLogger(Storage.class);
    private String filePath;
    private final TypeReference<Map<Long, T>> typeReference;

    @Getter
    protected Map<Long, T> data;

    protected Storage(String filePath, TypeReference<Map<Long, T>> typeReference) {
        this.filePath = filePath;
        this.typeReference = typeReference;
    }


    @PostConstruct
    public void init() throws IOException {
        File file = new File(filePath);
        if(!file.exists()) {
            data = new HashMap<>();
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        data = new HashMap<>(mapper.readValue(file, typeReference));
        logger.info("Read data from {}", filePath);
    }

    @PreDestroy
    public void destroy() throws IOException {
        File file = new File(filePath);
        file.createNewFile();

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, data);
        logger.info("Save data to {}", filePath);
    }
}
