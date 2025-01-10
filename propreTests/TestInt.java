package fr.ensimag.deca.context;

import java.io.IOException;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.*;

import static fr.ensimag.deca.tree.ManualTestInitialGencode.gencodeSource;

/**
 * Driver to test the contextual analysis (together with lexer/parser)
 *
 * @author Ensimag
 * @date 01/01/2025
 */
public class ManualTestContext {
    public static void main(String[] args) throws IOException {
        // Création du compilateur
        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);

        // Récupération de l'environnement des types prédéfinis
        EnvironmentType envTypes = compiler.environmentType;
        ListDeclClass listClasses = new ListDeclClass();

        // Liste des déclarations de variables
        ListDeclVar listDeclVar = new ListDeclVar();

        // Création de la table des symboles et des identifiants pour x et y
        SymbolTable symbolTable = compiler.symbolTable;
        Symbol symbolX = symbolTable.create("x");
        Symbol symbolY = symbolTable.create("y");

        // Création des identifiants x et y avec leurs locations
        Identifier idX = new Identifier(symbolX);
        idX.setLocation(new Location(1, 5, "test.deca"));

        Identifier idY = new Identifier(symbolY);
        idY.setLocation(new Location(2, 5, "test.deca"));

        // Utilisation du type prédéfini int
        Identifier intType = new Identifier(envTypes.INT.getName());
        intType.setLocation(new Location(1, 1, "test.deca"));

        // Déclarations des variables : int x = 5; et int y = 10;
        Initialization initX = new Initialization(new IntLiteral(5));
        initX.setLocation(new Location(1, 10, "test.deca"));
        listDeclVar.add(new DeclVar(intType, idX, initX));

        Initialization initY = new Initialization(new IntLiteral(10));
        initY.setLocation(new Location(2, 10, "test.deca"));
        listDeclVar.add(new DeclVar(intType, idY, initY));

        // Liste des instructions
        ListInst listInst = new ListInst();

        // Instruction : x = 2;
        IntLiteral three = new IntLiteral(2);
        three.setLocation(new Location(3, 8, "test.deca"));
        Assign assignX = new Assign(idX, three);
        assignX.setLocation(new Location(3, 5, "test.deca"));
        listInst.add(assignX);

        // Instruction : y = 7;
        IntLiteral seven = new IntLiteral(7);
        seven.setLocation(new Location(4, 8, "test.deca"));
        Assign assignY = new Assign(idY, seven);
        assignY.setLocation(new Location(4, 5, "test.deca"));
        listInst.add(assignY);

        // Instruction : y = x + y;
        Plus sumXY = new Plus(idX, idY);
        sumXY.setLocation(new Location(5, 8, "test.deca"));
        Assign assignSum = new Assign(idY, sumXY);
        assignSum.setLocation(new Location(5, 5, "test.deca"));
        listInst.add(assignSum);

        // Instruction : x = x - 3;
        Minus xMinusThree = new Minus(idX, three);
        xMinusThree.setLocation(new Location(6, 8, "test.deca"));
        Assign assignXMinusThree = new Assign(idX, xMinusThree);
        assignXMinusThree.setLocation(new Location(6, 5, "test.deca"));
        listInst.add(assignXMinusThree);

        // Nouvelle instruction : x = x / 2;
        IntLiteral two = new IntLiteral(2);
        two.setLocation(new Location(7, 8, "test.deca"));
        Divide xDivTwo = new Divide(idX, two);
        xDivTwo.setLocation(new Location(7, 8, "test.deca"));
        Assign assignXDivTwo = new Assign(idX, xDivTwo);
        assignXDivTwo.setLocation(new Location(7, 5, "test.deca"));
        listInst.add(assignXDivTwo);

        // Nouvelle instruction : y = y / x;
        Divide yDivX = new Divide(idY, idX);
        yDivX.setLocation(new Location(8, 8, "test.deca"));
        Assign assignYDivX = new Assign(idY, yDivX);
        assignYDivX.setLocation(new Location(8, 5, "test.deca"));
        listInst.add(assignYDivX);

        // Nouvelle multiplication : x = x * 3;
        IntLiteral threeForMultiplication = new IntLiteral(3);
        threeForMultiplication.setLocation(new Location(9, 8, "test.deca"));
        Multiply xTimesThree = new Multiply(idX, threeForMultiplication);
        xTimesThree.setLocation(new Location(9, 8, "test.deca"));
        Assign assignXTimesThree = new Assign(idX, xTimesThree);
        assignXTimesThree.setLocation(new Location(9, 5, "test.deca"));
        listInst.add(assignXTimesThree);

        // Création du bloc main avec les déclarations et les instructions
        Main mainBlock = new Main(listDeclVar, listInst);

        // Création du programme complet
        AbstractProgram source = new Program(listClasses, mainBlock);

        try {
            // Vérification contextuelle
            source.verifyProgram(compiler);
            String result = gencodeSource(source);
            System.out.println(result);
        } catch (LocationException e) {
            e.display(System.err);
            System.exit(1);
        }

        // Affichage du programme vérifié
        source.prettyPrint(System.out);
    }
}
