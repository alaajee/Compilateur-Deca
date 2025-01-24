.section .data
formatfloat: .asciz "%f"
.align 0
data2: .double 0.0
.align 0
data1: .double 3.5
.align 0
data0: .double 2.5

.section .text
.global main
main:
	LDR R1, =data0
	VLDR.F64 D0, [R1]
	LDR R1, =data1
	VLDR.F64 D1, [R1]
	VADD.F64 D2, D0, D1
	LDR R1, =data2
	VSTR D2, [R1]
	LDR R0, =formatfloat
	LDR R1, =data2
	VLDR.F64 D0, [R1]
	VMOV R2, R3, D0
	BL printf
	MOV R0, #0
	BL fflush
	MOV R7, #1
	SVC #0
