package fr.ensimag.arm.pseudocode;

import fr.ensimag.ima.pseudocode.*;

public class ARMRegisterOffset extends DAddr {
    public int getOffset() {
        return offset;
    }
    private final int offset;
    public ARMRegisterOffset(int offset) {
        super();
        this.offset = offset;
    }
    @Override
    public String toString() {
        return "=" + "data" + offset;
    }
}
