#!/bin/bash

GREEN="\033[0;32m"
RED="\033[0;31m"
NC="\033[0m"

# Se déplacer dans le répertoire racine du projet
cd "$(dirname "$0")"/../../.. || exit 1

# Répertoires de tests (relatifs à la racine du projet)
TEST_CONTEXTS=(
    "src/test/deca/context/invalid/provided"  # Répertoire 1
    "src/test/deca/context/valid/provided"    # Répertoire 2
)

DECAC_EXEC="decac"  # Assurez-vous que le chemin vers decac est correct

PASSED=0
FAILED=0

# Définir les outputs attendus pour chaque fichier de test invalide
declare -A EXPECTED_OUTPUTS
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/affect-incompatible.deca"]="affect-incompatible.deca:15"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/affectation_multiple_incompatible.deca"]="affectation_multiple_incompatible.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/affectation_non_entier.deca"]="affectation_non_entier.deca:13"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/comparaison_types_incompatibles.deca"]="comparaison_types_incompatibles.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/condition_entier.deca"]="condition_entier.deca:13"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/division_par_boolean.deca"]="division_par_boolean.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_addition_boolean.deca"]="invalid_addition_boolean.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_assign_boolean_to_float.deca"]="invalid_assign_boolean_to_float.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_assign_float_to_boolean.deca"]="invalid_assign_float_to_boolean.deca:13"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_binary_operation_float_float.deca"]="invalid_binary_operation_float_float.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_boolean_expression_assign.deca"]="invalid_boolean_expression_assign.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_comparison_int_boolean.deca"]="invalid_comparison_int_boolean.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_logical_comparison.deca"]="invalid_logical_comparison.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_logical_comparison_with_constant.deca"]="invalid_logical_comparison_with_constant.deca:13"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_logical_or_float.deca"]="invalid_logical_or_float.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_modulo_by_float.deca"]="invalid_modulo_by_float.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_modulo_float.deca"]="invalid_modulo_float.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_modulo_with_constant_float.deca"]="invalid_modulo_with_constant_float.deca:13"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_multiplication_boolean.deca"]="invalid_multiplication_boolean.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_multiplication_int_boolean.deca"]="invalid_multiplication_int_boolean.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_subtraction_boolean_boolean.deca"]="invalid_subtraction_boolean_boolean.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_subtraction_boolean.deca"]="invalid_subtraction_boolean.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/operation_boolean_entier.deca"]="operation_boolean_entier.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/operation_non_supportee.deca"]="operation_non_supportee.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/variable_double_declaration.deca"]="variable_double_declaration.deca:11"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/variable_non_declaree.deca"]="variable_non_declaree.deca:12"

echo "------------------TEST DE LA PARTIE B : ANALYSE CONTEXTUELLE------------------"

# Boucle sur chaque répertoire dans TEST_CONTEXTS
for TEST_CONTEXT in "${TEST_CONTEXTS[@]}"; do
    echo "Scanning directory: $TEST_CONTEXT"

    # Boucle sur tous les fichiers .deca dans le répertoire TEST_CONTEXT
    for TEST_FILE in "$TEST_CONTEXT"/*.deca; do
        echo "Running decac -v on test: $TEST_FILE"

        # Exécution de decac avec l'option -v
        decac_output=$($DECAC_EXEC -v "$TEST_FILE" 2>&1)
        echo -e "Decac output:\n$decac_output"

        if [[ "$TEST_CONTEXT" == "src/test/deca/context/invalid/provided" ]]; then
            # Vérification pour les tests invalides
            EXPECTED_OUTPUT="${EXPECTED_OUTPUTS["$TEST_FILE"]}"
            echo -e "Expected output: ${EXPECTED_OUTPUT}"
            echo -e "Actual output: ${decac_output}"

            if [[ "$decac_output" == *"$EXPECTED_OUTPUT"* ]]; then
                echo -e "${GREEN}Output PASSED${NC} - $TEST_FILE"
                PASSED=$((PASSED + 1))
            else
                echo -e "${RED}Output FAILED${NC} - $TEST_FILE"
                FAILED=$((FAILED + 1))
            fi

        else
            # Vérification pour les tests valides (aucun output attendu)
            if [[ -z "$decac_output" ]]; then
                echo -e "${GREEN}Output PASSED${NC} - $TEST_FILE"
                PASSED=$((PASSED + 1))
            else
                echo -e "${RED}Output FAILED (unexpected output)${NC} - $TEST_FILE"
                FAILED=$((FAILED + 1))
            fi
        fi
    done
done


# Résumé final
echo
echo "Summary:"
echo -e "${GREEN}PASSED: $PASSED${NC}"
echo -e "${RED}FAILED: $FAILED${NC}"
