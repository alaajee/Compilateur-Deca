package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.arm.pseudocode.ARMGPRegister;
import fr.ensimag.arm.pseudocode.ARMImmediateString;
import fr.ensimag.arm.pseudocode.ARMImmediateInteger;
import fr.ensimag.arm.pseudocode.ARMRegister;
import fr.ensimag.arm.pseudocode.instructions.*;

/**
 * Integer literal
 *
 * @author gl02
 * @date 01/01/2025
 */
public class IntLiteral extends AbstractExpr {
    public int getValue() {
        return value;
    }

    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                Symbol symbolInt = compiler.createSymbol("int");
                Type typeInt = compiler.environmentType.getEnvtypes().get(symbolInt).getType();       
                this.setType(typeInt);
                return typeInt;  
    }


    @Override
    String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
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
    protected DVal codeGenExpr(DecacCompiler compiler) {
        DAddr adresse = compiler.getCurrentAdresse();
        DVal res = new ImmediateInteger(value);
        if (compiler.isVar == true){
            GPRegister reg = compiler.associerReg();
            compiler.addInstruction(new LOAD(res, reg));
            compiler.addInstruction(new STORE(reg, adresse));
            res.isRegistre = true;
            compiler.isVar = false;
            // Il faut liberer le registre
            compiler.libererReg(reg.getNumber());
            return adresse;
        }
        else {
            return res;
        }

    }


    @Override
    public DVal codeGenExprARM(DecacCompiler compiler) {
        System.out.println("im here");
        DVal res = new ARMImmediateInteger(value);
        if(compiler.isVar){
            DAddr adresse = compiler.associerAdresseARM();
            // ARMGPRegister reg = compiler.associerRegARM();
            // compiler.addInstruction(new MOV(reg, res));
            // compiler.addInstruction(new STR(reg, new ARMImmediateString(adresse.toString())));
            // res.isRegistre = true;
            // Il faut liberer le registre
            // compiler.isVar = false;
            // compiler.libererReg(reg.getNumber());
            System.out.println("im here bossssssssss" + adresse);
            return res;
        }
        return res;
    }

    @Override
    public void codeGenInst(DecacCompiler compiler){};

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        compiler.addInstruction(new RFLOAT());
        DVal register = codeGenExpr(compiler);
        compiler.addInstruction(new LOAD(register, Register.R1));
        compiler.addInstruction(new WSTR(new ImmediateString("mok")));
        compiler.addInstruction(new WINT());

<<<<<<< Updated upstream
=======
    @Override
    protected void codeGenPrintARM(DecacCompiler compiler) {
        compiler.print = true;
        int ID = compiler.getUniqueDataID();
        String line = "data" + ID + ": .asciz " + "\"%d\"";
        compiler.addFirstComment(line);
        compiler.addInstruction(new LDR(ARMRegister.R0,new ARMImmediateString("="+"data"+ID)));
        compiler.addInstruction(new MOV(ARMRegister.R1, new ARMImmediateInteger(value)));
        compiler.addInstruction(new BL(new ARMImmediateString("printf")));
    }

    

    public DVal codeGenInit(DecacCompiler compiler){
        compiler.typeAssign = this.getType().toString();
        DVal res = new ImmediateInteger(value);
        return res;
>>>>>>> Stashed changes
    }

    public DVal codeGenInitARM(DecacCompiler compiler){
        compiler.typeAssign = this.getType().toString();
        DVal res = new ARMImmediateInteger(value);
        return res;
    }
}
