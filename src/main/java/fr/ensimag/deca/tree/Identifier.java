package fr.ensimag.deca.tree;

<<<<<<< Updated upstream
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import org.apache.commons.lang.Validate;
=======
>>>>>>> Stashed changes

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
<<<<<<< Updated upstream
=======
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
>>>>>>> Stashed changes

/**
 * Deca Identifier
 *
 * @author gl02
 * @date 01/01/2025
 */
public class Identifier extends AbstractIdentifier {
    
    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public 
    Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
                ExpDefinition definitionExp=localEnv.get(name);
                if(definitionExp == null){
                    throw new ContextualError("Identifier '" + this.name + "' is not defined", this.getLocation());
                }
                if(!(definitionExp instanceof VariableDefinition)){
                    throw new ContextualError("Identifier '" + this.name + "' is not a variable", this.getLocation());
                }

                this.setDefinition(definitionExp);
                this.setType(definitionExp.getType());
                this.setLocation(localEnv.getEnvExp().get(name).getLocation());
                return definitionExp.getType();            
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        TypeDefinition definitionT = compiler.environmentType.getEnvtypes().get(this.name);

        if (definitionT == null)
        {
            throw new ContextualError("Identifier '" + this.name + "' is not defined", this.getLocation());
        }
        this.setDefinition(definitionT); 
        this.setType(definitionT.getType());
        return definitionT.getType();
        
    }
    
    
    private Definition definition;


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }

        else 
        {
            System.out.println("lllll");
        }
    }

    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        String name = getName().toString();
        DAddr reg = compiler.getRegUn(name);
        System.out.println("reg: " + reg);
        return reg;

    }

    @Override
    protected DVal codeGenExprARM(DecacCompiler compiler) {
        String name = getName().toString();
        DAddr reg = compiler.getRegUnARM(name);
        return reg;
        // System.out.println("im in Identifier");
        // return null;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        String name = getName().toString();
        DAddr register = compiler.getRegUn(name);
        compiler.addInstruction(new LOAD(register, Register.R1));
        compiler.addInstruction(new WINT());

    }

    @Override
    protected void codeGenPrintARM(DecacCompiler compiler) {
        System.out.println("im in Identifier print ARM");
        compiler.print = true;
        String name = getName().toString();
        DAddr register = compiler.getRegUnARM(name);
        if(getType().isInt()){
            if(!compiler.printint){
                String line = "formatint" + ": .asciz " + "\"%d\"";
                compiler.addFirstComment(line);
                compiler.printint = true;
            }
            compiler.addInstruction(new LDR(ARMRegister.R0,new ARMImmediateString("="+"formatint")));
            compiler.addInstruction(new LDR(ARMRegister.R1, register));
            compiler.addInstruction(new LDR(ARMRegister.R1, new ARMImmediateString("[R1]")));
            compiler.addInstruction(new BL(new ARMImmediateString("printf")));
        } else if (getType().isFloat()){
            if(!compiler.printfloat){
                String line = "formatfloat" + ": .asciz " + "\"%f\"";
                compiler.addFirstComment(line);
                compiler.printfloat = true;
            };
            compiler.addInstruction(new LDR(ARMRegister.R0,new ARMImmediateString("="+"formatfloat")));
            compiler.addInstruction(new LDR(ARMRegister.R1, register));
            compiler.addInstruction(new LDR(ARMRegister.R1, new ARMImmediateString("[R1]")));
            compiler.addInstruction(new BL(new ARMImmediateString("printf")));
        }
    }

    @Override
    public DAddr getAddr(DecacCompiler compiler) {
        String name = getName().toString();
        VariableDefinition variableDefinition = compiler.getVarTab().get(name);
        DAddr adresse = variableDefinition.getOperand();
        return adresse;
    }
}