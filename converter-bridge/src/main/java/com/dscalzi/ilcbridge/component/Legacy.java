package com.dscalzi.ilcbridge.component;

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
