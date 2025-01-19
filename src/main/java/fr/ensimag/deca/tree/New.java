package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;
import java.util.LinkedList;

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
        compiler.tas_plein = true;
        GPRegister reg = compiler.associerReg();
        int i = compiler.getTableFields(this.Identifier.getName().getName())+ 1;
        compiler.addInstruction(new NEW(new ImmediateInteger(i),reg));
        compiler.addInstruction(new BOV(new Label("Tas_plein")));
        String name = this.Identifier.getName().getName();

        DAddr adresse = compiler.getTableClassee(name);
        compiler.addInstruction(new LEA(adresse,Register.R0));
        compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(0,reg)));
        compiler.addInstruction(new PUSH(reg));
        compiler.addInstruction(new BSR(new Label("init" + name)));
        compiler.addInstruction(new POP(reg));
        DAddr adresseNouvelle = compiler.getCurrentAdresse();
        compiler.addInstruction(new STORE(reg,adresseNouvelle));
        compiler.libererReg();
        return adresseNouvelle;
    }

    @Override
    protected DVal codeGenInstClass(DecacCompiler compiler, LinkedList<Instruction> lines, GPRegister register){
        return null;
    }

    @Override
    protected DVal codeGenExprARM(DecacCompiler compiler){
        return null;
    }
}