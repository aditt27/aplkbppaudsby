package com.adibu.aplk.grid;

public class GridModel {
    private int imageId;
    private int nameId;

    public GridModel(int imageId, int nameId) {
        this.imageId = imageId;
        this.nameId = nameId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }
}
