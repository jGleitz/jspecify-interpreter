package de.joshuagleitze.types;

import lombok.experimental.UtilityClass;

import java.lang.reflect.*;

@UtilityClass
public class TypeFormatting {
	static void appendFormatted(StringBuilder result, Executable executable) {
		if (executable instanceof Method method) {
			appendFormatted(result, method);
		} else if (executable instanceof Constructor<?> constructor) {
			appendFormatted(result, constructor);
		} else {
			result.append(executable);
		}
	}

	static void appendFormatted(StringBuilder result, Constructor<?> constructor) {
		result.append(constructor.getDeclaringClass().getSimpleName());
		appendParameters(result, constructor);
	}

	static void appendFormatted(StringBuilder result, Method method) {
		result.append(method.getDeclaringClass().getSimpleName())
				.append('.')
				.append(method.getName());
		appendParameters(result, method);
	}

	static void appendParameters(StringBuilder result, Executable executable) {
		var parameters = executable.getParameters();
		result.append("(");
		for (var i = 0; i < parameters.length; i++) {
			if (i > 0) {
				result.append(", ");
			}
			result.append(parameters[i].getType().getSimpleName());
		}
		result.append(')');
	}

	static void appendFormatted(StringBuilder result, AnnotatedType type) {
		if (type instanceof AnnotatedParameterizedType parameterized) {
			appendFormatted(result, parameterized);
		} else if (type instanceof AnnotatedArrayType array) {
			appendFormatted(result, array);
		} else if (type instanceof AnnotatedTypeVariable typeVariable) {
			appendFormatted(result, typeVariable);
		} else if (type instanceof AnnotatedWildcardType wildcard) {
			appendFormatted(result, wildcard);
		} else if (type instanceof BaseLocatedTypeUse baseLocatedTypeUse) {
			baseLocatedTypeUse.appendTo(result);
		} else {
			appendFormattedAnnotationsAndSeparator(result, type);
			appendOuterType(result, type);
			appendFormatted(result, type.getType());
		}
	}

	static void appendFormatted(StringBuilder result, AnnotatedParameterizedType type) {
		appendFormattedAnnotationsAndSeparator(result, type);
		appendOuterType(result, type);
		appendFormatted(result, ((ParameterizedType) type.getType()).getRawType());
		var parameters = type.getAnnotatedActualTypeArguments();
		if (parameters.length > 0) {
			result.append('<');
			for (int i = 0; i < parameters.length; i++) {
				if (i > 0) {
					result.append(", ");
				}
				appendFormatted(result, parameters[i]);
			}
			result.append('>');
		}
	}

	static void appendFormatted(StringBuilder result, AnnotatedArrayType type) {
		appendFormatted(result, type.getAnnotatedGenericComponentType());
		result.append(' ');
		appendFormattedAnnotationsAndSeparator(result, type);
		result.append("[]");
	}

	static void appendFormatted(StringBuilder result, AnnotatedTypeVariable type) {
		appendFormattedAnnotationsAndSeparator(result, type);
		result.append(((TypeVariable<?>) type.getType()).getName());
	}

	static void appendFormatted(StringBuilder result, AnnotatedWildcardType type) {
		appendFormattedAnnotationsAndSeparator(result, type);
		result.append('?');
		appendBounds(result, "extends", type.getAnnotatedUpperBounds());
		appendBounds(result, "super", type.getAnnotatedLowerBounds());
	}

	static void appendFormattedAnnotationsAndSeparator(StringBuilder result, AnnotatedType type) {
		for (var annotation : type.getAnnotations()) {
			result.append('@').append(annotation.annotationType().getSimpleName());
			var methods = annotation.annotationType().getDeclaredMethods();
			if (methods.length > 0) {
				result.append('(');
				for (int j = 0; j < methods.length; j++) {
					if (j > 0) {
						result.append(", ");
					}
					result.append(methods[j].getName()).append('=');
					try {
						var value = methods[j].invoke(annotation);
						result.append(value);
					} catch (ReflectiveOperationException e) {
						result.append("ERROR: ").append(e.getMessage());
					}
				}
				result.append(')');
			}
			result.append(' ');
		}
	}

	static void appendOuterType(StringBuilder result, AnnotatedType type) {
		var outerType = type.getAnnotatedOwnerType();
		if (outerType != null) {
			appendFormatted(result, outerType);
			result.append('.');
		}
	}

	static void appendBounds(StringBuilder result, String keyword, AnnotatedType[] bounds) {
		if (bounds.length > 0) {
			result.append(' ').append(keyword).append(' ');
			for (int i = 0; i < bounds.length; i++) {
				if (i > 0) {
					result.append(" & ");
				}
				appendFormatted(result, bounds[i]);
			}
		}
	}

	static void appendFormatted(StringBuilder result, Type type) {
		if (type instanceof Class<?> clazz) {
			appendFormatted(result, clazz);
		} else {
			result.append(type.getTypeName());
		}
	}

	static void appendFormatted(StringBuilder result, Class<?> type) {
		result.append(type.getSimpleName());
	}
}
