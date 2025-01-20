#!/bin/bash

GREEN="\033[0;32m"
RED="\033[0;31m"
NC="\033[0m"

# Se déplacer dans le répertoire racine du projet
cd "$(dirname "$0")"/../../.. || exit 1

# Répertoires de tests (relatifs à la racine du projet)
TEST_CONTEXTS=(
    "src/test/deca/context/invalid/provided"  # Répertoire 1
    "src/test/deca/context/valid/provided"  
)

DECAC_EXEC="decac"  # Assurez-vous que le chemin vers decac est correct

PASSED=0
FAILED=0

# Définir les outputs attendus pour chaque fichier de test invalide
declare -A EXPECTED_OUTPUTS
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/access_nonexistent_field_error.deca"]="access_nonexistent_field_error.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/access_nonexistent_field_in_loop_error.deca"]="access_nonexistent_field_in_loop_error.deca:16"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/access_protected_field_error.deca"]="access_protected_field_error.deca:16"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/addition_boolean_float_error.deca"]="addition_boolean_float_error.deca:19"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/affect-incompatible.deca"]="affect-incompatible.deca:15"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/affectation_multiple_incompatible.deca"]="affectation_multiple_incompatible.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/affectation_non_entier.deca"]="affectation_non_entier.deca:13"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/BooleanFloatComparison_error.deca"]="BooleanFloatComparison_error.deca:15"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/BooleanIntCondition_error.deca"]="BooleanIntCondition_error.deca:13"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/call_method_on_nonexistent_field_error.deca"]="call_method_on_nonexistent_field_error.deca:16"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/call_nonexistent_method_error.deca"]="call_nonexistent_method_error.deca:16"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/call_specific_method_in_polymorphism_error.deca"]="call_specific_method_in_polymorphism_error.deca:24"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/CastBooleanDivision_error.deca"]="CastBooleanDivision_error.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/CastBooleanToFloat_error.deca"]="CastBooleanToFloat_error.deca:15"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/CastBooleanToFloatInLoop_error.deca"]="CastBooleanToFloatInLoop_error.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/CastFloatToBoolean_error.deca"]="CastFloatToBoolean_error.deca:15"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/CastIntToBooleanLoop_error.deca"]="CastIntToBooleanLoop_error.deca:13"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/comparaison_types_incompatibles.deca"]="comparaison_types_incompatibles.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/comparison_boolean_int_error.deca"]="comparison_boolean_int_error.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/condition_entier.deca"]="condition_entier.deca:13"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/division_par_boolean.deca"]="division_par_boolean.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/field_redefinition_incompatible_error.deca"]="field_redefinition_incompatible_error.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/ImplicitBooleanAddition.deca"]="ImplicitBooleanAddition.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/implicit_conversion_error.deca"]="implicit_conversion_error.deca:20"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/incompatible_object_assignment_error.deca"]="incompatible_object_assignment_error.deca:21"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_addition_boolean.deca"]="invalid_addition_boolean.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_arithmetic_error.deca"]="invalid_arithmetic_error.deca:15"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_assign_boolean_to_float.deca"]="invalid_assign_boolean_to_float.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_assign_float_to_boolean.deca"]="invalid_assign_float_to_boolean.deca:13"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_binary_operation_float_float.deca"]="invalid_binary_operation_float_float.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_boolean_expression_assign.deca"]="invalid_boolean_expression_assign.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_cast_to_unrelated_class_error.deca"]="invalid_cast_to_unrelated_class_error.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_comparison_int_boolean.deca"]="invalid_comparison_int_boolean.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_logical_and_float_boolean_error.deca"]="invalid_logical_and_float_boolean_error.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_logical_and_with_float_error.deca"]="invalid_logical_and_with_float_error.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_logical_comparison.deca"]="invalid_logical_comparison.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_logical_comparison_with_constant.deca"]="invalid_logical_comparison_with_constant.deca:13"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_logical_or_float.deca"]="invalid_logical_or_float.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_modulo_by_float.deca"]="invalid_modulo_by_float.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_modulo_float.deca"]="invalid_modulo_float.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_modulo_with_constant_float.deca"]="invalid_modulo_with_constant_float.deca:13"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_multiplication_boolean.deca"]="invalid_multiplication_boolean.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_multiplication_int_boolean.deca"]="invalid_multiplication_int_boolean.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_subtraction_boolean.deca"]="invalid_subtraction_boolean.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/invalid_subtraction_boolean_boolean.deca"]="invalid_subtraction_boolean_boolean.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/method_argument_type_error.deca"]="method_argument_type_error.deca:15"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/method_override_return_type_error.deca"]="method_override_return_type_error.deca:16"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/method_parameter_type_error.deca"]="method_parameter_type_error.deca:16"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/method_return_type_mismatch_error.deca"]="method_return_type_mismatch_error.deca:16"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/method_signature_mismatch_error.deca"]="method_signature_mismatch_error.deca:18"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/MultiplicationBooleanFloat_error.deca"]="MultiplicationBooleanFloat_error.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/object_in_boolean_condition_error.deca"]="object_in_boolean_condition_error.deca:16"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/operation_boolean_entier.deca"]="operation_boolean_entier.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/operation_non_supportee.deca"]="operation_non_supportee.deca:14"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/override_method_return_type_error.deca"]="override_method_return_type_error.deca:16"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/protected_field_access_error.deca"]="protected_field_access_error.deca:22"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/polymorphic_method_signature_error.deca"]="polymorphic_method_signature_error.deca:16"
EXPECTED_OUTPUTS["src/test/deca/context/invalid/provided/TestConditionComplexeComparaisonInvalide.deca"]="TestConditionComplexeComparaisonInvalide.deca:21"
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
