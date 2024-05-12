with Ada.Text_IO; use Ada.Text_IO;

procedure UnDebut is

    function AireRectangle(Larg : Integer; Long : Integer) return Integer is
        Aire : Integer;
    begin
        Aire := Larg * Long;
        put(Aire);
        return Aire;
    end AireRectangle;

    function PerimetreRectangle(Larg : Integer; Long : Integer) return Integer is
        P : Integer;
    begin
        P := Larg * 2 + Long * 2;
        put(P);
        return P;
    end PerimetreRectangle;

    -- VARIABLES
    Choix : Integer;
    Valeur : Integer;

    -- PROCEDURE PRINCIPALE
begin
    Choix := 1;
    put(Choix);
    valeur := 3;
    Put(Valeur);


    PerimetreRectangle(choix, valeur);
    AireRectangle(choix, valeur);


end UnDebut;