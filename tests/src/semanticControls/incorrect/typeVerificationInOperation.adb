with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer := 0;
    b : integer := 0;
    c : character := 'a';
    d : character := 'b';
    e : boolean := true;
    f : boolean := false;

    op1 : integer;
    op2 : character;
    op3 : boolean;

    begin
        op1 := a + c;
        op2 := c + d;
        op3 := e + f;
    end test;
