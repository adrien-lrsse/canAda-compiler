with Ada.Text_IO; use Ada.Text_IO;

procedure laTotale is
    -- Déclaration des variables
    N : Integer;

begin
    -- Saisie du nombre
    N := 13;

    -- Utilisation d'une boucle for pour afficher les carrés des nombres de 1 à N
    -- Put("Les carrés sont : ");
    for I in 1..N loop
        Put(I * I);
    end loop;

    -- Utilisation d'une boucle while pour afficher les carrés des nombres de 1 à N
    -- Put("Les carrés sont : ");
    I := 1;
    while I <= N loop
        Put(I * I);
        I := I + 1;
    end loop;

    -- Utilisation de la condition if pour afficher un message en fonction du nombre
    if N > 0 then
        Put(1);
    elsif N = 0 then
        Put(0);
    else
        Put(-1);
    end if;
end laTotale;