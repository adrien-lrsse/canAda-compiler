with Ada.Text_IO; use Ada.Text_IO;

procedure UneFin is

    -- TYPES
    type PK is record -- Public Key
        N : Integer;
        e : Integer;
    end record;

    type SK is record -- Secret Key
        p : Integer;
        q : Integer;
        d : Integer;
    end record;

    type Key is record -- RSA Key
        pub : PK;
        sec : SK;
    end record;

    -- VARIABLES
    K : Key;

    -- FONCTIONS
    function Pow (X : Integer; N : Integer; M : Integer) return Integer
    is  -- Exponentiating by squaring (X^N mod M)
    begin
        if N = 0 then
            return 1;
        elsif N rem 2 = 0 then
            return Pow (X * X rem M, N / 2, M) rem M;
        else
            return X * Pow (X * X rem M, (N - 1) / 2, M) rem M;
        end if;
    end Pow;

    function KeyGen (p : Integer; q : Integer; e : Integer) return Key
    is -- Key generation
        type Bezout is record
            d : Integer;
            u : Integer;
            v : Integer;
        end record;

        K   : Key;
        Phi : Integer := (p - 1) * (q - 1);
        Bez : Bezout;

        function Euclide (a : Integer; b : Integer) return Bezout
        is -- Extended Euclidean algorithm
            tmp : Bezout;
            Res : Bezout;
        begin
            if b = 0 then
                Res.d := a;
                Res.u := 1;
                Res.v := 0;
                return Res;
            else
                tmp   := Euclide (b, a rem b);
                Res.d := tmp.d;
                Res.u := tmp.v;
                Res.v := tmp.u - (a / b) * tmp.v;
                return Res;
            end if;
        end Euclide;
    begin
        K.pub.N := p * q;
        K.pub.e := e;

        K.sec.p := p;
        K.sec.q := q;

        Bez := Euclide (Phi, e);
        if Bez.v < 0 then
            K.sec.d := Bez.v + Phi;
        else
            K.sec.d := Bez.v;
        end if;
        return K;
    end KeyGen;

    function Encrypt (M : Integer; K : PK) return Integer is -- Encryption
    begin
        return Pow (M, K.e, K.N);
    end Encrypt;

    function Decrypt (C : Integer; K : SK) return Integer is -- Decryption
    begin
        return Pow (C, K.d, K.p * K.q);
    end Decrypt;

    -- PROCEDURE PRINCIPALE
begin

    K := KeyGen (19, 23, 5);

    for I in 1 .. 10 loop
        Put (I);
        Put ('>');
        if Decrypt (Encrypt (I, K.pub), K.sec) = I then
            Put (character'Val (111)); -- o: ok

            if not (I = 10) and then I >= 0 then
                Put (' ');
            end if;
        else
            Put (character'Val (107)); -- x: error

            if True or else 1 / 0 = 0 then
                Put (' ');
            end if;
        end if;
    end loop;

end UneFin;
