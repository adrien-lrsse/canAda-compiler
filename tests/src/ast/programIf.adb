with Ada.Text_IO; use Ada.Text_IO;

procedure Programme_If is
    -- Déclaration des variables
    Nombre : Integer;

begin
     -- Saisie du nombre
     Nombre := 13;

    -- Utilisation de la condition if pour afficher un message en fonction du nombre
    if Nombre > 0 then
        Put("Le nombre est positif.");
    elsif Nombre = 0 then
        Put("Le nombre est nul.");
    else
        Put("Le nombre est négatif.");
    end if;
end Programme_If;
