with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer;
    b : integer;

    function func (p1 : integer; p2 : integer; p3 : character) return integer is
        begin
            put(p3);
            return p1 + p2;
        end;

    begin
        a := func(1, 'a', 2);
    end test;
