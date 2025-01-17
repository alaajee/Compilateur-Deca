package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import java.util.LinkedList;

import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import org.apache.commons.lang.Validate;



public class Block extends AbstractBlock {
    private ListInst instructions;
    private ListDeclVar listVar;

    public Block(ListInst instructions,ListDeclVar listVar
    ) {
        Validate.notNull(instructions);
        Validate.notNull(listVar);
        this.instructions = instructions;
        this.listVar = listVar;
    }

    @Override
    public void verifyBlock(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,Type type)
            throws ContextualError {
        System.out.println("moi");
        instructions.verifyListInst(compiler, localEnv, currentClass,type);
        listVar.verifyListDeclVariable(compiler, localEnv, currentClass);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent(); 
        listVar.decompile(s);
    
        instructions.decompile(s);
    
        s.unindent(); 
        s.println("}");
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        listVar.prettyPrint(s, prefix, false);
        instructions.prettyPrint(s, prefix, true); 
    }
    

    @Override
    protected void iterChildren(TreeFunction f) {
        listVar.iter(f);
        instructions.iter(f);   
    }

    @Override
    protected void codeGen(DecacCompiler compiler, LinkedList<Instruction> lines){
        compiler.regPush = 0;
        //compiler.registeres.clear();
        instructions.codeGenInstClasse(compiler,lines);
        compiler.addInstruction(new RTS());
    }
}