package com.github.thanospapapetrou.xkcd

import java.lang.reflect.Field
import java.lang.reflect.Modifier

trait SetterUtils {
	private static final String MODIFIERS = 'modifiers'

	public void setFinal(final Object object, final String fieldName, final Object fieldValue) {
		final Field field = object.class.getDeclaredField(fieldName)
		field.accessible = true
		final Field modifiers = Field.getDeclaredField(MODIFIERS)
		modifiers.accessible = true
		modifiers.setInt(field, field.modifiers & (~Modifier.FINAL))
		field.set(object, fieldValue)
	}

	public void setStaticFinal(final Class<?> clazz, final String fieldName, final Object fieldValue) {
		final Field field = clazz.getDeclaredField(fieldName)
		field.accessible = true
		final Field modifiers = Field.getDeclaredField(MODIFIERS)
		modifiers.accessible = true
		modifiers.setInt(field, field.modifiers & (~Modifier.FINAL))
		field.set(null, fieldValue)
	}
}
