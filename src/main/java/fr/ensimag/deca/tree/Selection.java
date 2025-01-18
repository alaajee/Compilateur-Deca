package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.AbstractExpr;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.AbstractLValue;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;

import java.io.PrintStream;


/**
 * @author gl02
 * @date 01/01/2025
 */
public class Selection extends AbstractLValue {

    private AbstractExpr expr;  
    private AbstractIdentifier name;  // Le nom du champ

    public Selection(AbstractExpr expr, AbstractIdentifier name) {
        this.expr = expr;
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        throw new ContextualError("pas encore implementey", getLocation());

    }

    // Génération du code pour l'instruction
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    }

    @Override
    public void decompile(IndentPrintStream s) {
        expr.decompile(s);  
        s.print('.');  // Accès au champ
        name.decompile(s);  
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);  // Affiche l'expression
        name.prettyPrint(s, prefix, true);   // Affiche le nom du champ
    }

    // Fonction d'itération sur les enfants de l'expression
    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iter(f);  // Itère sur les enfants de l'expression
        name.iter(f);  // Itère sur les enfants du nom du champ
    }

    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        return null;
    }

    @Override
    protected DVal codeGenExprARM(DecacCompiler compiler){
        return null;
    }

    @Override
    public DAddr getAddr(DecacCompiler compiler){
        return null;
    }

}
