package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.LinkedList;

import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.Instruction;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;






// Sous-classe pour une simple instruction
public class asm extends AbstractBlock {
    private String asm;

    public asm(String asm) {
        Validate.notNull(asm);
        this.asm = asm;
    }

    @Override
    public void verifyBlock(DecacCompiler compiler, EnvironmentExp localEnv, fr.ensimag.deca.context.ClassDefinition currentClass, Type t) throws ContextualError {
        //nothing to verify
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("asm(\"" + asm + "\");");
    }
    

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        s.println(prefix + "\"" + asm + "\"");
    }
    

    @Override
    protected void iterChildren(TreeFunction f) {
        //nothing to do 
    }

    @Override
    protected void codeGen(DecacCompiler compiler, LinkedList<Instruction> lines){

    }
}
