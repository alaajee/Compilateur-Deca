package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.NullType;
import fr.ensimag.deca.context.StringType;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.DVal;
import org.apache.commons.lang.Validate;

/**
 * Null literal (representing the null value)
 *
 * @author gl02
 * @date 01/01/2025
 */
public class NullLiteral extends AbstractExpr {

    private final String value = "null";


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type NullType = new NullType(compiler.createSymbol("null"));
            this.setType(NullType);
            return NullType;
    }
    

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        // Générer l'instruction pour imprimer le mot "null" en tant que chaîne de caractères.
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(value);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // Node leaf, rien à itérer
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // Node leaf, rien à imprimer
    }
    
    @Override
    String prettyPrintNode() {
        return "NullLiteral";
    }

    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        return null;
    }
}
