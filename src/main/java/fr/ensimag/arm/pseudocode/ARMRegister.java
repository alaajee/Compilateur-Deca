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

    private static final ARMGPRegister[] S = initRegistersS();

    private static final ARMGPRegister[] D = initRegistersD();
    /**
     * General Purpose Registers
     */
    public static ARMGPRegister getR(int i) {
        return R[i];
    }

    public static ARMGPRegister getD(int i) {
        return D[i];
    }

    public static ARMGPRegister getS(int i) {
        return S[i];
    }
    /**
     * Convenience shortcut for R[0]
     */
    public static final ARMGPRegister R0 = R[0];
    /**
     * Convenience shortcut for R[1]
     */
    public static final ARMGPRegister R1 = R[1];

    public static final ARMGPRegister R3 = R[3];

    public static final ARMGPRegister R7 = R[7];

    public static final ARMGPRegister S0 = S[0];

    public static final ARMGPRegister S1 = S[1];

    public static final ARMGPRegister S2 = S[2];

    public static final ARMGPRegister D0 = D[0];

    public static final ARMGPRegister D1 = D[1];

    public static final ARMGPRegister D2 = D[2];


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

    static private ARMGPRegister[] initRegistersS() {
        ARMGPRegister [] res = new ARMGPRegister[32];
        for (int i = 0; i <= 31; i++) {
            res[i] = new ARMGPRegister("S" + i, i);
        }
        return res;
    }

    static private ARMGPRegister[] initRegistersD() {
        ARMGPRegister [] res = new ARMGPRegister[16];
        for (int i = 0; i <= 15; i++) {
            res[i] = new ARMGPRegister("D" + i, i);
        }
        return res;
    }
}
