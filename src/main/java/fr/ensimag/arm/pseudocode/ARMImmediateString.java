package fr.ensimag.arm.pseudocode;


import fr.ensimag.ima.pseudocode.Operand;

public class ARMImmediateString extends Operand {
    private String value;

    public ARMImmediateString(String value) {
        super();
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
