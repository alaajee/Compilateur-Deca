package fr.ensimag.deca.codegen;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.SUB;

public class constructeurADD extends constructeur{

    @Override
    public void constructeur(DecacCompiler compiler, DVal op, GPRegister reg){
        compiler.addInstruction(new ADD(op,reg));
    }
}