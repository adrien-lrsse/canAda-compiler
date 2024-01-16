with Ada.Text_IO; use Ada.Text_IO;

procedure Programme_Avec_Fonction is
    -- Déclaration de la fonction de calcul de la somme
    function Somme_Entiers(N : Integer) return Integer is
        Resultat : Integer := 0;
    begin
        for I in 1..N loop
            Resultat := Resultat + I;
        end loop;
        return Resultat;
    end Somme_Entiers;

    -- Déclaration des variables
    N, Resultat : Integer;

begin
    -- Saisie de la valeur de N
    N := 15;

    -- Appel de la fonction pour calculer la somme des entiers de 1 à N
    Resultat := Somme_Entiers(N);

    -- Affichage de la somme
   -- Put("La somme des entiers de 1 à ");
    Put(N);
   -- Put(" est ");
    Put(Resultat);
end Programme_Avec_Fonction;
