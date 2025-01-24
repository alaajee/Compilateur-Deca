.section .data
line: .asciz "\n"
formatint: .asciz "%d"
data2: .word 3
data1: .word 2
data0: .word 1

.section .text
.global main
main:
	MOV R5, #2
	LDR R6, =data0
	LDR R6, [R6]
	MUL R4, R5, R6
	LDR R6, =data0
	LDR R6, [R6]
	MUL R5, R4, R6
	LDR R6, =data0
	LDR R6, [R6]
	MUL R4, R5, R6
	LDR R6, =data0
	LDR R6, [R6]
	MUL R5, R4, R6
	LDR R6, =data0
	LDR R6, [R6]
	MUL R4, R5, R6
	LDR R6, =data0
	LDR R6, [R6]
	MUL R5, R4, R6
	LDR R6, =data0
	LDR R6, [R6]
	MUL R4, R5, R6
	LDR R6, =data0
	LDR R6, [R6]
	MUL R5, R4, R6
	LDR R6, =data1
	LDR R6, [R6]
	MOV R7, #2
	MUL R4, R6, R7
	MOV R7, #2
	MUL R6, R4, R7
	MOV R7, #2
	MUL R4, R6, R7
	MOV R7, #2
	MUL R6, R4, R7
	ADD R4, R5, R6
	MOV R6, #2
	MUL R5, R4, R6
	MOV R6, #6
	SUB R4, R5, R6
	LDR R1, =data2
	STR R4, [R1]
	LDR R0, =formatint
	LDR R1, =data2
	LDR R1, [R1]
	BL printf
	LDR R0, =line
	BL printf
	MOV R6, #2
	LDR R7, =data0
	LDR R7, [R7]
	MUL R5, R6, R7
	LDR R7, =data0
	LDR R7, [R7]
	MUL R6, R5, R7
	LDR R7, =data0
	LDR R7, [R7]
	MUL R5, R6, R7
	LDR R7, =data0
	LDR R7, [R7]
	MUL R6, R5, R7
	LDR R7, =data0
	LDR R7, [R7]
	MUL R5, R6, R7
	LDR R7, =data0
	LDR R7, [R7]
	MUL R6, R5, R7
	LDR R7, =data0
	LDR R7, [R7]
	MUL R5, R6, R7
	LDR R7, =data0
	LDR R7, [R7]
	MUL R6, R5, R7
	LDR R7, =data1
	LDR R7, [R7]
	MOV R8, #2
	MUL R5, R7, R8
	MOV R8, #2
	MUL R7, R5, R8
	MOV R8, #2
	MUL R5, R7, R8
	MOV R8, #2
	MUL R7, R5, R8
	ADD R5, R6, R7
	MOV R7, #2
	MUL R6, R5, R7
	MOV R7, #6
	SUB R5, R6, R7
	LDR R0, =formatint
	MOV R1, R5
	BL printf
	MOV R0, #0
	BL fflush
	MOV R7, #1
	SVC #0
