package de.joshuagleitze.jspecify.interpreter;

import de.joshuagleitze.jspecify.interpreter.fixtures.*;
import de.joshuagleitze.types.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static de.joshuagleitze.jspecify.interpreter.NullnessOperator.MINUS_NULL;
import static de.joshuagleitze.jspecify.interpreter.NullnessOperator.UNION_NULL;
import static java.util.stream.Stream.concat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@DisplayName("certain type uses are intrinsically non-nullable")
@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntrinsicallyNonNullableTest {
	private final JSpecifyInterpreter jspecify = new JSpecify();

	@DisplayName("primitive types")
	@ParameterizedTest(name = "{0}")
	@MethodSource("primitiveParameters")
	@Order(0)
	void primitives(LocatedTypeUse primitiveType) {
		assertThat(jspecify.nullabilityOf(primitiveType)).isEqualTo(MINUS_NULL);
	}

	static Stream<LocatedTypeUse> primitiveParameters() {
		return Stream.of(Primitives.class.getRecordComponents()).map(LocatedRecordComponentType::new);
	}

	@DisplayName("any component of a return type in an annotation interface")
	@ParameterizedTest(name = "{0}")
	@MethodSource("annotationReturnTypeComponentsParameters")
	@Order(10)
	void annotationReturnTypeComponents(LocatedTypeUse annotatedType) {
		assertThat(jspecify.nullabilityOf(annotatedType)).isEqualTo(MINUS_NULL);
	}

	static Stream<LocatedTypeUse> annotationReturnTypeComponentsParameters() {
		return Stream.of(TestAnnotation.class.getDeclaredMethods()).map(LocatedReturnType::new).flatMap(TypeUtil::withComponents);
	}

	@DisplayName("a supertype in a class declaration")
	@ParameterizedTest(name = "{0}")
	@MethodSource("supertypeParameters")
	@Order(20)
	void supertype(LocatedTypeUse supertype) {
		assertThat(jspecify.nullabilityOf(supertype)).isEqualTo(MINUS_NULL);
	}

	static Stream<LocatedTypeUse> supertypeParameters() {
		return concat(
				Stream.of(ClassesWithSuperclasses.class.getDeclaredClasses()).map(LocatedSuperclass::new),
				Stream.of(ClassWithSuperInterfaces.class, InterfaceWithSuperInterfaces.class)
						.flatMap(c ->
								Stream.of(c.getAnnotatedInterfaces())
										.map(i -> new LocatedSuperinterface(c, i))
						)
		);
	}

	@DisplayName("but *not* components of a supertype")
	@ParameterizedTest(name = "{0}")
	@MethodSource("supertypeComponentsParameters")
	@Order(30)
	void supertypeComponents(LocatedTypeUse supertypeComponent) {
		assertThat(jspecify.nullabilityOf(supertypeComponent)).isEqualTo(UNION_NULL);
	}

	static Stream<LocatedTypeUse> supertypeComponentsParameters() {
		return supertypeParameters().flatMap(TypeUtil::allComponents);
	}

	@DisplayName("an outer type qualifying an inner type")
	@ParameterizedTest(name = "{0}")
	@MethodSource("outerTypeParameters")
	@Order(40)
	void outerType(LocatedTypeUse outerType) {
		assertThat(jspecify.nullabilityOf(outerType)).isEqualTo(MINUS_NULL);
	}

	static Stream<LocatedTypeUse> outerTypeParameters() {
		return Stream.of(OuterClass.class.getDeclaredMethods()).map(method -> new LocatedReturnType(method).getAnnotatedOwnerType());
	}

	@DisplayName("but *not* components of an outer type qualifying an inner type")
	@ParameterizedTest(name = "{0}")
	@MethodSource("outerTypeComponentsParameters")
	@Order(50)
	void outerTypeComponents(LocatedTypeUse outerType) {
		assertThat(jspecify.nullabilityOf(outerType)).isEqualTo(UNION_NULL);
	}

	static Stream<LocatedTypeUse> outerTypeComponentsParameters() {
		return outerTypeParameters().flatMap(TypeUtil::allComponents);
	}
}
