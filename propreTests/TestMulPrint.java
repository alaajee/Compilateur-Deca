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

        // Création de la table des symboles et des identifiants
        SymbolTable symbolTable = compiler.symbolTable;
        Symbol symbolX = symbolTable.create("x");

        // Création de l'identifiant x avec sa location
        Identifier idX = new Identifier(symbolX);
        idX.setLocation(new Location(1, 5, "test.deca"));

        // Utilisation du type prédéfini int
        Identifier intType = new Identifier(envTypes.INT.getName());
        intType.setLocation(new Location(1, 1, "test.deca"));

        // Déclaration de la variable : int x;
        listDeclVar.add(new DeclVar(intType, idX, new NoInitialization()));

        // Liste des instructions
        ListInst listInst = new ListInst();

        // Initialisation de x à 5
        IntLiteral five = new IntLiteral(5);
        five.setLocation(new Location(2, 8, "test.deca"));
        listInst.add(new Assign(idX, five));

        // Calcul de 5 * x * x
        FloatLiteral five1 = new FloatLiteral(5);
        Identifier idX2 = new Identifier(symbolX);
        Multiply xSquared = new Multiply(idX, idX2);
        xSquared.setLocation(new Location(3, 14, "test.deca"));

        Multiply fiveTimesXSquared = new Multiply(five1, xSquared);
        fiveTimesXSquared.setLocation(new Location(3, 8, "test.deca"));

        // Instruction println avec 5 * x * x
        ListExpr listExpr = new ListExpr();
        listExpr.add(fiveTimesXSquared);

        Println printInst = new Println(false, listExpr);
        printInst.setLocation(new Location(3, 1, "test.deca"));
        listInst.add(printInst);

        // Création du bloc main avec les déclarations et les instructions
        Main mainBlock = new Main(listDeclVar, listInst);

        // Création du programme complet
        AbstractProgram source = new Program(listClasses, mainBlock);

        try {
            // Vérification contextuelle
            source.verifyProgram(compiler);
//            String result = gencodeSource(source);
//            System.out.println(result);
        } catch (LocationException e) {
            e.display(System.err);
            System.exit(1);
        }

        // Affichage du programme vérifié
        source.prettyPrint(System.out);
    }
}
