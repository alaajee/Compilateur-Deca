package fr.ensimag.deca.tree;

import java.io.PrintStream;


import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;

/**
 * @author gl02
 * @date 01/01/2025
 */
public class Initialization extends AbstractInitialization {

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;


    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;

    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
            AbstractExpr exp= this.expression.verifyRValue(compiler, localEnv, currentClass, t);
            this.setExpression(exp);
            
        
    }



    @Override
    public void decompile(IndentPrintStream s) {
       this.expression.decompile(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }

    @Override
    // On vérifie si elle est initalisee ou non
    public boolean initialization(){
        if (this.getExpression() != null) {
            return true;
        }
        return false;
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler){
        if (getExpression().getExpr().equals("instruction")){
            DVal valeur = getExpression().codeGenInit(compiler);
            return valeur;
        }
        else {
            //System.out.println(getExpression());
            compiler.init = true;
            DVal valeur = getExpression().codeGenExpr(compiler);
            return valeur;
        }
    }
    @Override
    public DVal codeGenExprARM(DecacCompiler compiler){
        if (getExpression().getExpr().equals("instruction")){
            DVal valeur = getExpression().codeGenInitARM(compiler);
            return valeur;
        }
        else {
            DVal valeur = getExpression().codeGenExprARM(compiler);
            return valeur;
        }
    }

    @Override
    public void codeGenField(DecacCompiler compiler){
        getExpression().codeGenField(compiler);
    }
}