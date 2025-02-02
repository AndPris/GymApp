package example.storages.imp;

import com.fasterxml.jackson.core.type.TypeReference;
import example.entities.Trainer;
import example.storages.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrainerStorage extends Storage<Trainer> {
    public TrainerStorage(@Value("${trainer.storage.filepath}") String filePath) {
        super(filePath, new TypeReference<Map<Long, Trainer>>() {
        });
    }
}
