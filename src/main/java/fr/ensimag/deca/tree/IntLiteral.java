package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.LinkedList;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

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


        DVal res = new ImmediateInteger(value);
        compiler.typeAssign = getType().toString();
        if (compiler.isVar == true){
            // compiler.addInstruction(new WSTR("hh"));
            DAddr adresse = compiler.getCurrentAdresse();
            System.out.println("adresse int" + adresse.toString());
            GPRegister reg = compiler.associerReg();
            compiler.addInstruction(new LOAD(res, reg));
            compiler.addInstruction(new STORE(reg, adresse));
            res.isRegistre = true;
            // Il faut liberer le registre
            compiler.isVar = false;
            compiler.libererReg(reg.getNumber());
            return adresse;
        }
        else {
            return res;
        }

    }

    @Override
    public void codeGenInst(DecacCompiler compiler){};

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal register = codeGenExpr(compiler);
        compiler.addInstruction(new LOAD(register, Register.R1));
        compiler.addInstruction(new WINT());
    }

    public DVal codeGenInit(DecacCompiler compiler){
        compiler.typeAssign = this.getType().toString();
        DVal res = new ImmediateInteger(value);
        return res;
    }

    public void codeGenField(DecacCompiler compiler){
        compiler.addInstruction(new LOAD(new ImmediateInteger(value), Register.R0));
    }

    @Override
    protected DVal codeGenInstClass(DecacCompiler compiler, LinkedList<Instruction> lines, GPRegister register){
        GPRegister reg = compiler.associerReg();
        DVal res = new ImmediateInteger(value);
        compiler.addInstruction(new LOAD(res, reg));
        return reg;
    }
}
