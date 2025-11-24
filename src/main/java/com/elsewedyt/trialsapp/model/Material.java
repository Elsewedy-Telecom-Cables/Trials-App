package com.elsewedyt.trialsapp.model;

public class Material {
    private int matrialId;
    private String matrialName;

    // Constructors
    public Material() {
    }

    public Material(String matrialName) {
        this.matrialName = matrialName;
    }

    public Material(int matrialId, String matrialName) {
        this.matrialId = matrialId;
        this.matrialName = matrialName;
    }

    // Getters and Setters
    public int getMatrialId() {
        return matrialId;
    }

    public void setMatrialId(int matrialId) {
        this.matrialId = matrialId;
    }

    public String getMatrialName() {
        return matrialName;
    }

    public void setMatrialName(String matrialName) {
        this.matrialName = matrialName;
    }

    @Override
    public String toString() {
        return matrialName;
    }
}
