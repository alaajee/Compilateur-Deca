package fr.ensimag.arm.pseudocode;

import fr.ensimag.ima.pseudocode.DVal;

public class ARMImmediateFloat extends DVal {
    private float value;

    public ARMImmediateFloat(float value) {
        super();
        this.value = value;
    }

    @Override
    public String toString() {
        return "#" + value;
    }
}
