package fr.ensimag.arm.pseudocode;

import fr.ensimag.ima.pseudocode.DVal;

public class ARMImmediateInteger extends DVal {
    private int value;

    public ARMImmediateInteger(int value) {
        super();
        this.value = value;
        if (value == 0) {
            this.isNull = true;
        }
    }



    @Override
    public String toString() {
        return "#" + value;
    }


}
