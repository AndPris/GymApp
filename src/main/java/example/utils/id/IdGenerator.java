package example.utils.id;


import java.util.Set;

public interface IdGenerator {
    Long generateId(Set<Long> ids);
}
