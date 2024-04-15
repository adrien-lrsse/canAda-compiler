with Ada.Text_IO; use Ada.Text_IO;

procedure IDENT0 is

    IDENT : Integer;
    type Produit;

    function AireRectangle(Larg : Integer; Long : Integer) return Integer is
        Aire : Integer;
    begin
        Aire := Larg * Long;
        return Aire;
    end AireRectangle;

    -- Déclaration de la structure représentant un produit
    type Produit is record
        Nom : String;
        Prix : Integer;
        Quantite : Integer;
    end record;
    
    begin

IDENT :=1;

end;
