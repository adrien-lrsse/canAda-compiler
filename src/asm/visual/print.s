; ########### Print a string ##*#########
str_out      FILL    0x1000
str_hello    DCD     0x6C6C6548, 0x77202C6F, 0x646C726F, 0x21
start        B       main

             ;R0     contains the address of the null-terminated string to print
print        STMFA   SP!, {R0-R2}
             LDR     R1, =str_out ; address of the output buffer
print_loop   LDRB    R2, [R0], #1
             STRB    R2, [R1], #1
             TST     R2, R2
             BNE     print_loop
             LDMFA   SP!, {R0-R2}
             LDR     PC, [R13, #-4]!

             ;R0     contains the address of the null-terminated string to print
println      STMFA   SP!, {R0-R2}
             LDR     R1, =str_out
println_loop LDRB    R2, [R0], #1
             STRB    R2, [R1], #1
             TST     R2, R2
             BNE     println_loop
             MOV     R2, #10
             STRB    R2, [R1, #-1]
             MOV     R2, #0
             STRB    R2, [R1]
             LDMFA   SP!, {R0-R2}
             LDR     PC, [R13, #-4]!

main
