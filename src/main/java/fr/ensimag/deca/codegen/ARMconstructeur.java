package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.arm.pseudocode.ARMGPRegister;

public abstract class ARMconstructeur {
    public ARMconstructeur(){}
    public abstract void ARMconstructeur(DecacCompiler compiler, ARMGPRegister op1, DVal op2,  DVal op3);
}