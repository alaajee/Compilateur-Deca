package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.arm.pseudocode.instructions.*;
import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RINT;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class ReadInt extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                Symbol symbolInt = compiler.createSymbol("int");
                Type typeInt = compiler.environmentType.getEnvtypes().get(symbolInt).getType();       
                this.setType(typeInt);
                return typeInt;      
            }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readInt()");
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
        Label label = compiler.labelMap.get("io_error");
        compiler.addInstruction(new RINT());
        compiler.addInstruction(new BOV(label));
        GPRegister register = Register.R1;
        GPRegister reg = compiler.associerReg();
        compiler.addInstruction( new LOAD(register, reg));
        compiler.label = true;
        compiler.libererReg(reg.getNumber());
        return reg;
    }

    @Override
    protected DVal codeGenExprARM(DecacCompiler compiler){
        ARMGPRegister reg = compiler.associerRegARM();
        if(!compiler.printint){
            String line = "formatint" + ": .asciz " + "\"%d\"";
            compiler.addFirstComment(line);
            compiler.printint = true;
        }
        int ID = compiler.getUniqueDataID();
        String line = "data" + ID + ": .word 0";
        compiler.addFirstComment(line);

        compiler.addInstruction(new LDR(ARMRegister.R0,new ARMImmediateString("="+"formatint")));
        compiler.addInstruction(new LDR(ARMRegister.R1, new ARMImmediateString("=data"+ID)));
        compiler.addInstruction(new BL(new ARMImmediateString("scanf")));
        compiler.addInstruction(new LDR(ARMRegister.R1, new ARMImmediateString("=data"+ID)));
        compiler.addInstruction(new LDR(reg, new ARMImmediateString("[R1]")));
        return reg;
    }

}
