package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.AbstractInst;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WSTR;


import java.io.PrintStream;
import java.util.LinkedList;


public class Return extends AbstractInst {
    private AbstractExpr expr; 


    public Return(AbstractExpr expr) {
        if (expr == null) {
            throw new IllegalArgumentException("Expression in return cannot be null");
        }
        this.expr = expr;
    }


    public AbstractExpr getExpr() {
        return expr;
    }


    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) 
            throws ContextualError {
        if (returnType.isVoid()) {
            throw new ContextualError("Cannot use 'return' in a method with void return type", this.getLocation());
        }

        this.expr = this.expr.verifyRValue(compiler, localEnv, currentClass, returnType);
        if (!this.expr.getType().sameType(returnType)) {
            throw new ContextualError("Return type mismatch: expected " + returnType + " but found " + this.expr.getType(), 
                                      this.expr.getLocation());
        }
    }


    @Override
    protected  void codeGenInst(DecacCompiler compiler) {
        //compiler.addInstruction(new WSTR("hh"));
    }

    @Override
    protected  void codeGenInstARM(DecacCompiler compiler) {
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        expr.decompile(s);
        s.print(";");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, true);
    }


    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iter(f);
    }


    @Override
    protected  DVal codeGenInstClass(DecacCompiler compiler, LinkedList<Instruction> lines) {
        GPRegister register = compiler.associerReg();
        compiler.addInstruction(new LOAD(new RegisterOffset(-2,Register.LB),register));
        compiler.addInstruction(new LOAD(new RegisterOffset(1,register),register));
        compiler.addInstruction(new LOAD(register,Register.R0));
        return null;
    }
}
