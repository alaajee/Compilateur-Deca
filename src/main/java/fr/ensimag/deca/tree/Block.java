package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.*;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.Tree;



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
        instructions.verifyListInst(compiler, localEnv, currentClass,type);
        listVar.verifyListDeclVariable(compiler, localEnv, currentClass);
        
        
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}