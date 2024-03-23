with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer := 0;

    function func (p : in out integer) return integer is
        begin
            p := 1;
        end;

    begin
        a := 1;
    end test;
