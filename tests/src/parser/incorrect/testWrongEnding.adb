with Ada.Text_IO; use Ada.Text_IO;

procedure IDENT0 is

    begin
        if 1 then
            IDENT1 := 1;
        elsif 1 then
            IDENT2 := 1;
            IDENT3 := 1;
        end loop;
    end;
