package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
<<<<<<< Updated upstream
=======
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.arm.pseudocode.*;
>>>>>>> Stashed changes

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
        this.setLocation(localEnv.getEnvExp().get(varName.getName()).getLocation());
        this.initialization.verifyInitialization(compiler, this.type.verifyType(compiler), localEnv, currentClass);
        this.varName.verifyExpr(compiler, localEnv, currentClass);
    }



    
    @Override
    public void decompile(IndentPrintStream s) {
        this.type.decompile(s);
        this.varName.decompile(s);
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
        if (this.initialization.initialization()) {
            // Générer le code pour initialiser la variable
            // La normalement on a tout initialisé
            compiler.isVar = true;
            DVal valeur = this.initialization.codeGenExpr(compiler);
            System.out.println("valeur = " + valeur);
            compiler.addRegUn(this.varName.getName().toString(),adresse);


        }
    }

    protected void codegenVarARM(DecacCompiler compiler) {
        VariableDefinition variable = new VariableDefinition(this.type.getDefinition().getType(), this.getLocation());
        // Setoperand ?
        DAddr adresse = compiler.associerAdresseARM();
        variable.setOperand(adresse);
        compiler.addVar(variable,this.varName.getName().toString());
        compiler.addNameVal(this.getLocation(),this.varName.getName().toString());
        compiler.addRegUnARM(this.varName.getName().toString(),adresse);
        compiler.nbrVar++;
        if (this.initialization.initialization()) {
            compiler.isVar = true;
            compiler.isAssign = true;
            DVal valeur = this.initialization.codeGenExprARM(compiler);
            int ID = compiler.getUniqueDataID();
            String line ="";
            if(this.type.getDefinition().getType().isInt()){
                line = "data"+ID+ ": .word " + valeur.toString().substring(1,valeur.toString().length());
            } else if(this.type.getDefinition().getType().isFloat()){
                line = "data"+ID+ ": .float " + valeur.toString().substring(1,valeur.toString().length());
            }
            compiler.addFirstComment(line);
        }
        else{
            int ID = compiler.getUniqueDataID();
            String line = "";
            if (this.type.getDefinition().getType().isInt()) {
                line = "data" + ID + ": .word 0"; // Default for integers
            } else if (this.type.getDefinition().getType().isFloat()) {
                line = "data" + ID + ": .float 0.0"; // Default for floats
            }
            compiler.addFirstComment(line);
        }
        compiler.nbrVar++;
    }

}
