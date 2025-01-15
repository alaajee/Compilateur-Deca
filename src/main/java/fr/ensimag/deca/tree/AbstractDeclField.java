package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;






public abstract class AbstractDeclField extends Tree {




    protected abstract void verifyDeclField(DecacCompiler compiler,ClassDefinition currentClass,int index) throws ContextualError;
    protected abstract void codeGenField(DecacCompiler compiler);
    protected abstract void verifyinitField(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError ;

    
}
