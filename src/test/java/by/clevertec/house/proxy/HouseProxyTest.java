package by.clevertec.house.proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.clevertec.house.cache.Cache;
import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.entity.House;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.repository.HouseRepository;
import by.clevertec.house.service.HouseService;
import by.clevertec.house.util.HouseTestBuilder;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@RequiredArgsConstructor
class HouseProxyTest {

    private final HouseProxy houseProxy;

    @MockBean
    private final HouseMapper houseMapper;

    @MockBean
    private final HouseRepository houseRepository;

    @MockBean
    private final HouseService houseService;

    @MockBean
    private ProceedingJoinPoint proceedingJoinPoint;

    @Test
    void testGetHouse() throws Throwable {
        HouseResponseDto houseResponseDto = HouseTestBuilder.builder()
                .build()
                .buildHouseResponseDto();

        when(proceedingJoinPoint.getArgs())
                .thenReturn(new Object[]{houseResponseDto.getUuid()});
        when(proceedingJoinPoint.proceed())
                .thenReturn(houseResponseDto);

        Object result = houseProxy.getHouse(proceedingJoinPoint);

        assertEquals(houseResponseDto, result);
        verify(proceedingJoinPoint, times(1)).proceed();
    }

    @Test
    void testCreateHouse() throws Throwable {

        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        HouseResponseDto expected = HouseTestBuilder.builder()
                .build()
                .buildHouseResponseDto();

        HouseRequestDto houseRequestDto = HouseTestBuilder.builder()
                .build()
                .buildHouseRequestDto();

        when(houseMapper.toEntity(houseRequestDto)).thenReturn(house);
        when(houseService.getHouseByUuid(house.getUuid())).thenReturn(expected);

        houseProxy.createHouse(houseRequestDto);

        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{house.getUuid()});
        when(proceedingJoinPoint.proceed()).thenReturn(expected);

        HouseResponseDto result = (HouseResponseDto) houseProxy.getHouse(proceedingJoinPoint);

        assertEquals(expected.getUuid(), result.getUuid());
    }

    @Test
    void testDeleteHouse() throws Exception {
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        // Получаем поле userCache с помощью рефлексии
        Field userCacheField = HouseProxy.class.getDeclaredField("userCache");
        userCacheField.setAccessible(true);
        AtomicReference<Cache<UUID, Object>> userCacheRef =
                (AtomicReference<Cache<UUID, Object>>) userCacheField.get(houseProxy);

        Cache<UUID, Object> userCache = userCacheRef.get();
        userCache.put(house.getUuid(), house);

        houseProxy.deleteHouse(house.getUuid());

        assertNull(userCache.get(house.getUuid()));
    }

    @Test
    void testUpdateHouse() throws Exception {
        House house = HouseTestBuilder.builder()
                .build()
                .buildHouse();

        HouseRequestDto houseRequestDto = HouseTestBuilder.builder()
                .build()
                .buildHouseRequestDto();

        when(houseRepository.findByUuid(house.getUuid())).thenReturn(Optional.of(house));

        // Получаем поле userCache с помощью рефлексии
        Field userCacheField = HouseProxy.class.getDeclaredField("userCache");
        userCacheField.setAccessible(true);
        AtomicReference<Cache<UUID, Object>> userCacheRef =
                (AtomicReference<Cache<UUID, Object>>) userCacheField.get(houseProxy);

        Cache<UUID, Object> userCache = userCacheRef.get();

        houseProxy.updateHouse(house.getUuid(), houseRequestDto);

        assertEquals(house, userCache.get(house.getUuid()));
    }


}