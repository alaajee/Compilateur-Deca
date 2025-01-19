package fr.ensimag.deca.tree;

import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl02
 * @date 01/01/2025
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclVar declVar : this.getList()) {
            declVar.decompile(s);  // Décompiler chaque déclaration de variable
        }
    }
    

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */    
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {


        Iterator<AbstractDeclVar> iterator = this.iterator(); 

        while (iterator.hasNext()) {
            AbstractDeclVar declVar = iterator.next(); // Récupère l'élément suivant
            declVar.verifyDeclVar(compiler,localEnv,currentClass);
        }
    }

    public void codeGen(DecacCompiler compiler){
        for (AbstractDeclVar declVar : this.getList()) {
            declVar.codegenVar(compiler);
        }
    }

    public void codeGenARM(DecacCompiler compiler){
        // System.out.println("im in ListDeclVar");
        for (AbstractDeclVar declVar : this.getList()) {
            declVar.codegenVarARM(compiler);
        }
    }
}
