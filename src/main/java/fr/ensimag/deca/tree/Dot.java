package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.LinkedList;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Instruction;

public class Dot extends AbstractLValue{
    private AbstractExpr left;
    private AbstractIdentifier right;


    public Dot(AbstractExpr expr, AbstractIdentifier name) {
        this.left=expr;
        this.right=name;
    }

    @Override
    public String toString() {
        return left.toString()+" "+right.toString();
    }


@Override
public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
        throws ContextualError {
    // Vérification du type de gauche
    Type leftType = left.verifyExpr(compiler, localEnv, currentClass);
    if (!leftType.isClass()) {
        throw new ContextualError("The left-hand side of '.' must be a class type.", left.getLocation());
    }

    // Récupérer la définition de la classe gauche
    ClassDefinition leftClassDef = (ClassDefinition) compiler.environmentType.getEnvtypes().get(leftType.getName());
    if (leftClassDef == null) {
        throw new ContextualError("The class '" + leftType.getName() + "' is not defined.", left.getLocation());
    }

    // Rechercher le membre dans la classe actuelle et ses superclasses
    ClassDefinition current = leftClassDef;
    ExpDefinition memberDef = null;

    while (current != null) {
        memberDef = current.getMembers().getEnvExp().get(right.getName());
        if (memberDef != null) {
            // Vérification de visibilité pour `protected`
            if (memberDef instanceof FieldDefinition) {
                FieldDefinition fieldDef = (FieldDefinition) memberDef;

                if (fieldDef.getVisibility() == Visibility.PROTECTED) {
                    // Vérifier si currentClass est une sous-classe via ClassType.isSubClassOf
                    ClassType leftClassType = (ClassType) leftType;
                    if(currentClass==null){
                        throw new ContextualError(
                            "The field '" + right.getName() + "' is protected and cannot be accessed from Main",
                            right.getLocation()
                        );
                    }
                    if (!leftClassType.isSubClassOf(currentClass.getType()) && !leftClassType.sameType(currentClass.getType())) {
                        throw new ContextualError(
                            "The field '" + right.getName() + "' is protected and cannot be accessed from the current context.",
                            right.getLocation()
                        );
                    }
                }
            }
            break; // Champ trouvé
        }
        current = current.getSuperClass(); // Passer à la superclasse
    }

    if (memberDef == null) {
        throw new ContextualError(
            "The member '" + right.getName() + "' is not defined in class '" 
            + leftClassDef.getType().getName() + "' or its superclasses.",
            right.getLocation()
        );
    }

    if (!(memberDef instanceof FieldDefinition)) {
        throw new ContextualError(
            "The member '" + right.getName() + "' is not a field but a method or another type.",
            right.getLocation()
        );
    }

    // Associer le champ trouvé
    right.setDefinition(memberDef);
    right.setType(memberDef.getType());
    this.setType(memberDef.getType());
    return memberDef.getType();
}



    @Override
    public void decompile(IndentPrintStream s){
        if(left!=null){
            left.decompile(s);
            s.print(".");
        }
        right.decompile(s);

    }

    @Override
    public void prettyPrintChildren(PrintStream s, String name){
        if(left!=null){
            left.prettyPrint(s,name,false);
        }
        right.prettyPrint(s,name,false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        if(left!=null){
            left.iterChildren(f);
        }
        right.iterChildren(f);
    }


    @Override
    protected DVal codeGenExpr(DecacCompiler compiler){
        left.codeGenExpr(compiler);
        right.isParam = true;
        right.codeGenExpr(compiler);
        return null;
    }

    @Override
    public DAddr getAddr(DecacCompiler compiler){
        return null;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler){
        left.codeGenInst(compiler);
        right.codeGenInst(compiler);
    }

    protected DVal codeGenInst(DecacCompiler compiler, LinkedList<Instruction> lines){
        left.codeGenPrint(compiler);
        right.codeGenPrint(compiler);
        return null;
    }
}