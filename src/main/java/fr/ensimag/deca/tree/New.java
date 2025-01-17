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
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type classType = Identifier.verifyType(compiler);
        if (!classType.isClass()) {
            throw new ContextualError("The type '" + classType.getName() + "' is not a class type", this.getLocation());
        }
        ClassDefinition classDef = (ClassDefinition)compiler.environmentType.getEnvtypes().get(this.Identifier.getName());
        if (classDef == null) {
            throw new ContextualError("Class '" + Identifier.getName().getName() + "' is not defined", this.getLocation());
        }
        this.setType(classType);
        return classType;
    }
    

    @Override
    public void decompile(IndentPrintStream s){
        s.print("new ");
        this.Identifier.decompile(s);
        s.print("()");
    }

@Override
public void prettyPrintChildren(PrintStream s, String prefix) {
    if (Identifier != null) {
        Identifier.prettyPrint(s, prefix, true);
    }
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