package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.codegen.constructeur;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.codegen.constructeurCMP;
import fr.ensimag.deca.codegen.constructeurADD;

/**
 *
 * @author gl02
 * @date 01/01/2025
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler){
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);

        GPRegister reg = compiler.associerReg();
        constructeur constructeur = new constructeurADD();
        codeGen gen = new codeGen();
        DVal register = gen.codeGen(leftOperand,rightOperand,reg,constructeur,compiler);
        // GPRegister reg1 = compiler.associerReg();
        // constructeur constructeur1 = new constructeurCMP();
        // codeGen gen1 = new codeGen();
        // DVal register1 = gen1.codeGen(register, new ImmediateInteger(2), reg1, constructeur1, compiler);
        
        compiler.addInstruction(new CMP(new ImmediateInteger(0), reg));



        // compiler.addInstruction(new CMP(new ImmediateInteger(2), reg1));
        
        // compiler.addInstruction(new LOAD(leftOperand,reg));
        // constructeur.constructeur(compiler, rightOperand, reg);




        compiler.addInstruction(new SNE(reg));
        compiler.addInstruction(new CMP(new ImmediateInteger(0),reg));
        compiler.libererReg(reg.getNumber());
        gen.finalizeAndPush(reg, compiler);
        return reg;
    }


    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal leftOperand = getLeftOperand().codeGenExpr(compiler);
        DVal rightOperand = getRightOperand().codeGenExpr(compiler);
        GPRegister reg = compiler.associerReg();
        //  System.out.print(rightOperand + " * " + leftOperand + " = ");
        constructeur constructeurADD = new constructeurADD();
        codeGen gen = new codeGen();
        gen.codeGenPrint(leftOperand,rightOperand,reg,constructeurADD,compiler);
        if (getLeftOperand().getType().isFloat() || getRightOperand().getType().isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
        else {
            compiler.addInstruction(new WINT());
        }
    }
}
