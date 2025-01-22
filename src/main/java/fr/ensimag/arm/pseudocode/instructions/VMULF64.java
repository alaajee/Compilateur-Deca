package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.ARMGPRegister;
import fr.ensimag.arm.pseudocode.ARMTernaryInstructionVMULF64;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.*;

public class VMULF64 extends ARMTernaryInstructionVMULF64 {
    
    public VMULF64(ARMGPRegister op1, DVal op2,  DVal op3) {
        super(op1, op2, op3);
    }
}