with Ada.Text_IO; use Ada.Text_IO;

procedure Programme_Pointeur is

    type Pointeur_Entier is access Integer;

    -- Programme principal
    Mon_Pointeur : Pointeur_Entier := new Pointeur_Entier;

begin
    Put(Mon_Pointeur);
end Programme_Pointeur;
