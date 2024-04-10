with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer;
    b : integer;

    function func (p : integer) return integer is

        c : integer := 2;

        begin
            c := 2 * p + 1;
            return c + p;
        end;

    begin
        a := func(1);
        c := func(2);
    end test;
