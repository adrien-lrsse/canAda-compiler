with Ada.Text_IO; use Ada.Text_IO;

procedure isNull is

    function isNull(n : integer) return integer is
    result: integer;
    begin
        if n = 0 then
            result := 1;
        else
            result := 0;
        end if;
        return result;
    end isNull;

    --Variables
n : integer;

    --Main
begin
    n := 1;
    bool := isNull(n);

    if bool = 1 then
        put_line("Null");
    else
        put_line("Not Null");
    end if;


end Main ;