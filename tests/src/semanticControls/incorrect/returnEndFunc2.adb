with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer;
    b : integer;

    function func (p : integer) return integer is

        begin
            if p = 1 then
                return 1;
            elsif p = 2 then
                return 2;
            else
                p := p - 1;
            end if;
        end;

    begin
        a := func(1);
    end test;
