with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer;
    b : integer;

    function func (p : in out integer) return integer is
        begin
            return p + 1;
        end;

    begin
        a := 1;
        b := func(1);
    end test;
