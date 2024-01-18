with Ada.Text_IO; use Ada.Text_IO;

procedure isNull is

    function isNull(n : integer) return integer is
    result: integer;
    begin
        if n = 0 then
            result := 2147483647;
        else
            result := -2147483647;
        end if;
        return result;
    end isNull;

    --Variables
n : integer;

    --Main
begin
    n := 2147483648;
    bool := isNull(n);

    if bool = 1 then
        put(Null); --put_line("Null");
    else
        put(NotNull); --put_line("Not Null");
    end if;


end Main ;