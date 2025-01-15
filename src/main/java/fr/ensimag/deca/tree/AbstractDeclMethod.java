package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;






public abstract class AbstractDeclMethod extends Tree {

    protected abstract void verifyDeclMethod(DecacCompiler compiler,EnvironmentExp localEnv,ClassDefinition currentClass) throws ContextualError;
    protected abstract void verifyBlockMethod(DecacCompiler compiler,EnvironmentExp localEnv,ClassDefinition currentClass) throws ContextualError;
    protected abstract void codeGenMethod(DecacCompiler compiler,String className);

    
}
