package com.es.core.model.phone;

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

    @Autowired
    private JdbcPhoneDao jdbcPhoneDao;

    private static Phone testPhone;
    private static Color blackColor;
    private static Color whiteColor;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    public static void setTestPhone() {
        testPhone = new Phone();
        testPhone.setId(1000L);
        testPhone.setBrand("test");
        testPhone.setModel("test");
        testPhone.setPrice(null);
        testPhone.setDisplaySizeInches(BigDecimal.valueOf(10.1));
        testPhone.setWeightGr(482);
        testPhone.setLengthMm(BigDecimal.valueOf(276.0));
        testPhone.setWidthMm(BigDecimal.valueOf(167.0));
        testPhone.setHeightMm(BigDecimal.valueOf(12.6));
        testPhone.setAnnounced(null);
        testPhone.setDeviceType("test");
        testPhone.setOs("test");
        testPhone.setDisplayResolution("test");
        testPhone.setPixelDensity(149);
        testPhone.setDisplayTechnology(null);
        testPhone.setBackCameraMegapixels(null);
        testPhone.setFrontCameraMegapixels(BigDecimal.valueOf(1.3));
        testPhone.setRamGb(null);
        testPhone.setInternalStorageGb(BigDecimal.valueOf(8.0));
        testPhone.setBatteryCapacityMah(null);
        testPhone.setTalkTimeHours(null);
        testPhone.setStandByTimeHours(null);
        testPhone.setBluetooth("test");
        testPhone.setPositioning("test");
        testPhone.setImageUrl("test");
        testPhone.setDescription("test");

        blackColor = new Color();
        blackColor.setCode("Black");
        blackColor.setId(0L);
        whiteColor = new Color();
        whiteColor.setCode("White");
        whiteColor.setId(1L);

        testPhone.setColors(Set.of(blackColor));
    }

    @Test
    public void testFindAllData() {
        var secondPhone = new Phone();
        secondPhone.setId(1001L);
        secondPhone.setBrand("test");
        secondPhone.setModel("test1");
        secondPhone.setPrice(null);
        secondPhone.setDisplaySizeInches(BigDecimal.valueOf(10.1));
        secondPhone.setWeightGr(482);
        secondPhone.setLengthMm(BigDecimal.valueOf(250.0));
        secondPhone.setWidthMm(BigDecimal.valueOf(174.0));
        secondPhone.setHeightMm(BigDecimal.valueOf(10.0));
        secondPhone.setAnnounced(null);
        secondPhone.setDeviceType("test");
        secondPhone.setOs("test");
        secondPhone.setDisplayResolution("test");
        secondPhone.setPixelDensity(149);
        secondPhone.setDisplayTechnology("test");
        secondPhone.setBackCameraMegapixels(BigDecimal.valueOf(5.0));
        secondPhone.setFrontCameraMegapixels(BigDecimal.valueOf(0.3));
        secondPhone.setRamGb(BigDecimal.valueOf(1.0));
        secondPhone.setInternalStorageGb(BigDecimal.valueOf(8.0));
        secondPhone.setBatteryCapacityMah(6500);
        secondPhone.setTalkTimeHours(null);
        secondPhone.setStandByTimeHours(null);
        secondPhone.setBluetooth("test");
        secondPhone.setPositioning("test");
        secondPhone.setImageUrl("test");
        secondPhone.setDescription("test");
        secondPhone.setColors(Set.of(whiteColor));

        List<Phone> testPhones = List.of(testPhone, secondPhone);
        List<Phone> dbPhones = jdbcPhoneDao.findAll(0, 2);

        Assertions.assertTrue(dbPhones.stream().filter(testPhones::contains).toList().size() == testPhones.size());
    }

    @Test
    public void testFindAllDataWithOffset() {
        var secondPhone = new Phone();
        secondPhone.setId(1001L);
        secondPhone.setBrand("test");
        secondPhone.setModel("test1");
        secondPhone.setPrice(null);
        secondPhone.setDisplaySizeInches(BigDecimal.valueOf(10.1));
        secondPhone.setWeightGr(482);
        secondPhone.setLengthMm(BigDecimal.valueOf(250.0));
        secondPhone.setWidthMm(BigDecimal.valueOf(174.0));
        secondPhone.setHeightMm(BigDecimal.valueOf(10.0));
        secondPhone.setAnnounced(null);
        secondPhone.setDeviceType("test");
        secondPhone.setOs("test");
        secondPhone.setDisplayResolution("test");
        secondPhone.setPixelDensity(149);
        secondPhone.setDisplayTechnology("test");
        secondPhone.setBackCameraMegapixels(BigDecimal.valueOf(5.0));
        secondPhone.setFrontCameraMegapixels(BigDecimal.valueOf(0.3));
        secondPhone.setRamGb(BigDecimal.valueOf(1.0));
        secondPhone.setInternalStorageGb(BigDecimal.valueOf(8.0));
        secondPhone.setBatteryCapacityMah(6500);
        secondPhone.setTalkTimeHours(null);
        secondPhone.setStandByTimeHours(null);
        secondPhone.setBluetooth("test");
        secondPhone.setPositioning("test");
        secondPhone.setImageUrl("test");
        secondPhone.setDescription("test");
        secondPhone.setColors(Set.of(whiteColor));


        var thirdPhone = new Phone();
        thirdPhone.setId(1002L);
        thirdPhone.setBrand("test");
        thirdPhone.setModel("test2");
        thirdPhone.setPrice(null);
        thirdPhone.setDisplaySizeInches(BigDecimal.valueOf(10.1));
        thirdPhone.setWeightGr(482);
        thirdPhone.setLengthMm(null);
        thirdPhone.setWidthMm(null);
        thirdPhone.setHeightMm(null);
        thirdPhone.setAnnounced(null);
        thirdPhone.setDeviceType("test");
        thirdPhone.setOs("test");
        thirdPhone.setDisplayResolution("test");
        thirdPhone.setPixelDensity(118);
        thirdPhone.setDisplayTechnology("test");
        thirdPhone.setBackCameraMegapixels(null);
        thirdPhone.setFrontCameraMegapixels(BigDecimal.valueOf(0.3));
        thirdPhone.setRamGb(null);
        thirdPhone.setInternalStorageGb(BigDecimal.valueOf(8.0));
        thirdPhone.setBatteryCapacityMah(null);
        thirdPhone.setTalkTimeHours(null);
        thirdPhone.setStandByTimeHours(null);
        thirdPhone.setBluetooth("test");
        thirdPhone.setPositioning(null);
        thirdPhone.setImageUrl("test");
        thirdPhone.setDescription("test");
        thirdPhone.setColors(Set.of(blackColor));

        List<Phone> dbPhones = jdbcPhoneDao.findAll(1, 2);

        Assertions.assertTrue(dbPhones.stream().anyMatch(thirdPhone::equals));
    }

    @Test
    public void testFindNothing() {
        Assertions.assertEquals(0, jdbcPhoneDao.findAll(0, 0).size());
    }

    @Test
    public void testGetPhone() {
        Assertions.assertEquals(jdbcPhoneDao.get(testPhone.getId()).get(), testPhone);
    }

    @Test
    public void testGetWithInvalidId() {
        Assertions.assertFalse(jdbcPhoneDao.get(1L).isPresent());
    }

    @Test
    public void testSaveData() {
        var testPhoneTwo = new Phone();
        testPhoneTwo.setId(1006L);
        testPhoneTwo.setBrand("test");
        testPhoneTwo.setModel("test7");
        testPhoneTwo.setPrice(null);
        testPhoneTwo.setDisplaySizeInches(BigDecimal.valueOf(10.1));
        testPhoneTwo.setWeightGr(482);
        testPhoneTwo.setLengthMm(BigDecimal.valueOf(276.0));
        testPhoneTwo.setWidthMm(BigDecimal.valueOf(167.0));
        testPhoneTwo.setHeightMm(BigDecimal.valueOf(12.6));
        testPhoneTwo.setAnnounced(null);
        testPhoneTwo.setDeviceType("test");
        testPhoneTwo.setOs("test");
        testPhoneTwo.setDisplayResolution("test");
        testPhoneTwo.setPixelDensity(149);
        testPhoneTwo.setDisplayTechnology(null);
        testPhoneTwo.setBackCameraMegapixels(null);
        testPhoneTwo.setFrontCameraMegapixels(BigDecimal.valueOf(1.3));
        testPhoneTwo.setRamGb(null);
        testPhoneTwo.setInternalStorageGb(BigDecimal.valueOf(8.0));
        testPhoneTwo.setBatteryCapacityMah(null);
        testPhoneTwo.setTalkTimeHours(null);
        testPhoneTwo.setStandByTimeHours(null);
        testPhoneTwo.setBluetooth("test");
        testPhoneTwo.setPositioning("test");
        testPhoneTwo.setImageUrl("test");
        testPhoneTwo.setDescription("test");
        testPhoneTwo.setColors(Set.of(whiteColor, blackColor));

        jdbcPhoneDao.save(testPhoneTwo);

        Phone testedPhone = jdbcTemplate.queryForObject("select * from phones where id = " + testPhoneTwo.getId(),
                new BeanPropertyRowMapper<>(Phone.class));
        testedPhone.setColors(new HashSet<>(jdbcTemplate.query("select * from colors " +
                "join phone2color on colors.id = phone2color.colorId " +
                        "where phone2color.phoneId = " + testedPhone.getId(), new BeanPropertyRowMapper<>(Color.class))));

        Assertions.assertEquals(testPhoneTwo, testedPhone);
    }

    @Test
    public void testUpdateData() {
        Assertions.assertTrue(jdbcTemplate.query("select * from phones where id = 1004",
                new BeanPropertyRowMapper<>(Phone.class)).stream().findFirst().isPresent());

        var testPhoneTwo = new Phone();
        testPhoneTwo.setId(1004L);
        testPhoneTwo.setBrand("test");
        testPhoneTwo.setModel("test4");
        testPhoneTwo.setPrice(null);
        testPhoneTwo.setDisplaySizeInches(BigDecimal.valueOf(10.1));
        testPhoneTwo.setWeightGr(482);
        testPhoneTwo.setLengthMm(BigDecimal.valueOf(265.4));
        testPhoneTwo.setWidthMm(BigDecimal.valueOf(181.0));
        testPhoneTwo.setHeightMm(BigDecimal.valueOf(13.4));
        testPhoneTwo.setAnnounced(null);
        testPhoneTwo.setDeviceType("test");
        testPhoneTwo.setOs("test");
        testPhoneTwo.setDisplayResolution("test");
        testPhoneTwo.setPixelDensity(149);
        testPhoneTwo.setDisplayTechnology("test");
        testPhoneTwo.setBackCameraMegapixels(BigDecimal.valueOf(5.0));
        testPhoneTwo.setFrontCameraMegapixels(BigDecimal.valueOf(2.0));
        testPhoneTwo.setRamGb(BigDecimal.valueOf(1.0));
        testPhoneTwo.setInternalStorageGb(BigDecimal.valueOf(16.0));
        testPhoneTwo.setBatteryCapacityMah(6000);
        testPhoneTwo.setTalkTimeHours(null);
        testPhoneTwo.setStandByTimeHours(null);
        testPhoneTwo.setBluetooth("test");
        testPhoneTwo.setPositioning("test");
        testPhoneTwo.setImageUrl("test");
        testPhoneTwo.setDescription("test");
        testPhoneTwo.setColors(Set.of(whiteColor, blackColor));

        Phone testedPhone = jdbcTemplate.queryForObject("select * from phones where id = " + testPhoneTwo.getId(),
                new BeanPropertyRowMapper<>(Phone.class));
        testedPhone.setColors(new HashSet<>(jdbcTemplate.query("select * from colors " +
                "join phone2color on colors.id = phone2color.colorId " +
                "where phone2color.phoneId = " + testedPhone.getId(), new BeanPropertyRowMapper<>(Color.class))));

        Assertions.assertNotEquals(testPhoneTwo, testedPhone);

        jdbcPhoneDao.save(testPhoneTwo);

        testedPhone = jdbcTemplate.queryForObject("select * from phones where id = " + testPhoneTwo.getId(),
                new BeanPropertyRowMapper<>(Phone.class));
        testedPhone.setColors(new HashSet<>(jdbcTemplate.query("select * from colors " +
                "join phone2color on colors.id = phone2color.colorId " +
                "where phone2color.phoneId = " + testedPhone.getId(), new BeanPropertyRowMapper<>(Color.class))));

        Assertions.assertEquals(testPhoneTwo, testedPhone);
    }
}
