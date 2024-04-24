its      stmfd   r13!, {r0-r10, r11, lr} ; This is PreWritten Code for int to string
         mov     r11, r13
         ldr     r9, =str_buf
         ldr     r3, [r11, #4*13]
         mov     r4, #10
         mov     r8, #0
         mov     r5, #0
         mov     r6, #0
         mov     r7, #0
         cmp     r3, #0
         mov     r10, #0
         rsblt   r3, r3, #0
         movlt   r5, #0x2D
         addlt   r8, r8, #8
         addlt   r10, r10, #1
its_init cmp     r3, r4
         blt     its_loop
         mov     r1, r4
         mov     r2, #10
         stmfd   r13!, {r1}
         stmfd   r13!, {r2}
         sub     r13, r13, #4
         bl      mul
         ldr     r0, [r13]
         mov     r4, r0
         b       its_init
its_loop mov     r1, r4
         mov     r2, #10
         stmfd   r13!, {r2}
         stmfd   r13!, {r1}
         sub     r13, r13, #4
         bl      div
         ldr     r0, [r13]
         mov     r4, r0
         cmp     r4, #0
         beq     its_exit
         mov     r2, r0
         mov     r1, r3
         stmfd   r13!, {r2}
         stmfd   r13!, {r1}
         sub     r13, r13, #4
         bl      div
         ldr     r0, [r13]
         mov     r1, r0
         add     r0, r0, #0x30
         lsl     r0, r0, r8
         add     r5, r5, r0
         mov     r2, r4
         stmfd   r13!, {r1}
         stmfd   r13!, {r2}
         sub     r13, r13, #4
         bl      mul
         ldr     r0, [r13]
         sub     r3, r3, r0
         add     r8, r8, #8
         add     r10, r10, #1
         cmp     r8, #32
         moveq   r8, #0
         beq     its2
         b       its_loop
its2     mov     r1, r4
         mov     r2, #10
         stmfd   r13!, {r2}
         stmfd   r13!, {r1}
         sub     r13, r13, #4
         bl      div
         ldr     r0, [r13]
         mov     r4, r0
         cmp     r4, #0
         beq     its_exit
         mov     r2, r0
         mov     r1, r3
         stmfd   r13!, {r2}
         stmfd   r13!, {r1}
         sub     r13, r13, #4
         bl      div
         ldr     r0, [r13]
         mov     r1, r0
         add     r0, r0, #0x30
         lsl     r0, r0, r8
         add     r6, r6, r0
         mov     r2, r4
         stmfd   r13!, {r1}
         stmfd   r13!, {r2}
         sub     r13, r13, #4
         bl      mul
         ldr     r0, [r13]
         sub     r3, r3, r0
         add     r8, r8, #8
         add     r10, r10, #1
         cmp     r8, #32
         moveq   r8, #0
         beq     its3
         b       its2
its3     mov     r1, r4
         mov     r2, #10
         stmfd   r13!, {r2}
         stmfd   r13!, {r1}
         sub     r13, r13, #4
         bl      div
         ldr     r0, [r13]
         mov     r4, r0
         cmp     r4, #0
         beq     its_exit
         mov     r2, r0
         mov     r1, r3
         stmfd   r13!, {r1}
         stmfd   r13!, {r2}
         sub     r13, r13, #4
         bl      mul
         ldr     r0, [r13]
         mov     r1, r0
         add     r0, r0, #0x30
         lsl     r0, r0, r8
         add     r7, r7, r0
         add     r10, r10, #1
         mov     r2, r4
         stmfd   r13!, {r1}
         stmfd   r13!, {r2}
         sub     r13, r13, #4
         bl      mul
         ldr     r0, [r13]
         sub     r3, r3, r0
         add     r8, r8, #8
         b       its3
its_exit stmia   r9, {r5-r7}
         mov     r13, r11
         ldmfd   r13!, {r0-r10, r11, pc}
