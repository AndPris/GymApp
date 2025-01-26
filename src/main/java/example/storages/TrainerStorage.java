package example.storages;

import example.entities.Trainer;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrainerStorage {
    @Getter
    private Map<Long, Trainer> trainers;

    @Value("${trainer.storage.filepath}")
    private String filePath;
}
