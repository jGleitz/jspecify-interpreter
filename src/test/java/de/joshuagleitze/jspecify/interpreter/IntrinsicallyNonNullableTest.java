package de.joshuagleitze.jspecify.interpreter;

import de.joshuagleitze.jspecify.interpreter.examples.*;
import de.joshuagleitze.types.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static de.joshuagleitze.jspecify.interpreter.NullnessOperator.MINUS_NULL;
import static java.util.stream.Stream.concat;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("certain type uses are intrinsically non-nullable")
public class IntrinsicallyNonNullableTest {
	private final JSpecifyInterpreter jspecify = new JSpecify();

	@DisplayName("primitive types")
	@ParameterizedTest
	@MethodSource("primitiveArguments")
	void primitives(LocatedTypeUse primitiveType) {
		assertThat(jspecify.nullabilityOf(primitiveType)).isEqualTo(MINUS_NULL);
	}

	static Stream<LocatedTypeUse> primitiveArguments() {
		return Stream.of(Primitives.class.getRecordComponents()).map(LocatedRecordComponentType::new);
	}

	@DisplayName("any component of a return type in an annotation interface")
	@ParameterizedTest
	@MethodSource("annotationReturnTypeComponentsArguments")
	void annotationReturnTypeComponents(LocatedTypeUse annotatedType) {
		assertThat(jspecify.nullabilityOf(annotatedType)).isEqualTo(MINUS_NULL);
	}

	static Stream<LocatedTypeUse> annotationReturnTypeComponentsArguments() {
		return Stream.of(TestAnnotation.class.getDeclaredMethods()).map(LocatedReturnType::new).flatMap(TypeUtil::withComponents);
	}

	@DisplayName("a supertype in a class declaration")
	@ParameterizedTest
	@MethodSource("supertypeArguments")
	void supertype(LocatedTypeUse supertype) {
		assertThat(jspecify.nullabilityOf(supertype)).isEqualTo(MINUS_NULL);
	}

	static Stream<LocatedTypeUse> supertypeArguments() {
		return concat(
				Stream.of(ClassesWithSuperclasses.class.getDeclaredClasses()).map(LocatedSuperclass::new),
				Stream.of(ClassWithSuperInterfaces.class, InterfaceWithSuperInterfaces.class)
						.flatMap(c ->
								Stream.of(c.getAnnotatedInterfaces())
										.map(i -> new LocatedSuperinterface(c, i))
						)
		);
	}
}
