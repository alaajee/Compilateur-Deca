package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.deca.context.ClassDefinition;
import org.apache.commons.lang.Validate;


public class Param extends AbstractParam{
    final private AbstractIdentifier type;
    final private AbstractIdentifier paramName;
    public Param(AbstractIdentifier type, AbstractIdentifier paramName) {
        Validate.notNull(type);
        Validate.notNull(paramName);
        this.type = type;
        this.paramName = paramName;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        paramName.decompile(s);
        
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        paramName.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        paramName.iter(f);
    }

    @Override
    protected void verifyParam(DecacCompiler compiler,EnvironmentExp localEnv,ClassDefinition currentClass) throws ContextualError{
        Type t=this.type.verifyType(compiler);
        ParamDefinition paramDef = new ParamDefinition(t, this.paramName.getLocation());
        try {
            localEnv.declare(this.paramName.getName(), paramDef);
        } catch ( EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Param '" + this.paramName.getName() + "' is already declared ", this.paramName.getLocation());

        }
        this.paramName.verifyExpr(compiler, localEnv, currentClass);

    }

    @Override
    protected void codeGenParam(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }  
}