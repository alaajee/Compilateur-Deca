.section .data
formatint: .asciz "%d"
data0: .word 5

.section .text
.global main
main:
	MOV R4, #3
	LDR R1, =data0
	STR R4, [R1]
	LDR R0, =formatint
	LDR R1, =data0
	LDR R1, [R1]
	BL printf
	MOV R0, #0
	BL fflush
	MOV R7, #1
	SVC #0
