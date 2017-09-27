package ru.techlab.risks.calculation.unit;

import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.xegex.risks.libs.ex.convertion.ConvertionEx;
import ru.xegex.risks.libs.utils.DateTimeUtils;

/**
 * Created by rb052775 on 22.08.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class BasicTests {
    @Test
    public void whenConvertAs400DateTimeToJodaTimeIn20Century() throws ConvertionEx {
        LocalDateTime localDateTimeFromAs400 = DateTimeUtils.convertFromAs400Format(1170803);
        LocalDateTime localDateTimeExpected = LocalDateTime.parse("2017-08-03");
        Assert.assertEquals(localDateTimeExpected, localDateTimeFromAs400);
    }
    @Test
    public void whenConvertAs400DateTimeToJodaTimeIn19Century() throws ConvertionEx {
        LocalDateTime localDateTimeFromAs400 = DateTimeUtils.convertFromAs400Format(840921);
        LocalDateTime localDateTimeExpected = LocalDateTime.parse("1984-09-21");
        Assert.assertEquals(localDateTimeExpected, localDateTimeFromAs400);
    }
    @Test
    public void whenConvertAs400DateTimeToJodaTimeForInfiniteDate() throws ConvertionEx {
        LocalDateTime localDateTimeFromAs400 = DateTimeUtils.convertFromAs400Format(9999999);
        LocalDateTime localDateTimeExpected = LocalDateTime.parse("2999-12-30");
        Assert.assertEquals(localDateTimeExpected, localDateTimeFromAs400);
    }
    @Test(expected = ConvertionEx.class)
    public void whenConvertAs400DateTimeToJodaTimeInWrongFormat() throws ConvertionEx {
        LocalDateTime localDateTimeFromAs400 = DateTimeUtils.convertFromAs400Format(11840921);
        LocalDateTime localDateTimeExpected = LocalDateTime.parse("1984-09-21");
        Assert.assertEquals(localDateTimeExpected, localDateTimeFromAs400);
    }

}
