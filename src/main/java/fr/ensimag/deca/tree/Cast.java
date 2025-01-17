package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.TreeFunction;
import fr.ensimag.ima.pseudocode.DVal;

import java.io.PrintStream;

public class Cast extends AbstractExpr {
    private AbstractIdentifier type;
    private AbstractExpr expr;

    public Cast(AbstractIdentifier type, AbstractExpr expr) {
        this.type = type;
        this.expr = expr;
    }


    @Override
    public String toString() {
        return type.toString() + " " + expr;
    }

    @Override
    public  Type verifyExpr(DecacCompiler compiler,
                            EnvironmentExp localEnv, ClassDefinition currentClass)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decompile(IndentPrintStream s){}

    @Override
    public void prettyPrintChildren(PrintStream s, String name){

    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }


    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        return null;
    }
}
