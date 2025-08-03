package de.joshuagleitze.types;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;

import static lombok.AccessLevel.PROTECTED;

@RequiredArgsConstructor(access = PROTECTED)
abstract class BaseLocatedTypeUse implements LocatedTypeUse {
	/// The JDK’s representation of this type.
	@Delegate
	protected abstract AnnotatedType underlyingType();

	@Override
	public final @Nullable LocatedOuterType getAnnotatedOwnerType() {
		var owner = underlyingType().getAnnotatedOwnerType();
		return owner != null ? new LocatedOuterType(this, owner) : null;
	}

	@Override
	public LocatedTypeComponent[] getComponents() {
		if (underlyingType() instanceof AnnotatedParameterizedType parameterized) {
			var parameterTypes = parameterized.getAnnotatedActualTypeArguments();
			var components = new LocatedTypeComponent[parameterTypes.length];
			for (int i = 0; i < parameterTypes.length; i++) {
				components[i] = new LocatedTypeComponent(this, parameterTypes[i]);
			}
			return components;
		} else if (underlyingType() instanceof AnnotatedArrayType array) {
			return new LocatedTypeComponent[]{new LocatedTypeComponent(this, array.getAnnotatedGenericComponentType())};
		} else {
			return new LocatedTypeComponent[]{};
		}
	}

	abstract void appendTo(StringBuilder result);

	@Override
	public final String toString() {
		var result = new StringBuilder();
		appendTo(result);
		return result.toString();
	}
}
