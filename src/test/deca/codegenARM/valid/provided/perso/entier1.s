.section .data
formatint: .asciz "%d"
data0: .word 0

.section .text
.global main
main:
	MOV R4, #1
	LDR R1, =data0
	STR R4, [R1]
	LDR R0, =formatint
	LDR R1, =data0
	LDR R1, [R1]
	BL printf
	LDR R6, =data0
	LDR R6, [R6]
	MOV R7, #1
	ADD R5, R6, R7
	LDR R1, =data0
	STR R5, [R1]
	LDR R0, =formatint
	LDR R1, =data0
	LDR R1, [R1]
	BL printf
	MOV R0, #0
	BL fflush
	MOV R7, #1
	SVC #0
