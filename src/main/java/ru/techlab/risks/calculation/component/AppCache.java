package ru.techlab.risks.calculation.component;

import ru.techlab.risks.calculation.model.rest.BaseConfig;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategory;
import ru.techlab.risks.calculation.model.rest.LoanQualityCategoryMatrix;
import ru.techlab.risks.calculation.model.rest.LoanServCoeff;

import java.util.List;

/**
 * Created by rb052775 on 06.10.2017.
 */
public interface AppCache {
    void setVar(String name, Object var);
    Object getVar(String name);
}
