package ru.techlab.risks.calculation.services.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.techlab.risks.calculation.model.rest.BaseConfig;

/**
 * Created by rb052775 on 05.10.2017.
 */
@Service
public class ConfigServiceImpl implements ConfigService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.configServer}")
    private String configServer;
    @Value("${app.path.config}")
    private String path;

    @Override
    public BaseConfig getBaseConfig(String configServer){
        BaseConfig baseConfig = restTemplate
                .getForObject(configServer + path, BaseConfig.class);
        return baseConfig;
    }
}
