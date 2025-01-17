package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DVal;

import fr.ensimag.arm.pseudocode.ARMImmediateString;
import fr.ensimag.arm.pseudocode.ARMRegister;
import fr.ensimag.arm.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.StringType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

/**
 * String literal
 *
 * @author gl02
 * @date 01/01/2025
 */
public class StringLiteral extends AbstractStringLiteral {

    @Override
    public String getValue() {
        return value;
    }

    private String value;

    public StringLiteral(String value) {
        Validate.notNull(value);
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
            Type stringType = new StringType(compiler.createSymbol("string"));
            this.setType(stringType);
            return stringType;

    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        compiler.addInstruction(new WSTR(new ImmediateString(value)));
    }

    @Override
    protected void codeGenPrintARM(DecacCompiler compiler) {
        compiler.print = true;
        int ID = compiler.getUniqueDataID();
        String line = "data" + ID + ": .asciz " + value;
        compiler.addFirstComment(line);
        compiler.addInstruction(new LDR(ARMRegister.R0,new ARMImmediateString("="+"data"+ID)));
        compiler.addInstruction(new BL(new ARMImmediateString("printf")));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("\"" + value + "\"");
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
    String prettyPrintNode() {
        return "StringLiteral (" + value + ")";
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler){
        return null;
    }

    @Override
    public DVal codeGenExprARM(DecacCompiler compiler){
        return null;
    }

}
