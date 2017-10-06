package ru.techlab.risks.calculation.component;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rb052775 on 05.10.2017.
 */
@Service
public class AppCacheImpl implements AppCache{
    private Map<String, Object> cache = new HashMap<>();

    @Override
    public void setVar(String name, Object var) {
        cache.put(name, var);
    }

    @Override
    public Object getVar(String name) {
        return cache.get(name);
    }
}
