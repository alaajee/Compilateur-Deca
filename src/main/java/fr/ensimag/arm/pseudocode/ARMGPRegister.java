package fr.ensimag.arm.pseudocode;

public class ARMGPRegister extends ARMRegister {
    /**
     * @return the number of the register, e.g. 12 for R12.
     */
    public int getNumber() {
        return number;
    }

    private int number;

    ARMGPRegister(String name, int number) {
        super(name);
        this.number = number;
    }
}
