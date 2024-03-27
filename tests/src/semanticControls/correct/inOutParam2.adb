with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer := 0;
    b : integer := 0;

    type struct is
        record
            champsA : integer;
            champsB : integer;
        end record;

    s : struct;

    function func (p : in out integer) return integer is
        begin
            return p + 1;
        end;

    begin
        a := 1;
        b := func(s.champsA);
    end test;
