:- dynamic blancs/1.
:- dynamic noirs/1.


%%%% Verify if the place has a pion
isPion(X,Y,Player) :- Player=='Blancs', blancs(Blancs), member([X, Y, _],Blancs).
isPion(X,Y,Player) :- Player=='Noirs', noirs(Noirs), member([X, Y, _],Noirs).

%%%% DISPLAY
display1(I,Taille, [Liste|R2]) :- I<Taille, !,Y is I+1,display3(0,Taille,I,Liste), writeln(Liste), display1(Y,Taille, R2).
display1(_,_,[]).
display3(X,Taille,Ligne,[V|R]):-X<Taille,!,Y is X+1, display3(Y,Taille,Ligne,R), dessineCase(X, Ligne, V).
display3(_,_,_,[]).

dessineCase(X,Y, 'b') :- isPion( X,Y,'Blancs'),!.
dessineCase(X,Y, 'n') :- isPion( X,Y,'Noirs'),!.
dessineCase(_,_, '_').

ajoutLigne(Ligne,R6):-mod(Ligne,2) =:= 0,
	X is 1,append([],[[X,Ligne,pion]],R2),
	X2 is X+2,append(R2,[[X2,Ligne,pion]],R3),
	X3 is X2+2,append(R3,[[X3,Ligne,pion]],R4),
	X4 is X3+2,append(R4,[[X4,Ligne,pion]],R5),
	X5 is X4+2,append(R5,[[X5,Ligne,pion]],R6).

ajoutLigne(Ligne,R6):-mod(Ligne,2) =:= 1,
	X is 0,append([],[[X,Ligne,pion]],R2),
	X2 is X+2,append(R2,[[X2,Ligne,pion]],R3),
	X3 is X2+2,append(R3,[[X3,Ligne,pion]],R4),
	X4 is X3+2,append(R4,[[X4,Ligne,pion]],R5),
	X5 is X4+2,append(R5,[[X5,Ligne,pion]],R6).


ajouterListe(Ldebut,Lfin,R):- Ldebut =< Lfin,!,ajoutLigne(Ldebut,R1), Suite is Ldebut+1,ajouterListe(Suite,Lfin,R2),append(R1,R2,R).
ajouterListe(_,_,[]).

creerListe(noir,L):-ajouterListe(0,3,L).
creerListe(blanc,L):-ajouterListe(6,9,L).

% cherche les possiblit�s pour manger un pion dans chaque direction et
% retourne le chemin + l'etat du jeu.
% pour un Pion blanc
mangeTouteDirection([X,Y,pion],blanc,Blancs,Noirs,R):-mangeHautGauche([X,Y,pion],blanc,Blancs,Noirs,R1),mangeHautDroite(),mangeBasGauche(),mangeBasDroite().


%mouvement pour un pion blanc
moveHautGauche([X,_,_],_,_,_,[]):- X2 is X-1,X2<0,!.
moveHautGauche([_,Y,_],_,_,_,[]):- Y2 is Y-1,Y2<0,!.
moveHautGauche([X,Y,pion],blanc,Blancs,_,[]):-X2 is X-1, Y2 is Y-1,member([X2,Y2,_],Blancs),!.
moveHautGauche([X,Y,pion],blanc,Blancs,Noirs,[[X2,Y2,pion]|Blancs]):-X2 is X-1, Y2 is Y-1,member([X2,Y2,_],Noirs),mangeHautGauche().%a finir


%mouvement pour une dame blanc (a faire)
moveHautGauche([X,Y,dame],blanc,Blancs,Noirs,[[X2,Y2,dame]|Blancs]):-X2 is X-1, Y2 is Y-1, X2>=0,Y2>=0,not(member([X2,Y2,_],Blancs)),!,not(member([X2,Y2,_],Noirs)).

% R est du type [[Blancs,Noirs,Elem,[[posX,posY],...]],...]
% a changer S par R1 quand les predicats seront cr��s
movePossible(E,Camp,Blancs,Noirs,S):-moveHautGauche(E,Camp,Blancs,Noirs,S).%,moveHautDroite(R2),moveBasGauche(R3),moveBasDroite(R4),append(R1,R2,R5),append(R5,R3,R6),append(R6,R4,S).


%methode naive on prend la premiere solution trouvee
choixMove([[Noirs,Blancs]|_],Noirs,Blancs):-!.
choixMove(_,[],[]).


ia(blanc,Noirs,Blancs,Noirs2,Blancs2):-repeat,length(Blancs,X),Index is random(X),nth0(Index,Blancs,E),subtract(Blancs,[E],Blancs3),movePossible(E,blanc,Blancs3,Noirs,L),choixMove(L,Noirs2,Blancs2).

ia(noir,Noirs,Blancs,Noirs2,Blancs2).


init :-creerListe(noir,L1),creerListe(blanc,L2),assert(noirs(L1)),assert(blancs(L2)).%, play('b').

