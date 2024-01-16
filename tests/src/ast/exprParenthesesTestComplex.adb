with Ada.Text_IO; use Ada.Text_IO;

procedure exprParenthesesTestComplex is
begin
   x := (8+3*3*(6-2))/(7-5/(2+1))+16*((((3+(4*5-42)*2)+1)*2)+1);
end exprParenthesesTestComplex;