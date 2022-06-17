package com.bvilela.utils.annotation.gson;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.bvilela.utils.GsonUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

class TestNotSerialized {

	@Getter
	@Setter
	@NoArgsConstructor
	private class myDTO1 {
		private String name;
		@NotSerialized
		private String nickName;
		@NotSerialized
		private int age;
	}
	
	@Test
	void testJsonFieldNotSerialized() {
		var dto = new myDTO1();
		dto.setName("nameValue");
		dto.setNickName("nickNameValue");
		dto.setAge(20);
		var json = GsonUtils.getGson().toJson(dto);
		assertEquals("{\"name\":\"nameValue\"}", json);
		assertEquals("nameValue", dto.getName());
		assertEquals("nickNameValue", dto.getNickName());
		assertEquals(20, dto.getAge());
	}
	
	
	@Getter
	@Setter
	@NoArgsConstructor
	private class myDTO2 {
		private String name;
		private String nickName;
	}
	
	@Test
	void testJsonAllFieldsSerialized() {
		var dto = new myDTO2();
		dto.setName("nameValue");
		dto.setNickName("nickNameValue");
		var json = GsonUtils.getGson().toJson(dto);
		assertEquals("{\"name\":\"nameValue\",\"nickName\":\"nickNameValue\"}", json);
		assertEquals("nameValue", dto.getName());
		assertEquals("nickNameValue", dto.getNickName());
	}
	
}
