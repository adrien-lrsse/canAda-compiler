with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer := 0;
    b : integer := 0;

    type struct is
        record
            champsA : integer;
            champsB : integer;
        end record;

    function func (p : in out integer) return integer is
        begin
            return p + 1;
        end;

    s : struct;

    begin
        a := 1;
        s.champsA := 2;
        b := func(1);
    end test;
