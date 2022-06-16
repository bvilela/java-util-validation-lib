package com.bvilela.utils;

import com.bvilela.utils.annotation.gson.NotSerialized;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class GsonUtils {
	
	private GsonUtils() {
	}

	public static Gson getGson() {
		ExclusionStrategy strategy = new ExclusionStrategy() {
		    @Override
		    public boolean shouldSkipClass(Class<?> clazz) {
		        return false;
		    }

		    @Override
		    public boolean shouldSkipField(FieldAttributes field) {
		        return field.getAnnotation(NotSerialized.class) != null;
		    }
		};
		return new GsonBuilder().setExclusionStrategies(strategy).create();
	}
	
}
