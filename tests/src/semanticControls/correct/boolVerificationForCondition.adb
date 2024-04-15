with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer;
    b : integer;
    c : boolean;

    begin
        if a = b then
            a := a + 1;
        elsif a > b then
            a := a - 1;
        else
            a := a * 2;
        end if;

        while a < b loop
            a := a + 1;
        end loop;

        c := true;

        if c then
            a := a + 1;
        end if;
    end test;
