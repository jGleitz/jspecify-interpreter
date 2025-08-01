package de.joshuagleitze.jspecify.interpreter;

import de.joshuagleitze.jspecify.interpreter.examples.Primitives;
import de.joshuagleitze.jspecify.interpreter.examples.TestAnnotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static de.joshuagleitze.jspecify.interpreter.NullnessOperator.MINUS_NULL;
import static org.assertj.core.api.Assertions.assertThat;

public class IntrinsicallyNonNullableTest {
    private final JSpecifyInterpreter jspecify = new JSpecify();

    @DisplayName("primitive types are intrinsically non-nullable")
    @ParameterizedTest
    @MethodSource("primitiveArguments")
    void primitives(AnnotatedType primitiveType) {
        assertThat(jspecify.nullabilityOf(primitiveType)).isEqualTo(MINUS_NULL);
    }

    static AnnotatedType[] primitiveArguments() {
        return Primitives.class.getConstructors()[0].getAnnotatedParameterTypes();
    }

    @DisplayName("any component of a return type in an annotation interface is non-nullable")
    @ParameterizedTest
    @MethodSource("annotationReturnTypeComponentsArguments")
    void annotationReturnTypeComponents(AnnotatedType annotatedType) {
        assertThat(jspecify.nullabilityOf(annotatedType)).isEqualTo(MINUS_NULL);
    }

    static Stream<AnnotatedType> annotationReturnTypeComponentsArguments() {
        return Stream.of(TestAnnotation.class.getDeclaredMethods()).map(Method::getAnnotatedReturnType).flatMap(TypeUtil::withComponents);
    }
}
