package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;

public abstract class constructeur {
    public constructeur(){}
    public abstract void constructeur(DecacCompiler compiler, DVal op, GPRegister reg);
}