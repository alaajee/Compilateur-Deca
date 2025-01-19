package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.ima.pseudocode.DVal;


public abstract class AbstractDeclField extends Tree {




    protected abstract void verifyDeclField(DecacCompiler compiler,ClassDefinition currentClass,int index) throws ContextualError;
    protected abstract DVal codeGenField(DecacCompiler compiler);
    protected abstract void verifyinitField(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError ;

    protected abstract String getName();
}
