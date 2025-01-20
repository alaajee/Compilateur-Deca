package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;

/**
 * @author gl02
 * @date 01/01/2025
 */
public class DeclVar extends AbstractDeclVar {


    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
                                 EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type t=this.type.verifyType(compiler);
        VariableDefinition varDef = new VariableDefinition(t, this.varName.getLocation());
        try {
            localEnv.declare(this.varName.getName(), varDef);
        } catch ( EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Variable '" + this.varName.getName() + "' is already declared in this scope", this.varName.getLocation());

        }
        this.initialization.verifyInitialization(compiler, this.type.verifyType(compiler), localEnv, currentClass);
        this.varName.verifyExpr(compiler, localEnv, currentClass);
    }




    @Override
    public void decompile(IndentPrintStream s) {
        this.type.decompile(s);
        s.print(" ");
        this.varName.decompile(s);
        s.print(" ");
        if (this.initialization instanceof Initialization)
        {
            s.print(" = ");
            initialization.decompile(s);
        }
        s.println(";");


    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }


    protected void codegenVar(DecacCompiler compiler) {
        // Je dois normalement generer LOAD valeur , Rx alors je dois initialiser les variables et changer les operandes etcc ?
        // Je dois generer aussi STORE RX , x(GB)
        VariableDefinition variable = new VariableDefinition(this.type.getDefinition().getType(), this.getLocation());
        // Setoperand ?
        DAddr adresse = compiler.associerAdresse();
        variable.setOperand(adresse);
        compiler.addVar(variable,this.varName.getName().toString());
        compiler.addNameVal(this.getLocation(),this.varName.getName().toString());
        compiler.addRegUn(this.varName.getName().toString(),adresse);
        compiler.nbrVar++;

        compiler.typeAssign = this.type.getType().toString();
       //System.out.println("iciiiiiiiiiiiiiiiii"+this.type.getType().toString());
        if (this.initialization.initialization()) {
            // Générer le code pour initialiser la variable
            // La normalement on a tout initialisé
            compiler.isVar = true;
            compiler.isAssign = true;
            // System.out.println(this.initialization);
            DVal valeur = this.initialization.codeGenExpr(compiler);
            compiler.addRegUn(this.varName.getName().toString(),adresse);
        }

    }

}
