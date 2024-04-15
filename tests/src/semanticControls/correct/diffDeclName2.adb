with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer := 0;

    type struct;

    b : integer := 0;

    type struct is
        record
            champsA : integer;
            champsB : integer;
        end record;

    s : struct;

    function func (p : struct) return integer is
        begin
            return p.champsA + 1;
        end;

    begin
        s.champsA := 1;
        s.champsB := 2;
        a := func(s);
    end test;
