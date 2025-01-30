import example.entities.Trainee;
import example.storages.Storage;
import example.storages.imp.TraineeStorage;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StorageTests {
    private Storage<Trainee> storage;
    private Map<Long, Trainee> map;
    private final String filePath = "TestStorage.json";
    private Trainee testTrainee;

    @BeforeEach
    public void init() throws IOException, ParseException {
        createInitialFile();
        createTestTrainee();
        createStorage();
    }

    private void createInitialFile() throws IOException {
        File file = new File(filePath);
        if(file.exists())
            file.delete();

        file.createNewFile();

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("{\"1\":{\"id\":1,\"firstName\":\"new first\",\"lastName\":\"new last\",\"username\":\"test.test\",\"password\":\"%ym)r&oP}.\",\"active\":true,\"address\":\"test addr\",\"dateOfBirth\":1607724000000}}");
        }
    }

    private void createTestTrainee() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        testTrainee = new Trainee();
        testTrainee.setId(2L);
        testTrainee.setFirstName("test first name");
        testTrainee.setLastName("test last name");
        testTrainee.setActive(true);
        testTrainee.setPassword("test password");
        testTrainee.setUsername("test username");
        testTrainee.setDateOfBirth(format.parse("10-10-1010"));
    }

    private void createStorage() throws IOException {
        storage = new TraineeStorage(filePath);
        storage.init();
        map = storage.getData();
    }

    @Test
    public void initFileExists() {
        assertTrue(map.containsKey(1L));
    }

    @Test
    public void initFileDoesntExist() throws IOException {
        storage = new TraineeStorage("no_file.json");
        storage.init();
        Map<Long, Trainee> data = storage.getData();
        assertInstanceOf(HashMap.class, data);
        assertTrue(data.isEmpty());
    }

    @Test
    public void putTest() {
        storage.put(2L, testTrainee);
        assertEquals(testTrainee, map.get(2L));
    }

    @Test
    public void keySetTest() {
        Set<Long> expected = new HashSet<>();
        expected.add(1L);
        assertEquals(expected, storage.keySet());
        assertEquals(map.keySet(), storage.keySet());
    }

    @Test
    public void getTest() {
        Trainee actual = storage.get(1L);

        assertEquals(1L, actual.getId());
        assertEquals("new first", actual.getFirstName());
        assertEquals("new last", actual.getLastName());
        assertEquals("test.test", actual.getUsername());
        assertEquals("%ym)r&oP}.", actual.getPassword());
        assertTrue(actual.isActive());
        assertEquals("test addr", actual.getAddress());
        assertEquals(1607724000000L, actual.getDateOfBirth().getTime());
    }

    @Test
    public void containsKeyTest() {
        assertTrue(storage.containsKey(1L));
        assertFalse(storage.containsKey(20L));
    }

    @Test
    public void valuesTest() {
        assertEquals(map.values(), storage.values());
        assertEquals(1, storage.values().size());

        Trainee actual = (Trainee) storage.values().toArray()[0];
        assertEquals(1L, actual.getId());
        assertEquals("new first", actual.getFirstName());
        assertEquals("new last", actual.getLastName());
        assertEquals("test.test", actual.getUsername());
        assertEquals("%ym)r&oP}.", actual.getPassword());
        assertTrue(actual.isActive());
        assertEquals("test addr", actual.getAddress());
        assertEquals(1607724000000L, actual.getDateOfBirth().getTime());
    }

    @Test
    public void removeTest() {
        storage.remove(1L);

        assertFalse(storage.containsKey(1L));
        assertFalse(map.containsKey(1L));
        assertTrue(storage.values().isEmpty());
    }

    @Test
    public void destroyTest() throws IOException {
        storage.put(2L, testTrainee);
        storage.destroy();

        String expected = "{\"1\":{\"id\":1,\"firstName\":\"new first\",\"lastName\":\"new last\",\"username\":\"test.test\",\"password\":\"%ym)r&oP}.\",\"active\":true,\"address\":\"test addr\",\"dateOfBirth\":1607724000000},\"2\":{\"id\":2,\"firstName\":\"test first name\",\"lastName\":\"test last name\",\"username\":\"test username\",\"password\":\"test password\",\"active\":true,\"address\":null,\"dateOfBirth\":-30269815200000}}";
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String actual = br.readLine();
            assertEquals(expected, actual);
        }
    }
}
