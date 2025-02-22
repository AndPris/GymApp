package example.services.imp;

import example.entities.TrainingType;
import example.exceptions.TrainingTypeNotFoundException;
import example.repositories.TrainingTypeRepository;
import example.services.TrainingTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingTypeServiceImp implements TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingTypeServiceImp(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    public List<TrainingType> findAll() {
        return trainingTypeRepository.findAll();
    }

    @Override
    public TrainingType findById(Long id) {
        return trainingTypeRepository.findById(id)
                .orElseThrow(() -> new TrainingTypeNotFoundException("There's no training type with such id: " + id));
    }
}
