package de.joshuagleitze.types;

import lombok.experimental.Delegate;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.RecordComponent;
import java.util.List;

import static de.joshuagleitze.types.TypeFormatting.appendFormatted;
import static de.joshuagleitze.types.LocatedTypeUses.componentsOf;

/// A type use by a [RecordComponent]. For example, `@Nullable String` in `record Person(@Nullable String name)`.
public record LocatedRecordComponentType(RecordComponent recordComponent) implements LocatedTypeUse {
	@Delegate
	private AnnotatedType annotatedType() {
		return recordComponent.getAnnotatedType();
	}

	/// The [RecordComponent] declaring this type use.
	@Override
	public RecordComponent declaration() {
		return recordComponent;
	}

	@Override
	public List<LocatedTypeComponent> components() {
		return componentsOf(this, annotatedType());
	}

	@Override
	public @Nullable LocatedOuterType getAnnotatedOwnerType() {
		return LocatedTypeUse.super.getAnnotatedOwnerType();
	}

	@Override
	public String toString() {
		var result = new StringBuilder();
		appendFormatted(result, annotatedType());
		result.append(' ')
				.append(recordComponent.getName())
				.append(" in ")
				.append(recordComponent.getDeclaringRecord().getSimpleName());
		return result.toString();
	}
}
