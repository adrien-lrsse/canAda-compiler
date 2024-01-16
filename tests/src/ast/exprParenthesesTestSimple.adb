with Ada.Text_IO; use Ada.Text_IO;

procedure exprParenthesesTestSimple is
begin
   x := 2+(3*4);
   y := 2*(3+4);
   z := (2+3)*4;
   a := (2+3)*(4+5);
end exprParenthesesTestSimple;