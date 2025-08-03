package de.joshuagleitze.jspecify.interpreter;

import de.joshuagleitze.types.LocatedParameterType;
import de.joshuagleitze.types.LocatedReturnType;
import de.joshuagleitze.types.LocatedTypeUse;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

@UtilityClass
public class TypeUtil {
	public static Stream<LocatedTypeUse> withComponents(LocatedTypeUse typeUse) {
		return concat(Stream.of(typeUse), allComponents(typeUse));
	}

	public static Stream<LocatedTypeUse> allComponents(LocatedTypeUse typeUse) {
		return Stream.of(typeUse.getComponents()).flatMap(TypeUtil::withComponents);
	}

	public static Stream<LocatedTypeUse> returnAndParameterTypes(Method method) {
		return concat(Stream.of(new LocatedReturnType(method)), parameterTypes(method));
	}

	public static Stream<LocatedParameterType> parameterTypes(Method method) {
		return Stream.of(method.getParameters()).map(LocatedParameterType::new);
	}
}
