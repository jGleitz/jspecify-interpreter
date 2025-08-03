package de.joshuagleitze.types;

import org.jspecify.annotations.Nullable;

import java.lang.reflect.*;

/// A type use that retained information about where it was declared.
///
/// @see AnnotatedType
public interface LocatedTypeUse extends AnnotatedType {
	/// Where this type use was declared.
	AnnotatedElement getDeclaration();

	/// The components of this type use.
	///
	/// @return If this is a [parameterized type][AnnotatedParameterizedType], its type parameters; if this is an [array
	///  type][AnnotatedArrayType], an array containing only its array component type; or else an empty array.
	LocatedTypeComponent[] getComponents();

	@Override
	@Nullable
	LocatedOuterType getAnnotatedOwnerType();
}
