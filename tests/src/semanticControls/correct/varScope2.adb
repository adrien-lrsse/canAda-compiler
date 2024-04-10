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

    procedure proc (p : integer) is

        c : integer := 2;

        begin
            p := c + 1;
        end;

    begin
        a := func(1);
        proc(a);
    end test;
