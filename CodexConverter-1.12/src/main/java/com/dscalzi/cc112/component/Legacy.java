/*
 * CodexConverter112
 * Copyright (C) 2018 Daniel D. Scalzi
 * See LICENSE for license information.
 */
package com.dscalzi.cc112.component;

public class Legacy {

    private short id;
    private byte data;
    
    public Legacy(short id, byte data) {
     
        this.id = id;
        this.data = data;
        
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public byte getData() {
        return data;
    }

    public void setData(byte data) {
        this.data = data;
    }
    
}
