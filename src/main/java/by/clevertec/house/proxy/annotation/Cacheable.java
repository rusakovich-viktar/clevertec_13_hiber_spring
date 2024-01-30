package by.clevertec.house.proxy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация {@code Cacheable} используется для обозначения методов, которые должны быть кэшированы.
 * Эта аннотация предназначена для использования с аспектно-ориентированным программированием (AOP).
 * Методы, аннотированные как {@code Cacheable}, будут автоматически кэшироваться при вызове.
 * Аннотация имеет политику удержания {@code RetentionPolicy.RUNTIME}, что означает, что она будет доступна во время выполнения JVM.
 * Она также имеет цель {@code ElementType.METHOD}, что означает, что она может быть применена только к методам.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

}
