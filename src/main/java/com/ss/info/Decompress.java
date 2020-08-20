package com.ss.info;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NamedTag;

public class Decompress {
	public static JsonObject base64toJson(String encoded) {

		try {
			byte[] buf = Base64.getMimeDecoder().decode(encoded.getBytes("UTF-8"));
			try(ByteArrayInputStream byt = new ByteArrayInputStream(buf)) {
				JsonParser parser = new JsonParser();
				NamedTag result = new NBTDeserializer().fromStream(byt);
				JsonObject json = parser.parse(result.getTag().toString()).getAsJsonObject();
				return json;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
