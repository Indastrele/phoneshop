package com.es.core.cart;

import com.es.core.model.phone.Color;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.model.phone.dao.JdbcPhoneDao;
import com.es.core.model.phone.dao.JdbcStockDao;
import com.es.core.model.phone.exception.DataNotFoundException;
import com.es.core.model.phone.exception.InvalidIdException;
import com.es.core.model.phone.exception.NotEnoughStockException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class HttpCartServiceMockTest {
    private static List<CartItem> cartItems;
    private static Long totalQuantity;
    private static BigDecimal totalCost;
    @Mock
    private static JdbcPhoneDao jdbcPhoneDao;
    @Mock
    private static Cart cart;
    @Mock
    private static JdbcStockDao jdbcStockDao;

    @InjectMocks
    private static HttpSessionCartService httpSessionCartService;

    @BeforeEach
    public void setAll() {
        cartItems = new ArrayList<>();
        totalQuantity = 0L;
        totalCost = BigDecimal.ZERO;

        Color blackColor = new Color(0L, "Black");
        Color whiteColor = new Color(1L, "White");
        Color greenColor = new Color("Green");

        Phone firstPhone = new Phone(1000L, "test", "test", BigDecimal.valueOf(4.0), BigDecimal.valueOf(10.1),
                482, BigDecimal.valueOf(276.0), BigDecimal.valueOf(167.0), BigDecimal.valueOf(12.6),
                null, "test", "test", Set.of(blackColor), "test", 149,
                null, null, BigDecimal.valueOf(1.3), null,
                BigDecimal.valueOf(8.0), null, null, null,
                "test", "test", "test", "test");

        Phone secondPhone = new Phone(1001L, "test", "test1", BigDecimal.valueOf(1.0), BigDecimal.valueOf(10.1),
                482, BigDecimal.valueOf(250.0), BigDecimal.valueOf(174.0), BigDecimal.valueOf(10.0),
                null, "test", "test", Set.of(whiteColor), "test", 149,
                "test", BigDecimal.valueOf(5.0), BigDecimal.valueOf(0.3), BigDecimal.valueOf(1.0),
                BigDecimal.valueOf(8.0), 6500, null, null,
                "test", "test", "test", "test");

        Phone thirdPhone = new Phone(1002L, "test", "test2", BigDecimal.valueOf(1.0), BigDecimal.valueOf(10.1),
                482, null, null, null,
                null, "test", "test", Set.of(blackColor), "test", 118,
                "test", null, BigDecimal.valueOf(0.3), null,
                BigDecimal.valueOf(8.0), null, null, null,
                "test", null, "test", "test");
        Phone fourthPhone = new Phone(1003L, "test", "test4", BigDecimal.valueOf(1.0), BigDecimal.valueOf(10.1),
                482, BigDecimal.valueOf(265.4), BigDecimal.valueOf(181.0), BigDecimal.valueOf(13.4),
                null, "test", "test", Set.of(whiteColor, blackColor, greenColor),
                "test", 149, "test", BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(2.0), BigDecimal.valueOf(1.0), BigDecimal.valueOf(16.0), 6000,
                null, null, "test", "test", "test", "test");

        Stock firstStock = new Stock(firstPhone, 10, 7);
        Stock secondStock = new Stock(secondPhone, 10, 0);
        Stock thirdStock = new Stock(thirdPhone, 5, 5);

        Mockito.when(cart.getCartItems()).thenReturn(cartItems);

        Mockito.doReturn(totalQuantity).when(cart).getTotalQuantity();
        Mockito.doAnswer(invocationOnMock -> {
            totalQuantity = invocationOnMock.getArgument(0);
            return null;
        }).when(cart).setTotalQuantity(Mockito.isA(Long.class));

        Mockito.doReturn(totalCost).when(cart).getTotalCost();
        Mockito.doAnswer(invocationOnMock -> {
            totalCost = invocationOnMock.getArgument(0);
            return null;
        }).when(cart).setTotalCost(Mockito.isA(BigDecimal.class));

        Mockito.when(jdbcPhoneDao.get(1000L)).thenReturn(Optional.of(firstPhone));
        Mockito.when(jdbcPhoneDao.get(1001L)).thenReturn(Optional.of(secondPhone));
        Mockito.when(jdbcPhoneDao.get(1002L)).thenReturn(Optional.of(thirdPhone));
        Mockito.when(jdbcPhoneDao.get(1003L)).thenReturn(Optional.of(fourthPhone));

        Mockito.when(jdbcStockDao.get(1000L)).thenReturn(Optional.of(firstStock));
        Mockito.when(jdbcStockDao.get(1001L)).thenReturn(Optional.of(secondStock));
        Mockito.when(jdbcStockDao.get(1002L)).thenReturn(Optional.of(thirdStock));
    }

    @Test
    public void testAddPhones() {
        Assertions.assertEquals(0, cartItems.size());

        httpSessionCartService.addPhone(1000L, 1L);
        Mockito.verify(cart).setTotalQuantity(1L);
        Mockito.verify(cart).setTotalCost(BigDecimal.valueOf(4.0));

        Assertions.assertEquals(1L, cartItems.size());
        Assertions.assertEquals(1L, totalQuantity);
        Assertions.assertEquals(BigDecimal.valueOf(4.0), totalCost);
    }

    @Test
    public void testAddMoreQuantity() {
        httpSessionCartService.addPhone(1000L, 1L);

        Assertions.assertEquals(1L, cartItems.size());
        Assertions.assertEquals(1L, totalQuantity);
        Assertions.assertEquals(BigDecimal.valueOf(4.0), totalCost);

        httpSessionCartService.addPhone(1000L, 1L);

        Assertions.assertEquals(1L, cartItems.size());
        Assertions.assertEquals(2L, totalQuantity);
        Assertions.assertEquals(BigDecimal.valueOf(8.0), totalCost);
    }

    @Test
    public void testAddMorePhones() {
        httpSessionCartService.addPhone(1000L, 1L);

        Assertions.assertEquals(1L, cartItems.size());
        Assertions.assertEquals(1L, totalQuantity);
        Assertions.assertEquals(BigDecimal.valueOf(4.0), totalCost);

        httpSessionCartService.addPhone(1001L, 1L);

        Assertions.assertEquals(2L, cartItems.size());
        Assertions.assertEquals(2L, totalQuantity);
        Assertions.assertEquals(BigDecimal.valueOf(5.0), totalCost);
    }

    @Test
    public void testAddPhoneWithNoStock() {
        Assertions.assertThrows(NotEnoughStockException.class, () -> httpSessionCartService
                .addPhone(1002L, 1L));
    }

    @Test
    public void testAddNotExistingPhone() {
        Mockito.when(jdbcPhoneDao.get(Mockito.any())).thenReturn(Optional.empty());
        Assertions.assertThrows(InvalidIdException.class, () -> httpSessionCartService
                .addPhone(1L, Mockito.any()));
    }

    @Test
    public void testAddPhoneWithoutStockData() {
        Mockito.when(jdbcStockDao.get(Mockito.any())).thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class, () -> httpSessionCartService
                .addPhone(1003L, Mockito.any()));
    }

    @Test
    public void testRemoveItem() {
        httpSessionCartService.addPhone(1000L, 1L);

        Assertions.assertEquals(1L, cartItems.size());
        Assertions.assertEquals(1L, totalQuantity);
        Assertions.assertEquals(BigDecimal.valueOf(4.0), totalCost);

        httpSessionCartService.remove(1000L);
        Assertions.assertEquals(0L, cartItems.size());
        Assertions.assertEquals(0L, totalQuantity);
        Assertions.assertEquals(BigDecimal.ZERO, totalCost);
    }

    @Test
    public void testUpdateObjects() {
        httpSessionCartService.addPhone(1000L, 1L);
        httpSessionCartService.addPhone(1001L, 2L);
        Assertions.assertEquals(2L, cartItems.size());
        Assertions.assertEquals(3L, totalQuantity);
        Assertions.assertEquals(BigDecimal.valueOf(6.0), totalCost);

        Map<Long, Long> updatedValues = Map.of(1000L, 2L, 1001L, 5L);
        httpSessionCartService.update(updatedValues);
        Assertions.assertEquals(2L, cartItems.size());
        Assertions.assertEquals(7L, totalQuantity);
        Assertions.assertEquals(BigDecimal.valueOf(13.0), totalCost);
    }

    @Test
    public void testUpdateObjectsWithNotEnoughStock() {
        httpSessionCartService.addPhone(1000L, 1L);
        httpSessionCartService.addPhone(1001L, 2L);
        Assertions.assertEquals(2L, cartItems.size());
        Assertions.assertEquals(3L, totalQuantity);
        Assertions.assertEquals(BigDecimal.valueOf(6.0), totalCost);

        Map<Long, Long> updatedValues = Map.of(1000L, 100L, 1001L, 5L);
        Assertions.assertThrows(NotEnoughStockException.class, () -> httpSessionCartService.update(updatedValues));
    }

    @Test
    public void testUpdateObjectsWithExtraData() {
        httpSessionCartService.addPhone(1000L, 1L);
        httpSessionCartService.addPhone(1001L, 2L);
        Assertions.assertEquals(2L, cartItems.size());
        Assertions.assertEquals(3L, totalQuantity);
        Assertions.assertEquals(BigDecimal.valueOf(6.0), totalCost);

        Map<Long, Long> updatedValues = Map.of(1000L, 2L, 1001L, 5L, 1003L, 1L);
        httpSessionCartService.update(updatedValues);
        Assertions.assertEquals(2L, cartItems.size());
        Assertions.assertEquals(7L, totalQuantity);
        Assertions.assertEquals(BigDecimal.valueOf(13.0), totalCost);
    }

    @Test
    public void testGetTotalCost() {
        totalCost = BigDecimal.TEN;
        Mockito.when(cart.getTotalCost()).thenReturn(totalCost);

        Assertions.assertSame(totalCost, httpSessionCartService.getTotalCost());
        Mockito.verify(cart).getTotalCost();
    }

    @Test
    public void testGetTotalQuantity() {
        totalQuantity = 1L;
        Mockito.when(cart.getTotalQuantity()).thenReturn(totalQuantity);

        Assertions.assertSame(totalQuantity, httpSessionCartService.getTotalQuantity());
        Mockito.verify(cart).getTotalQuantity();
    }
}
