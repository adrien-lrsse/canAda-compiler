with Ada.Text_IO; use Ada.Text_IO;

procedure UnDebut is

    function AireRectangle(Larg : Integer; Long : Integer) return Integer is
        Aire : Integer;
    begin
        Aire := 2 * 3;
        return Aire;
    end AireRectangle;

    function PerimetreRectangle(Larg : Integer; Long : Integer) return Integer is
        P : Integer;
    begin
        P := 1 * 2 + 3 * 2;
        return P;
    end PerimetreRectangle;

    -- VARIABLES
    Choix : Integer;
    Valeur : Integer;

    -- PROCEDURE PRINCIPALE
begin
    Choix := 2;

    if Choix = 1 then
        Valeur := PerimetreRectangle(2, 3) * AireRectangle(2, 3);
        Put(Valeur);
    else
        Valeur := AireRectangle(2, 3);
        Put(Valeur);
    end if;
end UnDebut;