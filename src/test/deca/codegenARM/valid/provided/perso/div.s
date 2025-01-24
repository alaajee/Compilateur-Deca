.section .data
line: .asciz "\n"
formatint: .asciz "%d"
data2: .word 0
data1: .word 3
data0: .word 2

.section .text
.global main
main:
	LDR R0, =data0
	LDR R0, [R0]
	LDR R1, =data1
	LDR R1, [R1]
	BL __aeabi_idiv
	MOV R4, R0
	LDR R1, =data2
	STR R4, [R1]
	LDR R0, =formatint
	LDR R1, =data2
	LDR R1, [R1]
	BL printf
	LDR R0, =data0
	LDR R0, [R0]
	LDR R1, =data1
	LDR R1, [R1]
	BL __aeabi_idiv
	MOV R5, R0
	LDR R0, =formatint
	MOV R1, R5
	BL printf
	LDR R0, =line
	BL printf
	MOV R0, #0
	BL fflush
	MOV R7, #1
	SVC #0
