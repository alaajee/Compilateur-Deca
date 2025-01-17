package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 *
 * @author Gl02
 */
public class New extends AbstractExpr {

    final private AbstractIdentifier Identifier;

    public New(AbstractIdentifier identifier) {
        Validate.notNull(identifier);
        this.Identifier = identifier;
    }

    public AbstractIdentifier getIdentifier() {
        return this.Identifier;
    }

    @Override
    public  Type verifyExpr(DecacCompiler compiler,
                                    EnvironmentExp localEnv, ClassDefinition currentClass)
    {
            throw new UnsupportedOperationException();
    }

    @Override
    public void decompile(IndentPrintStream s){
        s.print("new ");
        this.Identifier.decompile(s);
        s.print("()");
    }

    @Override
    public void prettyPrintChildren(PrintStream s,String name){

    }

    @Override
    protected void iterChildren(TreeFunction f) {
        Identifier.iter(f);
    }


    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        return null;
    }
}