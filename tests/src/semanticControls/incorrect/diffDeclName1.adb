with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer;
    b : integer;

    function func (p : integer) return integer is

        begin
            return p + 1;
        end func;

    function func (p : integer) return integer is

        begin
            return p + 2;
        end func2;

    begin
        a := func(1);
    end test;
