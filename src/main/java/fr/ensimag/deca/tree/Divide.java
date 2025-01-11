package fr.ensimag.deca.tree;



import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.*;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }



    @Override
    public DVal codeGenExpr(DecacCompiler compiler){
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        constructeur constructeur = new constructeurSUB();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand,rightOperand,reg,constructeur,compiler);
        return register;
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        Type typeLeft = getLeftOperand().getType();
        Type typeRight = getRightOperand().getType();
        //  System.out.print(rightOperand + " * " + leftOperand + " = ");

        System.out.println("Diviser " + typeLeft + " par " + typeRight);
        if (typeRight.isNull() || typeLeft.isNull()){
            throw new RuntimeException("Pas possible de diviser un " + typeLeft + " par un " + typeRight + "");
        }
        if (typeRight.isInt() && typeLeft.isInt()){
            constructeurQUO constructeurQUO= new constructeurQUO();
            codeGen gen = new codeGen();
            gen.codeGenPrint(leftOperand,rightOperand,reg,constructeurQUO,compiler);
            compiler.addInstruction(new WINT());
        }
        else if (typeRight.isFloat() || typeLeft.isFloat()) {
            constructeurDIV constructeurDIV= new constructeurDIV();
            codeGen gen = new codeGen();
            gen.codeGenPrint(leftOperand,rightOperand,reg,constructeurDIV,compiler);
            compiler.addInstruction(new WFLOAT());
        }
        else {
            throw new RuntimeException("Pas possible de diviser un " + typeLeft + " par un " + typeRight + "");
        }


    }
}


// QUO
// DIV