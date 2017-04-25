package com.github.thanospapapetrou.xkcd.impl.cdi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;

/**
 * Qualifier specifying a parameter to be configured via selecting among various concrete implementations.
 * 
 * @author thanos
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface Selector {
	/**
	 * Literal of @Selector annotation.
	 * 
	 * @author thanos
	 */
	public static class SelectorLiteral extends AnnotationLiteral<Selector> {
		private static final long serialVersionUID = 0L;
	}
}
