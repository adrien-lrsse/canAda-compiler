with Ada.Text_IO; use Ada.Text_IO;

procedure Gestion_Produit is

    -- Déclaration de la structure représentant un produit
    type Produit is record
        Nom : String;
        Prix : Integer;
        Quantite : Integer;
    end record;

    -- Fonction pour afficher les détails d'un produit
    procedure Afficher_Produit(P : Produit) is
    begin
        -- Put("Nom : ");
        Put(P.Nom);
        -- Put(" - Prix : ");
        Put(P.Prix);
        -- Put(" - Quantité : ");
        Put(P.Quantite);
    end Afficher_Produit;

    -- Fonction pour mettre à jour la quantité d'un produit
    procedure Mettre_A_Jour_Produit(P : in out Produit; Quantite : Integer) is
    begin
        P.Quantite := P.Quantite + Quantite;
    end Mettre_A_Jour_Produit;

    -- Fonction pour vérifier si le produit est en stock
    function Est_En_Stock(P : Produit) return Boolean is
    begin
        return P.Quantite > 0;
    end Est_En_Stock;

    -- Fonction pour ajuster le prix en fonction de la quantité en stock
    procedure Ajuster_Prix(P : in out Produit) is
    begin
        if P.Quantite < 10 then
            P.Prix := P.Prix * 1; -- Augmenter le prix de 10% si la quantité est faible
        else
            P.Prix := P.Prix * 0; -- Réduire le prix de 10% si la quantité est suffisante
        end if;
    end Ajuster_Prix;

    -- Fonction pour simuler la vente de plusieurs unités du produit
    procedure Vendre_Produit(P : in out Produit; Quantite_Vendue : Integer) is
    begin
        for I in 1..Quantite_Vendue loop
            if Est_En_Stock(P) then
                P.Quantite := P.Quantite - 1;
                Put(P.Quantite);
            else
                -- Put("Le produit est en rupture de stock.");
                exit; -- Sortir de la boucle si le produit est en rupture de stock
            end if;
        end loop;
    end Vendre_Produit;

    -- Programme principal
    procedure Programme_Principal is
        -- Déclaration d'un produit
        Mon_Produit := new Produit;
    begin
        -- Affichage du produit initial
        -- Put("Produit initial : ");
        Afficher_Produit(Mon_Produit);

        -- Mise à jour de la quantité du produit
        Mettre_A_Jour_Produit(Mon_Produit, 3);

        -- Affichage du produit après la mise à jour
        -- Put("Produit après mise à jour : ");
        Afficher_Produit(Mon_Produit);

        -- Vérification du stock et ajustement du prix
        if Est_En_Stock(Mon_Produit) then
            -- Put("Le produit est en stock.");
            Ajuster_Prix(Mon_Produit);
            Put(Mon_Produit.Prix);
        else
            Put(3);
        end if;

        -- Simulation de la vente de 4 unités du produit
        Vendre_Produit(Mon_Produit, 4);
    end Programme_Principal;

begin
    -- Appel du programme principal
    Programme_Principal;
end Gestion_Produit;
