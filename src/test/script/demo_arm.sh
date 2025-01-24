#!/bin/bash

GREEN="\033[0;32m"
RED="\033[0;31m"
NC="\033[0m"

cd "$(dirname "$0")"/../../.. || exit 1

# Répertoires (ajoute autant de répertoires que nécessaire)
TEST_DIRS=(
    "src/test/deca/codegenARM/valid/provided/perso"  # Répertoire 1
)

DECAC_EXEC="decac -a"  # Assurez-vous que le chemin vers decac est correct
arm_EXEC="src/test/script/script_arm.sh"  # La commande ima, tu dois t'assurer qu'elle est dans ton PATH ou indiquer le chemin complet

PASSED=0
FAILED=0

# Définir les outputs attendus pour chaque fichier de test  
declare -A EXPECTED_OUTPUTS
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/ecrit0.deca"]="okok"
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/entier1.deca"]="12"
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/printLong.deca"]="-6"
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/nega.deca"]="-1"
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/calculLong.deca"]="62 62"
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/declaration.deca"]="1.0000003"
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/floatx.deca"]="1.000000"
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/mul.deca"]="66"
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/add.deca"]="55"
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/sub.deca"]="-1-1"
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/div.deca"]="00"
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/addfloat.deca"]="6.000000"
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/subfloat.deca"]="-1.000000"
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/mulfloat.deca"]="8.750000"
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/divfloat.deca"]="5.000000"
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/affectation.deca"]="3"
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/affectationfloat.deca"]="3.500000"
EXPECTED_OUTPUTS["src/test/deca/codegenARM/valid/provided/perso/helloworld.deca"]="hello World!"

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