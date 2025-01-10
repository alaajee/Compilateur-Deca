package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RFLOAT;
import fr.ensimag.ima.pseudocode.instructions.RINT;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class ReadFloat extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                Symbol symbolFloat = compiler.createSymbol("float");
                Type typeFloat = compiler.environmentType.getEnvtypes().get(symbolFloat).getType();       
                this.setType(typeFloat);
                return typeFloat;      
            }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readFloat()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        compiler.addInstruction(new RFLOAT());
        GPRegister register = Register.R1;
        GPRegister reg = compiler.associerReg();
        compiler.addInstruction( new LOAD(register, reg));
        return reg;
    }
}
