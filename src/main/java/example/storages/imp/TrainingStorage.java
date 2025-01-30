package example.storages.imp;

import com.fasterxml.jackson.core.type.TypeReference;
import example.entities.Training;
import example.storages.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrainingStorage extends Storage<Training> {
    public TrainingStorage(@Value("${training.storage.filepath}") String filePath) {
        super(filePath, new TypeReference<Map<Long, Training>>() {});
    }
}
