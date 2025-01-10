package fr.ensimag.deca.context;

import java.io.IOException;

import fr.ensimag.deca.CompilerOptions;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.*;

import static fr.ensimag.deca.tree.ManualTestInitialGencode.gencodeSource;

public class ManualTestContext {
    public static void main(String[] args) throws IOException {
        // Création du compilateur
        DecacCompiler compiler = new DecacCompiler(new CompilerOptions(), null);

        // Récupération de l'environnement des types prédéfinis
        EnvironmentType envTypes = compiler.environmentType;
        ListDeclClass listClasses = new ListDeclClass();

        // Liste des déclarations de variables
        ListDeclVar listDeclVar = new ListDeclVar();

        // Création de la table des symboles et des identifiants pour x, y, et z
        SymbolTable symbolTable = compiler.symbolTable;
        Symbol symbolX = symbolTable.create("x");
        Symbol symbolY = symbolTable.create("y");
        Symbol symbolZ = symbolTable.create("z");

        // Création des identifiants pour x, y, et z avec leurs locations
        Identifier idX = new Identifier(symbolX);
        idX.setLocation(new Location(1, 5, "test.deca"));

        Identifier idY = new Identifier(symbolY);
        idY.setLocation(new Location(2, 5, "test.deca"));

        Identifier idZ = new Identifier(symbolZ);
        idZ.setLocation(new Location(3, 5, "test.deca"));

        // Utilisation du type prédéfini int
        Identifier intType = new Identifier(envTypes.INT.getName());
        intType.setLocation(new Location(1, 1, "test.deca"));

        // Déclarations des variables : int x = 6; et int y = 3;
        Initialization initX = new Initialization(new IntLiteral(6));
        initX.setLocation(new Location(1, 10, "test.deca"));
        listDeclVar.add(new DeclVar(intType, idX, initX));

        Initialization initY = new Initialization(new IntLiteral(1));
        initY.setLocation(new Location(2, 10, "test.deca"));
        listDeclVar.add(new DeclVar(intType, idY, initY));

        // Déclaration de la variable z (sans initialisation ici)
        listDeclVar.add(new DeclVar(intType, idZ, new NoInitialization()));

        // Liste des instructions
        ListInst listInst = new ListInst();

        // Calcul de 2 * x / 3 * y
        IntLiteral two = new IntLiteral(2);
        two.setLocation(new Location(3, 8, "test.deca"));

        IntLiteral three = new IntLiteral(3);
        three.setLocation(new Location(3, 12, "test.deca"));

        // Multiplication : 2 * x
        Multiply twoTimesX = new Multiply(two, idX);
        twoTimesX.setLocation(new Location(3, 10, "test.deca"));

        // Multiplication : 3 * y
        Multiply threeTimesY = new Multiply(three, idY);
        threeTimesY.setLocation(new Location(3, 14, "test.deca"));

        // Division : (2 * x) / (3 * y)
        Divide division = new Divide(twoTimesX, threeTimesY);
        division.setLocation(new Location(3, 16, "test.deca"));

        // Instruction : z = (2 * x) / (3 * y);
        Assign assignZ = new Assign(idZ, division);
        assignZ.setLocation(new Location(3, 5, "test.deca"));
        listInst.add(assignZ);

        // Nouvelle instruction : println(z);
        ListExpr listExpr = new ListExpr();
        listExpr.add(idZ); // Ajouter la variable z à la liste des expressions
        Println printInst = new Println(false, listExpr); // false signifie que ce n'est pas un println pour une expression en ligne
        printInst.setLocation(new Location(4, 5, "test.deca"));
        listInst.add(printInst); // Ajouter l'instruction de println à la liste des instructions

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
