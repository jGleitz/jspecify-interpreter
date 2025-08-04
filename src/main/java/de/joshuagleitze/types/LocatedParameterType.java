package de.joshuagleitze.types;

import lombok.EqualsAndHashCode;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Parameter;

import static de.joshuagleitze.types.TypeFormatting.appendFormatted;
import static de.joshuagleitze.types.TypeFormatting.appendFormattedMarked;

/// A type use by a [Parameter]. For example, `@Nullable String` in `void isFriend(@Nullable String name)`.
@EqualsAndHashCode(callSuper = false)
public final class LocatedParameterType extends BaseLocatedTypeUse {
	private final Parameter parameter;
	@EqualsAndHashCode.Exclude
	private final AnnotatedType parameterType;

	public LocatedParameterType(Parameter parameter) {
		this.parameter = parameter;
		this.parameterType = parameter.getAnnotatedType();
	}

	/// The [Parameter] declaring this type use.
	@Override
	public Parameter getDeclaration() {
		return parameter;
	}

	@Override
	protected AnnotatedType underlyingType() {
		return parameter.getAnnotatedType();
	}


	@Override
	void appendTo(StringBuilder result) {
		appendFormattedMarked(result, parameterType);
		result.append(' ');
		result.append(parameter.getName());
		result.append(" declared by ");
		appendFormatted(result, parameter.getDeclaringExecutable());
	}
}
