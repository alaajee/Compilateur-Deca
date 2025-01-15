package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp;






public abstract class AbstractParam extends Tree {




    protected abstract void verifyParam(DecacCompiler compiler,EnvironmentExp localEnv,ClassDefinition currentClass) throws ContextualError;

    protected abstract void codeGenParam(DecacCompiler compiler);

    
}
