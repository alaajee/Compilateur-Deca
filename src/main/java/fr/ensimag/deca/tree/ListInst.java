package fr.ensimag.deca.tree;

import java.util.Iterator;
import java.util.LinkedList;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.Instruction;

/**
 * 
 * @author gl02
 * @date 01/01/2025
 */
public class ListInst extends TreeList<AbstractInst> {

    /**
     * Implements non-terminal "list_inst" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv corresponds to "env_exp" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     * @param returnType
     *          corresponds to "return" attribute (void in the main bloc).
     */   
     
    public void verifyListInst(DecacCompiler compiler, EnvironmentExp localEnv,
    ClassDefinition currentClass, Type returnType)
    throws ContextualError {
        Iterator<AbstractInst> iterator = this.iterator(); 

        while (iterator.hasNext()) {
            AbstractInst instruction = iterator.next(); 
            instruction.verifyInst(compiler, localEnv, currentClass, returnType);
        }
    }

    public void codeGenListInst(DecacCompiler compiler) {
        for (AbstractInst i : getList()) {
            i.codeGenInst(compiler);
        }
    }

    public void codeGenListInstARM(DecacCompiler compiler) {
        for (AbstractInst i : getList()) {
            i.codeGenInstARM(compiler);
        }
        compiler.addFirstComment(".section .data");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractInst i : getList()) {
            i.decompileInst(s);
            s.println();
        }
    }


    public void addAll(ListInst other) {
        if (other != null) {
            for (AbstractInst inst : other.getList()) {
                add(inst);
            }
        }
    }

    protected void codeGenInstClasse(DecacCompiler compiler, LinkedList<Instruction> lines){
        for (AbstractInst inst : getList()) {
            inst.codeGenInstClass(compiler,lines);
        }
    }
}