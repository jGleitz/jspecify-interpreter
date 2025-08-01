package de.joshuagleitze.jspecify.interpreter;

import lombok.experimental.UtilityClass;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

@UtilityClass
public class TypeUtil {
    public static Stream<AnnotatedType> withComponents(AnnotatedType type) {
        if (type instanceof AnnotatedParameterizedType parameterizedType) {
            return concat(Stream.of(type), Stream.of(parameterizedType.getAnnotatedActualTypeArguments()).flatMap(TypeUtil::withComponents));
        } else if (type instanceof AnnotatedArrayType arrayType) {
            return concat(Stream.of(type), withComponents(arrayType.getAnnotatedGenericComponentType()));
        }
        return Stream.of(type);
    }

}
