package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SEQ;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.log4j.Logger;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        throw new UnsupportedOperationException("not yet implemented");
        // LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    protected void codeGenClasses(DecacCompiler compiler){
        System.out.println(getList());
        if (getList().size() > 0) {
            DAddr adresseUne = new RegisterOffset(1, Register.GB);
            DAddr adresseDeux = new RegisterOffset(2, Register.GB);
            DVal dval = new NullOperand();
            compiler.addInstruction(new LOAD(dval,Register.R0));
            compiler.addInstruction(new STORE(Register.R0,adresseUne));
            dval = new classeNom("Object");
            compiler.addInstruction(new LOAD(dval,Register.R1));
            compiler.addInstruction(new STORE(Register.R1,adresseDeux));
            compiler.adresseClasse = adresseUne;
        }
        for (AbstractDeclClass c : getList()) {
            c.codeGenclasse(compiler);
        }
    };

}
