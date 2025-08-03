package de.joshuagleitze.jspecify.interpreter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import java.lang.reflect.AnnotatedElement;

/// bridges the fact that [Package] doesn’t expose its containing module publicly.
@RequiredArgsConstructor
class PackageInModule implements AnnotatedElement {
	private final @Delegate Package pkg;
	@Getter
	private final Module module;
}
