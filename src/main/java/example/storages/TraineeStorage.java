package example.storages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.entities.Trainee;
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
public class TraineeStorage {
    @Getter
    private Map<Long, Trainee> trainees;

    @Value("${trainee.storage.filepath}")
    private String filePath;

    @PostConstruct
    public void init() throws IOException {
        File file = new File(filePath);
        if(!file.exists()) {
            trainees = new HashMap<>();
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        trainees = mapper.readValue(file, new TypeReference<Map<Long, Trainee>>() {});
    }

    @PreDestroy
    public void destroy() throws IOException {
        File file = new File(filePath);
        file.createNewFile();

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, trainees);
    }
}
