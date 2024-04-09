with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer;
    b : integer;

    function func (p : in integer) return integer is

        begin
            p := 1;
            return p;
        end;

    begin
        a := func(1);
    end test;
