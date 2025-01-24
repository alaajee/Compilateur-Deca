.section .data
line: .asciz "\n"
data0: .asciz "hello World!"

.section .text
.global main
main:
	LDR R0, =data0
	BL printf
	LDR R0, =line
	BL printf
	MOV R0, #0
	BL fflush
	MOV R7, #1
	SVC #0
