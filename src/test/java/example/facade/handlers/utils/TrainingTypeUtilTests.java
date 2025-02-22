//package example.facade.handlers.utils;
//
//import example.entities.TrainingType;
//import example.menu.Menu;
//import example.services.TrainingTypeService;
//import example.utils.input.InputHandler;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TrainingTypeUtilTests {
//
//    @Mock
//    private Menu menu;
//
//    @Mock
//    private TrainingTypeService trainingTypeService;
//
//    @Mock
//    private InputHandler inputHandler;
//
//    private TrainingTypeUtil trainingTypeUtil;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        trainingTypeUtil = new TrainingTypeUtil(menu, trainingTypeService, inputHandler);
//    }
//
//    @Test
//    void getTrainingType_ShouldReturnTrainingType_WhenValidChoice() {
//        TrainingType type1 = new TrainingType("Pilates");
//        TrainingType type2 = new TrainingType("Fitness");
//        List<TrainingType> trainingTypes = Arrays.asList(type1, type2);
//
//        when(trainingTypeService.findAll()).thenReturn(trainingTypes);
//        when(inputHandler.getInputInRange(1, trainingTypes.size(), false)).thenReturn(1);
//        when(trainingTypeService.findById(1L)).thenReturn(type1);
//
//        TrainingType result = trainingTypeUtil.getTrainingType(false);
//
//        assertNotNull(result);
//        assertEquals(type1, result);
//        verify(menu, times(1)).displayTrainingTypeMenu(trainingTypes);
//        verify(inputHandler, times(1)).getInputInRange(1, trainingTypes.size(), false);
//        verify(trainingTypeService, times(1)).findById(1L);
//    }
//
//    @Test
//    void getTrainingType_ShouldReturnNull_WhenAllowEmptyAndUserChoosesEmpty() {
//        List<TrainingType> trainingTypes = Arrays.asList(new TrainingType("Pilates"));
//        when(trainingTypeService.findAll()).thenReturn(trainingTypes);
//        when(inputHandler.getInputInRange(1, trainingTypes.size(), true)).thenReturn(null);
//
//        TrainingType result = trainingTypeUtil.getTrainingType(true);
//
//        assertNull(result);
//        verify(menu, times(1)).displayTrainingTypeMenu(trainingTypes);
//        verify(inputHandler, times(1)).getInputInRange(1, trainingTypes.size(), true);
//        verify(trainingTypeService, never()).findById(anyLong());
//    }
//
//    @Test
//    void getTrainingType_ShouldThrowException_WhenInvalidTrainingType() {
//        List<TrainingType> trainingTypes = Arrays.asList(new TrainingType("Pilates"));
//        when(trainingTypeService.findAll()).thenReturn(trainingTypes);
//        when(inputHandler.getInputInRange(1, trainingTypes.size(), false)).thenReturn(2);
//        when(trainingTypeService.findById(2L)).thenReturn(Optional.empty());
//
//        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
//            trainingTypeUtil.getTrainingType(false);
//        });
//
//        assertEquals("There's no training type with id 2", thrownException.getMessage());
//        verify(menu, times(1)).displayTrainingTypeMenu(trainingTypes);
//        verify(inputHandler, times(1)).getInputInRange(1, trainingTypes.size(), false);
//        verify(trainingTypeService, times(1)).findById(2L);
//    }
//}
