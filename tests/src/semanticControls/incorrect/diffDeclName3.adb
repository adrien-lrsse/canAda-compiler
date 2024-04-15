with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer;
    b : integer;

    procedure proc (p : integer) is

        c : integer := 2;

        begin
            p := c + 1;
        end;

    procedure proc (p : integer) is

        c : integer := 5;

        begin
            p := c + 1;
        end;

    begin
        proc(a);
    end test;
