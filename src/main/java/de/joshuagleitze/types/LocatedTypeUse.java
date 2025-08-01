package de.joshuagleitze.types;

import org.jspecify.annotations.Nullable;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.util.List;

/// A type use that retained information about where it was declared.
///
/// @see AnnotatedType
public interface LocatedTypeUse extends AnnotatedType {
	/// Where this type use was declared.
	AnnotatedElement declaration();

	/// The components of this type use.
	///
	/// @return If this is a [parameterized type][AnnotatedParameterizedType], its type parameters; if this is an [array
	///  type][AnnotatedArrayType], a list containing only its array component type; or else an empty list.
	List<LocatedTypeComponent> components();

	@Override
	default @Nullable LocatedOuterType getAnnotatedOwnerType() {
		var owner = AnnotatedType.super.getAnnotatedOwnerType();
		return owner != null ? new LocatedOuterType(this, owner) : null;
	}
}
