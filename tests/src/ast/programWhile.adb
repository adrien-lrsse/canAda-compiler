with Ada.Text_IO; use Ada.Text_IO;

procedure Programme_Boucle_While is
    -- Déclaration des variables
    N, Somme, I : Integer;

begin
    -- Initialisation de la somme
    Somme := 0;

    -- Saisie de la valeur de N
    N := 15;

    -- Utilisation d'une boucle while pour calculer la somme des entiers de 1 à N
    I := 1;
    while I <= N loop
        Somme := Somme + I;
        I := I + 1;
    end loop;

    -- Affichage de la somme
    -- Put("La somme des entiers de 1 à ");
    Put(N);
    -- Put(" est ");
    Put(Somme);
end Programme_Boucle_While;
