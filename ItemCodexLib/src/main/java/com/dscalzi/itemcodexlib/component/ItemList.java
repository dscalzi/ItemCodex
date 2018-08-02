package com.dscalzi.itemcodexlib.component;

import java.util.List;

public class ItemList {

    private String version;
    private List<ItemEntry> items;
    
    public ItemList(String version, List<ItemEntry> entries) {
        
        this.version = version;
        this.items = entries;
        
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<ItemEntry> getItems() {
        return items;
    }

    public void setItems(List<ItemEntry> entries) {
        this.items = entries;
    }
    
}
