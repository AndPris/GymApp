package example.utils.storages;


import java.util.Set;

public interface IdGenerator {
    Long generateId(Set<Long> ids);
}
