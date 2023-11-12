with Ada.Text_IO; use Ada.Text_IO;

procedure isNull is

    function isNull(字 : integer) return integer is
    result: integer;
    begin
        if 字 = 0 then
            result := 1;
        else
            result := 0;
        end if;
        return result;
    end isNull;

    --Variables
字 : integer;

    --Main
begin
    字 := 1;
    bool := isNull(字);

    if bool = 1 then
        put_line("Null");
    else
        put_line("Not Null");
    end if;


end Main ;