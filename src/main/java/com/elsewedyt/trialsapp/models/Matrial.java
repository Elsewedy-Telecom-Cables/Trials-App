package com.elsewedyt.trialsapp.models;

public class Matrial {
    private int matrialId;
    private String matrialName;

    // Constructors
    public Matrial() {
    }

    public Matrial(String matrialName) {
        this.matrialName = matrialName;
    }

    public Matrial(int matrialId, String matrialName) {
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
