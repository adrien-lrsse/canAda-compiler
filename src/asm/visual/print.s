;       ########### Print a string ##*#########
str_out      fill    0x1000
str_buf      fill    0xC
start        b       main

println_int  stmfd   r13!, {r0-r2, r11, lr} ; This is PreWritten Code for println(int n)
             mov     r11, r13
             ldr     r0, [r11, #4*5]
             sub     r13, r13, #4
             str     r0, [r13]
             bl      its
             ldr     r0, =str_buf
             ldr     r1, =str_out
println_loop ldrb    r2, [r0], #1
             strb    r2, [r1], #1
             tst     r2, r2
             bne     println_loop
             mov     r2, #10
             strb    r2, [r1, #-1]
             mov     r2, #0
             strb    r2, [r1]
             mov     r13, r11
             ldmfd   r13!, {r0-r2, r11, pc}

println_char stmfd   r13!, {r0-r2, r11, lr} ; This is PreWritten Code for println(char c)
             mov     r11, r13
             ldr     r0, [r11, #4*5]
             ldr     r1, =str_buf
             str     r0, [r1]
             ldr     r0, =str_buf
             ldr     r1, =str_out
             B       println_loop

main