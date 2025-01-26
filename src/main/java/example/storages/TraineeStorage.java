package example.storages;

import example.entities.Trainee;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TraineeStorage {
    @Getter
    private Map<Long, Trainee> trainees;

    @Value("${trainee.storage.filepath}")
    private String filePath;
}
