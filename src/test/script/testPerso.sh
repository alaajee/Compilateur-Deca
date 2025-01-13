#!/bin/bash

GREEN="\033[0;32m"
RED="\033[0;31m"
NC="\033[0m"

# Répertoires (ajoute autant de répertoires que nécessaire)
TEST_DIRS=(
    "$HOME/ensimag/GL/Projet_GL/src/test/deca/codegen/valid/provided"  # Répertoire 1
)

DECAC_EXEC="decac"  # Assurez-vous que le chemin vers decac est correct
IMA_EXEC="ima"  # La commande ima, tu dois t'assurer qu'elle est dans ton PATH ou indiquer le chemin complet

PASSED=0
FAILED=0

# Définir les outputs attendus pour chaque fichier de test
declare -A EXPECTED_OUTPUTS
EXPECTED_OUTPUTS["$HOME/ensimag/GL/Projet_GL/src/test/deca/codegen/valid/provided/cond0.deca"]="ok"
EXPECTED_OUTPUTS["$HOME/ensimag/GL/Projet_GL/src/test/deca/codegen/valid/provided/ecrit0.deca"]="okok"
EXPECTED_OUTPUTS["$HOME/ensimag/GL/Projet_GL/src/test/deca/codegen/valid/provided/entier1.deca"]="12"
EXPECTED_OUTPUTS["$HOME/ensimag/GL/Projet_GL/src/test/deca/codegen/valid/provided/condition.deca"]="2"
EXPECTED_OUTPUTS["$HOME/ensimag/GL/Projet_GL/src/test/deca/codegen/valid/provided/declaration.deca"]="1.00000e+003"
EXPECTED_OUTPUTS["$HOME/ensimag/GL/Projet_GL/src/test/deca/codegen/valid/provided/erreurDiv.deca"]="Error: Division by zero Error: Stack Overflow"
EXPECTED_OUTPUTS["$HOME/ensimag/GL/Projet_GL/src/test/deca/codegen/valid/provided/floatx.deca"]="1.00000e+00"

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
            ass_file="${TEST_FILE%.deca}.ass"

            # Exécution avec ima
            ima_output=$($IMA_EXEC "$ass_file" 2>&1)

            # Normalisation des sorties
            normalized_actual=$(echo "$ima_output" | tr -d '[:space:]')
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
