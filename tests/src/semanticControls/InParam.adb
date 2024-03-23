with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer := 0;

    type struct is
        record
            champsA : integer;
            champsB : integer;
        end record;

    function func (p : in struct) return integer is
        begin
            p.champsA := 1;
        end;

    begin
        a := 1;
    end test;
