package com.es.core.model.phone.dao;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.exception.InvalidIdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ContextConfiguration(locations = {"/context/applicationContext-coreTest.xml"})
@ExtendWith(SpringExtension.class)
public class JdbcPhoneDaoIntTest {

    private static final String SELECT_FROM_PHONES_WHERE_ID = "select * from phones where id = ?";
    private static final String LOAD_COLLORS_MANUALLY = "select * from colors " +
            "join phone2color on colors.id = phone2color.colorId " +
            "where phone2color.phoneId = ?";
    @Autowired
    private JdbcPhoneDao jdbcPhoneDao;

    private static Phone firstPhone;
    private static Phone secondPhone;
    private static Phone thirdPhone;
    private static Phone saveTestPhone;
    private static Phone updateTestPhone;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    public static void setTestPhone() {
        Color blackColor = new Color(0L, "Black");
        Color whiteColor = new Color(1L, "White");

        firstPhone = new Phone(1000L, "test", "test", null, BigDecimal.valueOf(10.1),
                482, BigDecimal.valueOf(276.0), BigDecimal.valueOf(167.0), BigDecimal.valueOf(12.6),
                null, "test", "test", Set.of(blackColor), "test", 149,
                null, null, BigDecimal.valueOf(1.3), null,
                BigDecimal.valueOf(8.0), null, null, null,
                "test", "test", "test", "test");

        secondPhone = new Phone(1001L, "test", "test1", null, BigDecimal.valueOf(10.1),
                482, BigDecimal.valueOf(250.0), BigDecimal.valueOf(174.0), BigDecimal.valueOf(10.0),
                null, "test", "test", Set.of(whiteColor), "test", 149,
                "test", BigDecimal.valueOf(5.0), BigDecimal.valueOf(0.3), BigDecimal.valueOf(1.0),
                BigDecimal.valueOf(8.0), 6500, null, null,
                "test", "test", "test", "test");

        thirdPhone = new Phone(1002L, "test", "test2", null, BigDecimal.valueOf(10.1),
                482, null, null, null,
                null, "test", "test", Set.of(blackColor), "test", 118,
                "test", null, BigDecimal.valueOf(0.3), null,
                BigDecimal.valueOf(8.0), null, null, null,
                "test", null, "test", "test");

        saveTestPhone = new Phone(1006L, "test", "test7", null, BigDecimal.valueOf(10.1),
                482, BigDecimal.valueOf(276.0), BigDecimal.valueOf(167.0), BigDecimal.valueOf(12.6),
                null, "test", "test", Set.of(whiteColor, blackColor), "test", 149,
                null, null, BigDecimal.valueOf(1.3), null,
                BigDecimal.valueOf(8.0), null, null, null,
                "test", "test", "test", "test");

        updateTestPhone = new Phone(1004L, "test", "test4", null, BigDecimal.valueOf(10.1),
                482, BigDecimal.valueOf(265.4), BigDecimal.valueOf(181.0), BigDecimal.valueOf(13.4),
                null, "test", "test", Set.of(whiteColor, blackColor), "test", 149,
                "test", BigDecimal.valueOf(5.0), BigDecimal.valueOf(2.0), BigDecimal.valueOf(1.0),
                BigDecimal.valueOf(16.0), 6000, null, null,
                "test", "test", "test", "test");
    }

    @Test
    public void testFindAllData() {
        List<Phone> testPhones = List.of(firstPhone, secondPhone);
        List<Phone> dbPhones = jdbcPhoneDao.findAll(0, 2);
        List<Phone> similarPhones = dbPhones.stream().filter(testPhones::contains).toList();

        Assertions.assertEquals(similarPhones.size(), testPhones.size());
    }

    @Test
    public void testFindAllDataWithOffset() {
        List<Phone> dbPhones = jdbcPhoneDao.findAll(1, 2);

        Assertions.assertTrue(dbPhones.stream().anyMatch(thirdPhone::equals));
    }

    @Test
    public void testFindNothing() {
        Assertions.assertEquals(0, jdbcPhoneDao.findAll(0, 0).size());
    }

    @Test
    public void testGetPhone() {
        Assertions.assertEquals(jdbcPhoneDao.get(firstPhone.getId()).get(), firstPhone);
    }

    @Test
    public void testGetWithInvalidId() {
        Assertions.assertFalse(jdbcPhoneDao.get(1L).isPresent());
    }

    @Test
    public void testSaveData() {
        jdbcPhoneDao.save(saveTestPhone);

        Phone testedPhone = jdbcTemplate.queryForObject(SELECT_FROM_PHONES_WHERE_ID,
                new Object[]{saveTestPhone.getId()}, new BeanPropertyRowMapper<>(Phone.class));

        testedPhone.setColors(new HashSet<>(jdbcTemplate.query(LOAD_COLLORS_MANUALLY,
                ps -> ps.setLong(1, testedPhone.getId()), new BeanPropertyRowMapper<>(Color.class))));

        Assertions.assertEquals(saveTestPhone, testedPhone);
    }

    @Test
    public void testUpdateData() {
        Assertions.assertTrue(jdbcTemplate.query(SELECT_FROM_PHONES_WHERE_ID,
                ps -> ps.setLong(1, updateTestPhone.getId()),
                new BeanPropertyRowMapper<>(Phone.class)).stream().findFirst().isPresent());

        Phone testedPhone = jdbcTemplate.queryForObject(SELECT_FROM_PHONES_WHERE_ID, new Object[]{updateTestPhone.getId()},
                new BeanPropertyRowMapper<>(Phone.class));
        testedPhone.setColors(new HashSet<>(jdbcTemplate.query(LOAD_COLLORS_MANUALLY,
                ps -> ps.setLong(1, testedPhone.getId()), new BeanPropertyRowMapper<>(Color.class))));

        Assertions.assertNotEquals(updateTestPhone, testedPhone);

        jdbcPhoneDao.save(updateTestPhone);

        Phone newTestedPhone = jdbcTemplate.queryForObject(SELECT_FROM_PHONES_WHERE_ID, new Object[]{updateTestPhone.getId()},
                new BeanPropertyRowMapper<>(Phone.class));
        newTestedPhone.setColors(new HashSet<>(jdbcTemplate.query(LOAD_COLLORS_MANUALLY,
                ps -> ps.setLong(1, newTestedPhone.getId()), new BeanPropertyRowMapper<>(Color.class))));

        Assertions.assertEquals(updateTestPhone, newTestedPhone);
    }

    @Test
    public void testNullIdGet() {
        Assertions.assertThrows(InvalidIdException.class, () -> jdbcPhoneDao.get(null));
    }
}
