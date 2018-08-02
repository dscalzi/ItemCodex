package com.dscalzi.itemcodexlib.component.adapter;

import java.lang.reflect.Type;

import com.dscalzi.itemcodexlib.component.ItemEntry;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ItemEntryTypeAdapter implements JsonSerializer<ItemEntry>, JsonDeserializer<ItemEntry>{

    @Override
    public ItemEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JsonElement serialize(ItemEntry src, Type typeOfSrc, JsonSerializationContext context) {
        // TODO Auto-generated method stub
        return null;
    }

}
