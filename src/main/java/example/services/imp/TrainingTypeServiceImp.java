package example.services.imp;

import example.entities.TrainingType;
import example.repositories.TrainingTypeRepository;
import example.services.TrainingTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingTypeServiceImp implements TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingTypeServiceImp(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    public List<TrainingType> findAll() {
        return  trainingTypeRepository.findAll();
    }

    @Override
    public Optional<TrainingType> findById(Long id) {
        return trainingTypeRepository.findById(id);
    }
}
