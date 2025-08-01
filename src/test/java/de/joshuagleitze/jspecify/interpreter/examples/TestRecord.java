package de.joshuagleitze.jspecify.interpreter.examples;

public record TestRecord() {
	public boolean equals(Object a, Object b) {
		return true;
	}

	public boolean equals(String c) {
		return false;
	}
}
