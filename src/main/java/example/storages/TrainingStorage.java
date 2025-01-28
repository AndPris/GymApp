package example.storages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.entities.Training;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class TrainingStorage {
    @Getter
    private Map<Long, Training> trainings;

    @Value("${training.storage.filepath}")
    private String filePath;

    @PostConstruct
    public void init() throws IOException {
        File file = new File(filePath);
        if(!file.exists()) {
            trainings = new HashMap<>();
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        trainings = mapper.readValue(file, new TypeReference<Map<Long, Training>>() {});
    }

    @PreDestroy
    public void destroy() throws IOException {
        File file = new File(filePath);
        file.createNewFile();

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, trainings);
    }
}
