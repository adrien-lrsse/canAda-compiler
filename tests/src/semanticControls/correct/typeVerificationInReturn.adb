with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer;
    b : integer;

    function func (p : integer) return integer is

        c : integer := 1;

        begin
            return c + p;
        end;

    begin
        a := func(1);
    end test;
