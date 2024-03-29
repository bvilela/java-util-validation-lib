package br.com.bvilela.lib.utils.annotation.gson;

import br.com.bvilela.lib.utils.GsonUtils;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The annotated element will not be serialized to gson.toJson(dto). I need to get the Gson() by the
 * AnnotationUtils.getGson() method.
 *
 * <p>{@link GsonUtils#getGson()}
 *
 * @author Bruno Vilela
 * @since 0.0.1
 */
@Retention(RUNTIME)
@Target(FIELD)
@Documented
public @interface NotSerialized {}
