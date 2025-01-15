package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.classeNom;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl02
 * @date 01/01/2025
 */
public class DeclClass extends AbstractDeclClass {

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("Not yet supported");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }


    @Override
    protected void codeGenclasse(DecacCompiler compiler) {
        String className = this.getClass().getSimpleName();
        DVal Object = new classeNom("Object");
        DVal dval = new classeNom(className);
        DAddr adresse = compiler.associerAdresse();
        compiler.addInstruction(new LEA(compiler.adresseClasse, Register.R0));
        compiler.adresseClasse = adresse;
        compiler.addInstruction(new STORE(Register.R0, adresse));
        compiler.addInstruction(new LOAD(Object,Register.R0));
        compiler.addInstruction(new STORE(Register.R0, compiler.associerAdresse()));
        compiler.addInstruction(new LOAD(new classeNom(className),Register.R0));
        compiler.addInstruction(new STORE(Register.R0,compiler.associerAdresse()));
    }
}
