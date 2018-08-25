@file:Suppress("DEPRECATED_JAVA_ANNOTATION")

package eu.thedarken.wldonate.main.core.locks

import dagger.MapKey

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class TypeKey(val value: Lock.Type)