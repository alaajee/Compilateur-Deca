.section .data
line: .asciz "\n"
formatint: .asciz "%d"
data2: .word 3
data1: .word 2
data0: .word 1

.section .text
.global main
main:
	LDR R5, =data0
	LDR R5, [R5]
	LDR R6, =data0
	LDR R6, [R6]
	MUL R4, R5, R6
	LDR R6, =data0
	LDR R6, [R6]
	MUL R5, R4, R6
	LDR R6, =data0
	LDR R6, [R6]
	MUL R4, R5, R6
	LDR R6, =data2
	LDR R6, [R6]
	MUL R5, R4, R6
	LDR R6, =data1
	LDR R6, [R6]
	MUL R4, R5, R6
	LDR R6, =data0
	LDR R6, [R6]
	MUL R5, R4, R6
	LDR R6, =data0
	LDR R6, [R6]
	MUL R4, R5, R6
	LDR R6, =data1
	LDR R6, [R6]
	LDR R7, =data2
	LDR R7, [R7]
	MUL R5, R6, R7
	MOV R7, #2
	MUL R6, R5, R7
	SUB R5, R4, R6
	LDR R0, =formatint
	MOV R1, R5
	BL printf
	LDR R0, =line
	BL printf
	MOV R0, #0
	BL fflush
	MOV R7, #1
	SVC #0
