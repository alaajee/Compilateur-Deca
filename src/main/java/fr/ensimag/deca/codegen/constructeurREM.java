package fr.ensimag.deca.codegen;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.REM;

public class constructeurREM extends constructeur{

    @Override
    public void constructeur(DecacCompiler compiler, DVal op, GPRegister reg){
        compiler.addInstruction(new REM(op,reg));
    }
}