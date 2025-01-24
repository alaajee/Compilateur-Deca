.section .data
formatfloat: .asciz "%f"
.align 0
data0: .double 2.5

.section .text
.global main
main:
	VMOV.F64 D0, #3.5
	LDR R1, =data0
	VSTR D0, [R1]
	LDR R0, =formatfloat
	LDR R1, =data0
	VLDR.F64 D0, [R1]
	VMOV R2, R3, D0
	BL printf
	MOV R0, #0
	BL fflush
	MOV R7, #1
	SVC #0
