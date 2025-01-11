#!/bin/bash

GREEN="\033[0;32m"
RED="\033[0;31m"
NC="\033[0m"

# Répertoires (ajoute autant de répertoires que nécessaire)
TEST_DIRS=(
    "$HOME/ensimag/GL/Projet_GL/src/test/deca/context/valid/provided"  # Répertoire 1
#    "$HOME/ensimag/GL/Projet_GL/src/test/deca/context/valid/another"   # Répertoire 2
#    "$HOME/ensimag/GL/Projet_GL/src/test/deca/context/valid/more_tests" # Répertoire 3
)

DECAC_EXEC="decac"  # Assurez-vous que le chemin vers decac est correct
IMA_EXEC="ima"  # La commande ima, tu dois t'assurer qu'elle est dans ton PATH ou indiquer le chemin complet

PASSED=0
FAILED=0

# Définir les outputs attendus pour chaque fichier de test
declare -A EXPECTED_OUTPUTS
EXPECTED_OUTPUTS["$HOME/ensimag/GL/Projet_GL/src/test/deca/context/valid/provided/hello-world.deca"]="Hello, world!"
#EXPECTED_OUTPUTS["$HOME/ensimag/GL/Projet_GL/src/test/deca/context/valid/provided/test2.deca"]="Hello, world!"
#EXPECTED_OUTPUTS["$HOME/ensimag/GL/Projet_GL/src/test/deca/context/valid/provided/test3.deca"]=1
# Ajoute d'autres fichiers et leurs sorties attendues ici

# Boucle sur chaque répertoire dans TEST_DIRS
for TEST_DIR in "${TEST_DIRS[@]}"; do
    echo "Scanning directory: $TEST_DIR"

    # Boucle sur tous les fichiers .deca dans le répertoire TEST_DIR
    for TEST_FILE in "$TEST_DIR"/*.deca; do
        echo "Running decac on test: $TEST_FILE"

        # Exécution de decac sur le fichier .deca spécifié
        decac_output=$($DECAC_EXEC "$TEST_FILE" 2>&1)
        echo -e "Decac output:\n$decac_output"

        if [ $? -eq 0 ]; then
            echo -e "${GREEN}DeCA compilation PASSED${NC} - $TEST_FILE"

            # Création du fichier .ass associé
            ass_file="${TEST_FILE%.deca}.ass"
            echo -e "Generated .ass file: $ass_file"

            # Exécution du fichier .ass avec ima
            ima_output=$($IMA_EXEC "$ass_file" 2>&1)
            echo -e "IMA output:\n$ima_output"

            # Récupérer l'output attendu pour ce fichier
            EXPECTED_OUTPUT="${EXPECTED_OUTPUTS["$TEST_FILE"]}"
            echo -e "Expected output: ${EXPECTED_OUTPUT}"
            echo -e "Actual output: ${ima_output}"

            if [[ "$ima_output" == "$EXPECTED_OUTPUT" ]]; then
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
