package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;






public abstract class AbstractParam extends AbstractExpr {

    protected abstract Type verifyParam(DecacCompiler compiler,EnvironmentExp localEnv,ClassDefinition currentClass) throws ContextualError;

    protected abstract void codeGenParam(DecacCompiler compiler);

    
}
