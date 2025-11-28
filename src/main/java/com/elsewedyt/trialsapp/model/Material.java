package com.elsewedyt.trialsapp.model;

public class Material {
    private int materialId;
    private String materialName;

    // Constructors
    public Material() {
    }

    public Material(String matrialName) {
        this.materialName = matrialName;
    }

    public Material(int matrialId, String matrialName) {
        this.materialId = matrialId;
        this.materialName = matrialName;
    }

    // Getters and Setters
    public int getMaterialId() {
        return materialId;
    }

    public void setMatrialId(int matrialId) {
        this.materialId = matrialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String matrialName) {
        this.materialName = matrialName;
    }

    @Override
    public String toString() {
        return materialName;
    }
}
