with Ada.Text_IO; use Ada.Text_IO;

procedure Gestion_Produit is

    Coix : Interger;

    -- Déclaration de la structure représentant un produit
    type Produit is record
        Nom : String;
    end record;

    -- Programme principal
    procedure Programme_Principal is
        Mon_Produit : Produit := new Produit;
    begin
        Put(Mon_Produit.Nom);
    end Programme_Principal;

begin
    -- Appel du programme princiProgramme_Principalpal
    Programme_Principal;
end Gestion_Produit;
