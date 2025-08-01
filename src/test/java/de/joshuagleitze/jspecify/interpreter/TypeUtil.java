package de.joshuagleitze.jspecify.interpreter;

import de.joshuagleitze.types.LocatedTypeUse;
import lombok.experimental.UtilityClass;

import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

@UtilityClass
public class TypeUtil {
	public static Stream<LocatedTypeUse> withComponents(LocatedTypeUse typeUse) {
		return concat(Stream.of(typeUse), typeUse.components().stream());
	}
}
