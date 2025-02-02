package example.storages.imp;

import com.fasterxml.jackson.core.type.TypeReference;
import example.entities.Trainee;
import example.storages.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TraineeStorage extends Storage<Trainee> {
    public TraineeStorage(@Value("${trainee.storage.filepath}") String filePath) {
        super(filePath, new TypeReference<Map<Long, Trainee>>() {
        });
    }
}
