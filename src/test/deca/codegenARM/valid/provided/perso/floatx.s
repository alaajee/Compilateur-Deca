.section .data
formatfloat: .asciz "%f"
.align 0
data0: .double 0.0

.section .text
.global main
main:
	VMOV.F64 D0, #5
	VMOV.F64 D2, D0
	LDR R1, =data0
	VSTR D2, [R1]
	VMOV.F64 D0, #5
	VMOV.F64 D3, D0
	LDR R1, =data0
	VLDR.F64 D0, [R1]
	VMOV.F64 D1, D3
	VDIV.F64 D4, D0, D1
	LDR R1, =data0
	VSTR D4, [R1]
	LDR R0, =formatfloat
	LDR R1, =data0
	VLDR.F64 D0, [R1]
	VMOV R2, R3, D0
	BL printf
	MOV R0, #0
	BL fflush
	MOV R7, #1
	SVC #0
