with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer;
    b : boolean;
    c : character;

    type struct is record
        champs1 : integer;
        champs2 : boolean;
        champs3 : character;
    end record;

    d : struct;

    function func (x : integer) return integer is
    begin
        return 1;
    end func;

    begin
        a := 1;
    end test;
