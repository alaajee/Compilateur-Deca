package fr.ensimag.deca.tree;

import java.io.PrintStream;


import fr.ensimag.ima.pseudocode.DVal;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.RegisterOffset;


public class Param extends AbstractParam{
    final private AbstractIdentifier type;
    final private AbstractIdentifier paramName;
    private RegisterOffset register;

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
    protected Type verifyParam(DecacCompiler compiler,EnvironmentExp localEnv,ClassDefinition currentClass) throws ContextualError{
        Type t=this.type.verifyType(compiler);
        
        if (t.isVoid()) {
            throw new ContextualError("A parameter cannot have 'void' as its type", this.type.getLocation());
        }

        ParamDefinition paramDef = new ParamDefinition(t, this.paramName.getLocation());
        try {
            localEnv.declare(this.paramName.getName(), paramDef);
        } catch ( EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Param '" + this.paramName.getName() + "' is already declared ", this.paramName.getLocation());

        }
        this.paramName.verifyExpr(compiler, localEnv, currentClass);
        return t;


    }

    @Override
    protected void codeGenParam(DecacCompiler compiler) {
        this.register = compiler.getRegisterParam();
        compiler.setRegisterOffsets(this.paramName.getName().toString(),this.register.getOffset());
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler,
                                    EnvironmentExp localEnv, ClassDefinition currentClass) {
        return null;
    }

    @Override
    protected  DVal codeGenExpr(DecacCompiler compiler){
        return this.register;
    }

    @Override
    protected  DVal codeGenExprARM(DecacCompiler compiler){
        return this.register;
    }

    @Override
    protected DVal codeGenInstClass(DecacCompiler compiler){
        return this.register;
    }
}