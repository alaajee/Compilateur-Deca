package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DAddr;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl02
 * @date 01/01/2025
 */
public abstract class AbstractLValue extends AbstractExpr {
    public abstract DAddr getAddr(DecacCompiler compiler);

}
