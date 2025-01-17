package fr.ensimag.arm.pseudocode;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.arm.pseudocode.*;

public class ARMRegister extends DVal {
    private String name;
    protected ARMRegister(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }


    /**
     * Program Counter
     */
    public static final ARMRegister PC = new ARMRegister("PC");
    /**
     * Link Register
     */
    public static final ARMRegister LR = new ARMRegister("LR");
    /**
     * Stack Pointer
     */
    public static final ARMRegister SP = new ARMRegister("SP");
    /**
     * General Purpose Registers. Array is private because Java arrays cannot be
     * made immutable, use getR(i) to access it.
     */
    private static final ARMGPRegister[] R = initRegisters();
    /**
     * General Purpose Registers
     */
    public static ARMGPRegister getR(int i) {
        return R[i];
    }
    /**
     * Convenience shortcut for R[0]
     */
    public static final ARMGPRegister R0 = R[0];
    /**
     * Convenience shortcut for R[1]
     */
    public static final ARMGPRegister R1 = R[1];

    public static final ARMGPRegister R7 = R[7];


    // static private ARMGPRegister[] initRegisters() {
    //     ARMGPRegister [] res = new ARMGPRegister[13];
    //     for (int i = 0; i <= 12; i++) {
    //         res[i] = new ARMGPRegister("R" + i, i);
    //     }
    //     return res;
    // }

    static private ARMGPRegister[] initRegisters() {
        ARMGPRegister [] res = new ARMGPRegister[16];
        for (int i = 0; i <= 15; i++) {
            res[i] = new ARMGPRegister("R" + i, i);
        }
        return res;
    }
}
