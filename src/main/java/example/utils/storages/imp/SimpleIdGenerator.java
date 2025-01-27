package example.utils.storages.imp;

import example.utils.storages.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SimpleIdGenerator implements IdGenerator {
    @Override
    public Long generateId(Set<Long> ids) {
        if(ids == null || ids.isEmpty())
            return 1L;

        return ids.stream().max(Long::compare).get() + 1;
    }
}
