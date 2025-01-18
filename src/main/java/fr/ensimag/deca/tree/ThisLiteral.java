package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;

import java.io.PrintStream;

/**
 * This literal (representing the current object in an instance method)
 *
 * @author gl02
 * @date 01/01/2025
 */
public class ThisLiteral extends AbstractExpr {

    public ThisLiteral() {
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // 'this' renvoie le type de la classe dans laquelle il est utilisé
        if (currentClass == null) {
            throw new ContextualError("Cannot use 'this' outside of a class context.", null);
        }
        return currentClass.getType();  // Renvoie le type de la classe courante (ex: ClassType)
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        // 'this' fait référence à l'objet actuel dans une classe
        // Ici, vous pouvez insérer du code pour générer l'adresse de l'objet, 
        // typiquement dans un registre ou en utilisant un pointeur vers l'objet courant.
        // Cela dépend de votre architecture, voici une instruction simplifiée :
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("this");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // 'this' n'a pas d'enfants (nœud feuille)
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // 'this' n'a pas d'enfants (nœud feuille)
    }

    @Override
    String prettyPrintNode() {
        return "ThisLiteral";
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
