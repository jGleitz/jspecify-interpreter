package de.joshuagleitze.types;

import lombok.experimental.UtilityClass;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

@UtilityClass
class LocatedTypeUses {
	static List<LocatedTypeComponent> componentsOf(LocatedTypeUse outerType, AnnotatedType reflectionOuterType) {
		if (reflectionOuterType instanceof AnnotatedParameterizedType parameterized) {
			var parameterTypes = parameterized.getAnnotatedActualTypeArguments();
			var components = new ArrayList<LocatedTypeComponent>();
			for (var parameterType : parameterTypes) {
				components.add(new LocatedTypeComponent(outerType, parameterType));
			}
			return unmodifiableList(components);
		} else if (reflectionOuterType instanceof AnnotatedArrayType array) {
			return List.of(new LocatedTypeComponent(outerType, array.getAnnotatedGenericComponentType()));
		} else {
			return emptyList();
		}
	}
}
