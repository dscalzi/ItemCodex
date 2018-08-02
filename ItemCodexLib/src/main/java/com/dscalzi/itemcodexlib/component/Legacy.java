/*
 * ItemCodexLib
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.itemcodexlib.component;

public class Legacy {

    // These MUST match the fields declared below.
    public static final String KEY_LEGACY_ID = "id";
    public static final String KEY_LEGACY_DATA = "data";
    
    private short id;
    private short data;
    
    public Legacy(short id, short data) {
     
        this.id = id;
        this.data = data;
        
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public short getData() {
        return data;
    }

    public void setData(short data) {
        this.data = data;
    }
    
}
