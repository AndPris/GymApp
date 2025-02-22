package example.facade.handlers.utils;

import example.entities.TrainingType;
import example.exceptions.TrainingTypeNotFoundException;
import example.menu.Menu;
import example.services.TrainingTypeService;
import example.utils.input.InputHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingTypeUtil {
    private final Menu menu;
    private final TrainingTypeService trainingTypeService;
    private final InputHandler inputHandler;

    public TrainingTypeUtil(Menu menu, TrainingTypeService trainingTypeService, InputHandler inputHandler) {
        this.menu = menu;
        this.trainingTypeService = trainingTypeService;
        this.inputHandler = inputHandler;
    }

    public TrainingType getTrainingType(boolean allowEmpty) {
        List<TrainingType> trainingTypes = trainingTypeService.findAll();
        menu.displayTrainingTypeMenu(trainingTypes);

        Integer choice = inputHandler.getInputInRange(1, trainingTypes.size(), allowEmpty);
        if (choice == null) {
            return null;
        }

        return trainingTypeService.findById((long) choice);
    }
}
