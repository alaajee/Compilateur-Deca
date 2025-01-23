package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
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
        Label label = compiler.labelMap.get("io_error");
        compiler.addInstruction(new RFLOAT());
        compiler.addInstruction(new BOV(label));
        GPRegister register = Register.R1;
        GPRegister reg = compiler.associerReg();
        compiler.addInstruction( new LOAD(register, reg));
        compiler.libererReg(reg.getNumber());
        compiler.label = true;
        return reg;
    }

    @Override
    protected DVal codeGenExprARM(DecacCompiler compiler){
        ARMGPRegister reg = compiler.associerRegARMD();
        if(!compiler.printfloat){
            String line = "formatfloat" + ": .asciz " + "\"%f\"";
            compiler.addFirstComment(line);
            compiler.printfloat = true;
        }
        int ID = compiler.getUniqueDataID();
        String line = "data" + ID + ": .double 0.0";
        compiler.addFirstComment(line);
        line = ".align 0";
        compiler.addFirstComment(line);

        compiler.addInstruction(new LDR(ARMRegister.R0,new ARMImmediateString("="+"formatfloat")));
        compiler.addInstruction(new LDR(ARMRegister.R1, new ARMImmediateString("=data"+ID)));
        compiler.addInstruction(new BL(new ARMImmediateString("scanf")));
        compiler.addInstruction(new LDR(ARMRegister.R1, new ARMImmediateString("=data"+ID)));
        compiler.addInstruction(new VLDRF64(reg, new ARMImmediateString("[R1]")));
        return reg;
    }
}
