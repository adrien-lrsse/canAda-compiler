with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer;
    b : integer;

    function func1 (p : integer) return integer is

        begin
            return p + 1;
        end func1;

    function func2 (p : integer) return integer is

        begin
            return p + 2;
        end func2;

    begin
        a := func1(1);
    end test;
