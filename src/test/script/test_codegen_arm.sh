#!/bin/bash

GREEN="\033[0;32m"
RED="\033[0;31m"
NC="\033[0m"

cd "$(dirname "$0")"/../../.. || exit 1

# Répertoires (ajoute autant de répertoires que nécessaire)
TEST_DIRS=(
    "src/test/deca/codegen/valid/provided/perso"  # Répertoire 1
)

DECAC_EXEC="decac -a"  # Assurez-vous que le chemin vers decac est correct
arm_EXEC="src/test/script/script_arm.sh"  # La commande ima, tu dois t'assurer qu'elle est dans ton PATH ou indiquer le chemin complet

PASSED=0
FAILED=0

# Définir les outputs attendus pour chaque fichier de test  
declare -A EXPECTED_OUTPUTS
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/cond0.deca"]="ok"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/ecrit0.deca"]="okok"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/entier1.deca"]="12"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/condition.deca"]="2"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/declaration.deca"]="1.00000e+003"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/erreurDiv.deca"]="Error: Division by zero Error: Stack Overflow"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/floatx.deca"]="1.00000e+00"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/printLong.deca"]="-6"

EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/debut.deca"]="ca marche"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/and.deca"]="ca marche"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/avance.deca"]="8"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/compilateurMarche.deca"]="mon compilateur presque marche"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/encoreVrai.deca"]="vrai"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/faux.deca"]="faux"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/nega.deca"]="-1"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/plusLa.deca"]="jenesuispluslajenesuispluslajenesuispluslajenesuispluslajenesuisplusla"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/sortie4.deca"]="4"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/sortie5.deca"]="5"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/sortie5v2.deca"]="5"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/sortie6.deca"]="5"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/sortie6v2.deca"]="6"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/sortie7.deca"]="7"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/sortie7v2.deca"]="7"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/sortie8.deca"]="8"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/Vrai.deca"]="Vrai 4 je ne suis plus la"

EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/amicaux.deca"]="220 et 284 sont des nombres amicaux."
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/calculLong.deca"]="62 62"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/MultiplicationCond.deca"]="Lerésultatfinalest:16"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/narcissique.deca"]="153 est un nombre narcissique."
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/OuAnd.deca"]="Lerésultatfinalest:8"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/OuCond.deca"]="Lerésultatfinalest:10"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/palindrome.deca"]="12321 est un palindrome."
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/parfait.deca"]="28 est un nombre parfait."
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/plusieursTests.deca"]="Reverse de 12345: 54321
                                                                                              Reverse de 54321: 12345
                                                                                              Somme des chiffres de 987654: 39
                                                                                              Factorielle de 5: 120
                                                                                              7 est un nombre positif impair.
                                                                                              7 est supérieur à 5."
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/premier.deca"]="x n est pas un nombre premier."


EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/NotEquals.deca"]="not ok"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/greaterEquals.deca"]="not ok"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/lower.deca"]="not ok"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/Equals.deca"]="ok"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/classPrint.deca"]="je suis la Non je suis la"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/classSimple.deca"]="6"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/classExtendsSimple.deca"]="je suis la"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/classExtendsFields.deca"]="je suis la 6"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/CastSimple.deca"]="3"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/FloatCastSimple.deca"]="5"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/paramClass.deca"]="1"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/deuxArgsMethod.deca"]="3"

EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/InitVarField.deca"]="5"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/MultOpArithClass.deca"]="6"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/printDot.deca"]="a.z = 6"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/AddPrintDot.deca"]="a.z = 7"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/AgetX.deca"]="a.getX() = 0"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/EstGrand.deca"]="Devinezlenombre(entre1et100)!Essai1:Tropgrand!Essai2:Tropgrand!Essai3:Tropgrand!Essai4:Tropgrand!Essai5:Tropgrand!Désolé,vousn'avezpasdeviné.Lenombreétait:42"

EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/Moyenne.deca"]="Moyenne pondérée : 3.45000e+01
                                                                             La moyenne dépasse-t-elle le seuil de 30 ?  :true"




EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/Attribution_Field.deca"]="10"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/cast_float_int.deca"]="5"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/cast_int.deca"]="Résultataprèscastetaddition:7"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/cast_Moyenne.deca"]="Flottantinitial:3.70000e+00Arrondiàl'entierleplusproche:4"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/cast_print.deca"]="Lesvaleurssontidentiquesaprèscast."
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/cast_print_float.deca"]="Multiplicationd'unentieretd'unflottant:2.50000e+00"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/class_incremente.deca"]="1"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/divFloat.deca"]="Divisionréelle:3.50000e+00Divisionentière:3"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/floatArrondi.decaa"]="Flottantinitial:3.70000e+00Arrondiàl'entierleplusproche:4"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/Minus_cast.deca"]="Multiplicationd'unentieretd'unflottant:2.50000e+00"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/Moyenne_ponderee.deca"]="Moyennepondérée:3.45000e+01Lamoyennedépasse-t-elleleseuilde30?:true"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/mult.deca"]="Multiplicationd'unentieretd'unflottant:1.25000e+01"

EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/sommeImpairs.deca"]="SuitedeFibonacci(termesimpairsseulement):11351321Sommedestermesimpairs:44"

EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/floatArrondi.deca"]="Flottantinitial:3.70000e+00Arrondiàl'entierleplusproche:4"


EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/deux_Appels.deca"]="100 200"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/class_Age.deca"]="6"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/calcul2fois.deca"]="8"

EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/detect_min.deca"]="Le minimum entre 12 et 18 est : 12"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/pgcd.deca"]="Le PGCD des deux nombres est : 14"
EXPECTED_OUTPUTS["src/test/deca/codegen/valid/provided/perso/puissance.deca"]="3 à la puissance 4 est : 81"


# Boucle sur chaque répertoire dans TEST_DIRS
for TEST_DIR in "${TEST_DIRS[@]}"; do
    echo "Scanning directory: $TEST_DIR"

    for TEST_FILE in "$TEST_DIR"/*.deca; do
        echo "Running decac on test: $TEST_FILE"

        # Exécution de decac
        decac_output=$($DECAC_EXEC "$TEST_FILE" 2>&1)

        if [ $? -eq 0 ]; then
            echo -e "${GREEN}DeCA compilation PASSED${NC} - $TEST_FILE"

            # Création du fichier .ass associé
            ass_file="${TEST_FILE%.deca}.s"

            # Exécution avec arm
            arm_output=$($arm_EXEC "$ass_file" 2>&1)

            # Normalisation des sorties
            normalized_actual=$(echo "$arm_output" | tr -d '[:space:]')
            normalized_expected=$(echo "${EXPECTED_OUTPUTS["$TEST_FILE"]}" | tr -d '[:space:]')

            echo -e "Normalized expected: $normalized_expected"
            echo -e "Normalized actual: $normalized_actual"

            if [[ "$normalized_actual" == "$normalized_expected" ]]; then
                echo -e "${GREEN}Output PASSED${NC} - $TEST_FILE"
                PASSED=$((PASSED + 1))
            else
                echo -e "${RED}Output FAILED${NC} - $TEST_FILE"
                FAILED=$((FAILED + 1))
            fi
        else
            echo -e "${RED}DeCA compilation FAILED${NC} - $TEST_FILE"
            FAILED=$((FAILED + 1))
        fi
    done
done

# Résumé final
echo
echo "Summary:"
echo -e "${GREEN}PASSED: $PASSED${NC}"
echo -e "${RED}FAILED: $FAILED${NC}"
