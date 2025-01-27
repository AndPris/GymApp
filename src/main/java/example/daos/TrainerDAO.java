package example.daos;

import example.entities.Trainer;
import example.storages.TrainerStorage;
import example.utils.storages.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class TrainerDAO {
    private TrainerStorage trainerStorage;
    private IdGenerator idGenerator;
    private Map<Long, Trainer> trainerMap;

    @Autowired
    public void setTrainerStorage(final TrainerStorage trainerStorage) {
        this.trainerStorage = trainerStorage;
        this.trainerMap = trainerStorage.getTrainers();
    }

    @Autowired
    public void setIdGenerator(final IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }


    public Trainer save(Trainer trainer) {
        if(trainer == null)
            throw new IllegalArgumentException("Can not perform save: trainer is null");

        Long id;

        if(trainer.getId() == null) {
            id = idGenerator.generateId(trainerMap.keySet());
            trainer.setId(id);
        } else {
            id = trainer.getId();
        }

        trainerMap.put(id, trainer);

        return trainer;
    }

    public Iterable<Trainer> findAll() {
        return trainerMap.values();
    }

    public Optional<Trainer> findById(Long id) {
        if(id == null)
            throw new IllegalArgumentException("Can not perform findById: id is null");

        return trainerMap.containsKey(id) ? Optional.of(trainerMap.get(id)) : Optional.empty();
    }
}
