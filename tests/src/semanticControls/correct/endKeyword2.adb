with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : Integer;

    function func(a : integer) return Integer is
        begin
            return a;
        end func;

    begin
        a := func(5);
    end test;