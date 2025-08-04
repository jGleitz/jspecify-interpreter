package de.joshuagleitze.jspecify.interpreter.test;

import de.joshuagleitze.jspecify.interpreter.JSpecify;
import de.joshuagleitze.jspecify.interpreter.JSpecifyInterpreter;
import de.joshuagleitze.jspecify.interpreter.NullnessOperator;
import de.joshuagleitze.types.LocatedParameterType;
import de.joshuagleitze.types.LocatedTypeUse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.Parameter;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static de.joshuagleitze.jspecify.interpreter.NullnessOperator.*;
import static java.util.stream.Stream.concat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@DisplayName("nullness of type uses")
@ParameterizedClass(name = "… {0}")
@CsvSource(
		// language=CSV
		textBlock = """
				# name,                                                  expectedOperatorIfUnspecified, examplesBasePackage
				'without @NullMarked',                                   UNSPECIFIED,                   de.joshuagleitze.jspecify.interpreter.fixtures.notmarked
				'in a @NullMarked package',                              NO_CHANGE,                     de.joshuagleitze.jspecify.interpreter.fixtures.nullmarkedpackage
				'in a @NullMarked class',                                NO_CHANGE,                     de.joshuagleitze.jspecify.interpreter.fixtures.nullmarkedclass
				'in a @NullUnmarked class within a @NullMarked package', UNSPECIFIED,                   de.joshuagleitze.jspecify.interpreter.fixtures.nullunmarkedclass
				'in a @NullMarked module',                               NO_CHANGE,                     de.joshuagleitze.jspecify.interpreter.fixtures.nullmarkedmodule.notmarked
				'in a @NullUnmarked package in a @NullMarked module',    UNSPECIFIED,                   de.joshuagleitze.jspecify.interpreter.fixtures.nullmarkedmodule.nullunmarkedpackage
				"""
)
@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NullnessOperatorRulesTest {
	private final JSpecifyInterpreter jspecify = new JSpecify();
	@SuppressWarnings("unused")
	@Parameter(0)
	private String classTestName;
	@Parameter(1)
	private NullnessOperator expectedOperatorIfUnspecified;
	@Parameter(2)
	private String examplesBasePackage;

	private Class<?> getExampleClass(String name) throws ClassNotFoundException {
		return getClass().getClassLoader().loadClass(examplesBasePackage + "." + name);
	}

	@DisplayName("… explicitly marked with @Nullable")
	@ParameterizedTest(name = "{0}")
	@MethodSource("explicitlyNullableParameters")
	@Order(0)
	void explicitlyNullable(LocatedTypeUse explicitlyNullableType) {
		assertThat(jspecify.nullabilityOf(explicitlyNullableType)).isEqualTo(UNION_NULL);
	}

	Stream<LocatedTypeUse> explicitlyNullableParameters() throws ClassNotFoundException {
		return concat(
				Stream.of(getExampleClass("ExplicitlyNullable").getDeclaredMethods()).flatMap(TypeUtil::returnAndParameterTypes),
				Stream.of(getExampleClass("ExplicitlyNullable$Components").getDeclaredMethods())
						.flatMap(TypeUtil::returnAndParameterTypes)
						.flatMap(TypeUtil::allComponents)
		);
	}

	@DisplayName("… explicitly marked with @NonNull")
	@ParameterizedTest(name = "{0}")
	@MethodSource("explicitlyNonNullParameters")
	@Order(10)
	void explicitlyNonNull(LocatedTypeUse explicitlyNonNullType) {
		assertThat(jspecify.nullabilityOf(explicitlyNonNullType)).isEqualTo(MINUS_NULL);
	}

	Stream<LocatedTypeUse> explicitlyNonNullParameters() throws ClassNotFoundException {
		return concat(
				Stream.of(getExampleClass("ExplicitlyNonNull").getDeclaredMethods()).flatMap(TypeUtil::returnAndParameterTypes),
				Stream.of(getExampleClass("ExplicitlyNonNull$Components").getDeclaredMethods())
						.flatMap(TypeUtil::returnAndParameterTypes)
						.flatMap(TypeUtil::allComponents)
		);
	}

	@DisplayName("… marked with neither @NonNull nor @Nullable")
	@ParameterizedTest(name = "{0}")
	@MethodSource("noAnnotationsParameters")
	@Order(20)
	void noAnnotations(LocatedTypeUse typeWithBothAnnotations) {
		assertThat(jspecify.nullabilityOf(typeWithBothAnnotations)).isEqualTo(expectedOperatorIfUnspecified);
	}

	Stream<LocatedTypeUse> noAnnotationsParameters() throws ClassNotFoundException {
		return concat(
				Stream.of(getExampleClass("NoAnnotations").getDeclaredMethods()).flatMap(TypeUtil::returnAndParameterTypes),
				Stream.of(getExampleClass("NoAnnotations$Components").getDeclaredMethods())
						.flatMap(TypeUtil::returnAndParameterTypes)
						.flatMap(TypeUtil::allComponents)
		);
	}

	@DisplayName("… marked with both @NonNull and @Nullable")
	@ParameterizedTest(name = "{0}")
	@MethodSource("bothAnnotationsParameters")
	@Order(30)
	void bothAnnotations(LocatedTypeUse typeWithBothAnnotations) {
		assertThat(jspecify.nullabilityOf(typeWithBothAnnotations)).isEqualTo(expectedOperatorIfUnspecified);
	}

	Stream<LocatedTypeUse> bothAnnotationsParameters() throws ClassNotFoundException {
		return concat(
				Stream.of(getExampleClass("BothAnnotations").getDeclaredMethods()).flatMap(TypeUtil::returnAndParameterTypes),
				Stream.of(getExampleClass("BothAnnotations$Components").getDeclaredMethods())
						.flatMap(TypeUtil::returnAndParameterTypes)
						.flatMap(TypeUtil::allComponents)
		);
	}

	@DisplayName("parameters of equals(Object) in records")
	@Test
	@Order(40)
	void equalsInRecord() throws NoSuchMethodException, ClassNotFoundException {
		var equalsOfRecord = getExampleClass("EqualsInRecord").getMethod("equals", Object.class);
		var equalsParameter = new LocatedParameterType(equalsOfRecord.getParameters()[0]);
		assertThat(jspecify.nullabilityOf(equalsParameter)).isEqualTo(UNION_NULL);
	}

	@DisplayName("parameters of any other equals method in records")
	@ParameterizedTest(name = "{0}")
	@MethodSource("otherEqualsInRecordParameters")
	@Order(50)
	void otherEqualsInRecord(LocatedTypeUse otherEqualsParameterType) {
		assertThat(jspecify.nullabilityOf(otherEqualsParameterType)).isEqualTo(expectedOperatorIfUnspecified);
	}

	Stream<LocatedTypeUse> otherEqualsInRecordParameters() throws ClassNotFoundException {
		return Stream.of(getExampleClass("EqualsInRecord").getDeclaredMethods())
				.filter(equalsMethod -> equalsMethod.getParameterCount() != 1 || !equalsMethod.getParameters()[0].getType().equals(Object.class))
				.flatMap(TypeUtil::parameterTypes);
	}
}
