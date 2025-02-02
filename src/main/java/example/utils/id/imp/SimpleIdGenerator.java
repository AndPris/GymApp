package example.utils.id.imp;

import example.utils.id.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SimpleIdGenerator implements IdGenerator {
    @Override
    public Long generateId(Set<Long> ids) {
        if (ids == null || ids.isEmpty())
            return 1L;

        return ids.stream().max(Long::compare).get() + 1;
    }
}
