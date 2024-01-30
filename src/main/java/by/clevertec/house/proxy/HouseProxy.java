package by.clevertec.house.proxy;

import by.clevertec.house.cache.Cache;
import by.clevertec.house.cache.impl.LfuCache;
import by.clevertec.house.cache.impl.LruCache;
import by.clevertec.house.dto.HouseRequestDto;
import by.clevertec.house.dto.HouseResponseDto;
import by.clevertec.house.entity.House;
import by.clevertec.house.exception.EntityNotFoundException;
import by.clevertec.house.mapper.HouseMapper;
import by.clevertec.house.repository.HouseRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Класс {@code UserProxy} представляет собой прокси для работы с пользователями.
 * Он использует аспектно-ориентированное программирование (AOP) для кэширования пользователей.
 * <p>
 * Этот класс использует {@code Cache<Integer, User>} для хранения пользователей, где ключом является идентификатор пользователя.
 * Кэш настраивается с помощью метода {@code configureCache()}, который читает конфигурацию из файла YAML.
 * <p>
 * Методы {@code getUser()}, {@code createUser()}, {@code deleteUser()} и {@code updateUser()} объявлены как точки среза (pointcuts) для AOP.
 * Они используются в аннотациях {@code Around} и {@code AfterReturning} для выполнения действий перед, после или вместо вызова соответствующих методов сервиса.
 * <p>
 * Каждый из этих методов записывает информацию в журнал и обновляет кэш соответствующим образом.
 */
@Aspect
@RequiredArgsConstructor
@Component
public class HouseProxy {

    @Value("${cache.algorithm}")
    private String algorithm;

    @Value("${cache.capacity}")
    private Integer maxCapacity;

    private Cache<UUID, Object> userCache = configureCache();
    private final HouseMapper houseMapper;
    private final HouseRepository houseRepository;

    @Pointcut("@annotation(by.clevertec.house.proxy.annotation.Cacheable)")
    public void createCacheable() {
    }

    @Pointcut("@annotation(by.clevertec.house.proxy.annotation.Cacheable) && execution(* by.clevertec.house.service.HouseService.deleteHouse(..)) ")
    public void deleteCacheable() {
    }

    @Pointcut("@annotation(by.clevertec.house.proxy.annotation.Cacheable) && execution(* by.clevertec.house.service.HouseService.updateHouse(..))")
    public void updateCacheable() {
    }

    @SuppressWarnings("checkstyle:IllegalCatch")
    @Around("@annotation(by.clevertec.house.proxy.annotation.Cacheable) && execution(* by.clevertec.house.service.HouseService.getHouseByUuid(..))")
    public Object getHouse(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        UUID uuid = (UUID) args[0];
        HouseResponseDto houseResponseDto = (HouseResponseDto) userCache.get(uuid);
        if (houseResponseDto != null) {
            return houseResponseDto;
        }
        Object result = joinPoint.proceed();
        if (result instanceof HouseResponseDto houseDto) {
            userCache.put(uuid, result);
            return houseDto;
        }
        return result;
    }

    @AfterReturning(pointcut = "createCacheable() && args(houseDto)")
    public void createHouse(HouseRequestDto houseDto) {
        House house = houseMapper.toEntity(houseDto);
        userCache.put(house.getUuid(), house);
    }

    @AfterReturning(pointcut = "deleteCacheable() && args(uuid)")
    public void deleteHouse(UUID uuid) {
        userCache.remove(uuid);
    }

    @AfterReturning(pointcut = "updateCacheable() && args(uuid, houseDto)", argNames = "uuid,houseDto")
    public void updateHouse(UUID uuid, HouseRequestDto houseDto) {
        House house = houseRepository.findByUuid(uuid).orElseThrow(() -> EntityNotFoundException.of(House.class, uuid));
        houseMapper.updateHouseFromDto(houseDto, house);
        userCache.put(house.getUuid(), house);
    }

    /**
     * Метод {@code configureCache} используется для настройки кэша.
     * Он читает конфигурацию из файла YAML и создает кэш с выбранным типом и вместимостью.
     *
     * @return Объект {@code Cache<Integer, User>}, представляющий настроенный кэш.
     */
    private Cache<UUID, Object> configureCache() {
        if (userCache == null) {
            synchronized (this) {
                if (maxCapacity == null) {
                    maxCapacity = 50;
                }
                if ("LFU".equals(algorithm)) {
                    userCache = new LfuCache<>(maxCapacity);
                } else {
                    userCache = new LruCache<>(maxCapacity);
                }
            }
        }
        return userCache;
    }
}
