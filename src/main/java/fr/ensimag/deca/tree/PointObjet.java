package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;

import java.io.PrintStream;

public class PointObjet extends AbstractExpr {
    private AbstractExpr instance;
    private CallMethod method;

    public PointObjet(AbstractExpr instance, CallMethod method) {
        this.instance = instance;
        this.method = method;
    }

    @Override
    public String toString() {
        return "PointObjet [instance=" + instance + ", method=" + method + "]";
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler,
                           EnvironmentExp localEnv, ClassDefinition currentClass)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decompile(IndentPrintStream s){
        instance.decompile(s);
        s.print(".");
        method.decompile(s);
    }

    @Override
    public void prettyPrintChildren(PrintStream s, String name){
        instance.prettyPrint(s, name,false);
        method.prettyPrint(s,name,false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        instance.iterChildren(f);
        method.iterChildren(f);
    }


    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        return null;
    }

    @Override
    protected DVal codeGenExprARM(DecacCompiler compiler){
        return null;
    }
}