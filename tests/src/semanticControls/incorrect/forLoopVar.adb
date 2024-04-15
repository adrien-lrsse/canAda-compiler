with Ada.Text_IO; use Ada.Text_IO;

procedure test is

    a : integer;
    b : integer;

    function func (p : integer) return integer is

        begin
            for i in 1..10 loop
                i := p;
            end loop;
            return p;
        end;

    begin
        a := func(1);
    end test;
