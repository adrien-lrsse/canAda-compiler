with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer;
    b : integer;

    function func (p : character) return integer is
        begin
            return p;
        end;

    begin
        a := func('a');
    end test;
