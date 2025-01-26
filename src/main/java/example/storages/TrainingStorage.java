package example.storages;

import example.entities.Training;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrainingStorage {
    @Getter
    private Map<Long, Training> trainings;

    @Value("${training.storage.filepath}")
    private String filePath;
}
