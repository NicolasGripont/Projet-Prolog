:- dynamic blancs/1.
:- dynamic noirs/1.
:- dynamic cptDraw/1.


%%%% Test is the game is finished %%%
gameover(Blancs,Noirs,_,_,Winner) :- winner(Winner, Blancs, Noirs), !.  % There exists a winning configuration: We cut!
gameover(Blancs,Noirs,Blancs2,Noirs2,egalite) :- length(Noirs, X), length(Noirs2, X), length(Blancs, Y), length(Blancs2, Y),
										cptDraw(CptDraw), NewCptDraw is CptDraw+1, retractall(cptDraw(_)), assert(cptDraw(NewCptDraw)),
										NewCptDraw == 25,!.
gameover(Blancs,Noirs,Blancs2,Noirs2,continuer) :- length(Noirs, X), length(Noirs2, X), length(Blancs, Y), length(Blancs2, Y),!.
gameover(_,_,_,_,continuer) :- retractall(cptDraw(_)), assert(cptDraw(0)).

%%%% Test de victoire pour les joueurs.
winner(blanc, Blancs, Noirs) :- (length(Noirs, 0);(movePossiblePlayer(noir, Blancs, Noirs, Noirs, [],_))), !.
winner(noir, Blancs, Noirs) :- (length(Blancs, 0);(movePossiblePlayer(blanc, Blancs, Noirs, Blancs, [],_))), !.

%%%% Predicate to get the next player
changePlayer(blanc,noir).
changePlayer(noir,blanc).

%%%% Verify if the place has a pion
isPion(X,Y,blanc) :- blancs(Blancs), member([X, Y, pion],Blancs).
isPion(X,Y,noir) :- noirs(Noirs), member([X, Y, pion],Noirs).

%%%% Verify if the place has a dame
isDame(X,Y,blanc) :- blancs(Blancs), member([X, Y, dame],Blancs).
isDame(X,Y,noir) :- noirs(Noirs), member([X, Y, dame],Noirs).

%%%% DISPLAY
display1(I,Taille, [Liste|R2]) :- I<Taille, !,Y is I+1,display3(0,Taille,I,Liste), writeln(Liste), display1(Y,Taille, R2).
display1(_,_,[]).
display3(X,Taille,Ligne,[V|R]):-X<Taille,!,Y is X+1, display3(Y,Taille,Ligne,R), dessineCase(X, Ligne, V).
display3(_,_,_,[]).

%%%% Dessine la contenu de la case
dessineCase(X,Y, 'b') :- isPion( X,Y,blanc),!.
dessineCase(X,Y, 'n') :- isPion( X,Y,noir),!.
dessineCase(X,Y, 'B') :- isDame( X,Y,blanc),!.
dessineCase(X,Y, 'N') :- isDame( X,Y,noir),!.
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

%cherche le premier pion ou dame adverse dans la diagonale
cherchePionHautGauche(X,Y,blanc,Blancs,Noirs,[X1,Y1,T],Manges):-X1 is X-1, Y1 is Y-1, X1>=0, Y1>=0, not(member([X1,Y1,_],Blancs)),
		not(member([X1,Y1,_],Manges)),member([X1,Y1,T],Noirs),!.
cherchePionHautGauche(X,Y,noir,Blancs,Noirs,[X1,Y1,T],Manges):-X1 is X-1, Y1 is Y-1, X1>=0, Y1>=0, not(member([X1,Y1,_],Noirs)),
		not(member([X1,Y1,_],Manges)),member([X1,Y1,T],Blancs),!.
cherchePionHautGauche(X,Y,Camp,Blancs,Noirs,[X2,Y2,T],Manges):-X1 is X-1, Y1 is Y-1, X1>=0, Y1>=0, not(member([X1,Y1,_],Blancs)),
		not(member([X1,Y1,T],Noirs)),not(member([X1,Y1,_],Manges)),cherchePionHautGauche(X1,Y1,Camp,Blancs,Noirs,[X2,Y2,T]).
		
cherchePionHautDroite(X,Y,blanc,Blancs,Noirs,[X1,Y1,T],Manges):-X1 is X+1, Y1 is Y-1, X1=<9, Y1>=0, not(member([X1,Y1,_],Blancs)),
		not(member([X1,Y1,_],Manges)),member([X1,Y1,T],Noirs),!.
cherchePionHautDroite(X,Y,noir,Blancs,Noirs,[X1,Y1,T],Manges):-X1 is X+1, Y1 is Y-1, X1=<9, Y1>=0, not(member([X1,Y1,_],Noirs)),
		not(member([X1,Y1,_],Manges)),member([X1,Y1,T],Blancs),!.
cherchePionHautDroite(X,Y,Camp,Blancs,Noirs,[X2,Y2,T],Manges):-X1 is X+1, Y1 is Y-1, X1=<9, Y1>=0, not(member([X1,Y1,_],Blancs)),
		not(member([X1,Y1,T],Noirs)),not(member([X1,Y1,_],Manges)),cherchePionHautDroite(X1,Y1,Camp,Blancs,Noirs,[X2,Y2,T]).

cherchePionBasGauche(X,Y,blanc,Blancs,Noirs,[X1,Y1,T],Manges):-X1 is X-1, Y1 is Y+1, X1>=0, Y1=<9, not(member([X1,Y1,_],Blancs)),
		not(member([X1,Y1,_],Manges)),member([X1,Y1,T],Noirs),!.
cherchePionBasGauche(X,Y,noir,Blancs,Noirs,[X1,Y1,T],Manges):-X1 is X-1, Y1 is Y+1, X1>=0, Y1=<9, not(member([X1,Y1,_],Noirs)),
		not(member([X1,Y1,_],Manges)),member([X1,Y1,T],Blancs),!.
cherchePionBasGauche(X,Y,Camp,Blancs,Noirs,[X2,Y2,T],Manges):-X1 is X-1, Y1 is Y+1, X1>=0, Y1=<9, not(member([X1,Y1,_],Blancs)),
		not(member([X1,Y1,T],Noirs)),not(member([X1,Y1,_],Manges)),cherchePionBasGauche(X1,Y1,Camp,Blancs,Noirs,[X2,Y2,T]).
		
cherchePionBasDroite(X,Y,blanc,Blancs,Noirs,[X1,Y1,T],Manges):-X1 is X+1, Y1 is Y+1, X1=<9, Y1=<9, not(member([X1,Y1,_],Blancs)),
		not(member([X1,Y1,_],Manges)),member([X1,Y1,T],Noirs),!.
cherchePionBasDroite(X,Y,noir,Blancs,Noirs,[X1,Y1,T],Manges):-X1 is X+1, Y1 is Y+1, X1=<9, Y1=<9, not(member([X1,Y1,_],Noirs)),
		not(member([X1,Y1,_],Manges)),member([X1,Y1,T],Blancs),!.
cherchePionBasDroite(X,Y,Camp,Blancs,Noirs,[X2,Y2,T],Manges):-X1 is X+1, Y1 is Y+1, X1=<9, Y1=<9, not(member([X1,Y1,_],Blancs)),
		not(member([X1,Y1,T],Noirs)),not(member([X1,Y1,_],Manges)),cherchePionBasDroite(X1,Y1,Camp,Blancs,Noirs,[X2,Y2,T]).
%tests
% cherchePionHautGauche(4,4,blanc,[[4,4,pion]],[[1,1,pion],[3,3,pion]],R).
% cherchePionHautGauche(4,4,blanc,[[4,4,pion]],[[1,1,pion],[3,1,pion]],R).
% cherchePionHautGauche(4,4,blanc,[[4,4,pion]],[[2,1,pion],[3,1,pion]],R).

%cherche les cases vides dans la diagonale
chercheCaseVideHautGauche(X,Y,Blancs,Noirs, Manges, [[X1,Y1]|L]) :-
    X1 is X-1, Y1 is Y-1, X1>=0, Y1>=0,
    not(member([X1,Y1,_],Blancs)),
    not(member([X1,Y1,_],Noirs)),
    not(member([X1,Y1],Manges)),
    chercheCaseVideHautGauche(X1,Y1,Blancs,Noirs,Manges,L),!.
chercheCaseVideHautGauche(_,_,_,_,_,[]).

%tests
% chercheCaseVideHautGauche(4,4,[[4,4,pion]],[[1,1,pion]], [], R).
% chercheCaseVideHautGauche(4,4,[[4,4,pion]],[[1,1,pion],[3,3,pion]], [], R).
% chercheCaseVideHautGauche(4,4,[],[],[[2,2]],R).

creerMouvement(B,N,[],[[B,N,[]]]):-!.
creerMouvement(_,_,S,S).
%tests
%creerMouvement([],[],[],R).

ajoutMouvement(Pos,[[Blancs,Noirs,ListeMouvement]|Suite],[[Blancs,Noirs,[Pos|ListeMouvement]]|R]):-!,ajoutMouvement(Pos,Suite,R).
ajoutMouvement(_,[],[]).
%Tests
%ajoutMouvement([1,2],[[[],[],[[0,0]]],[[],[],[[1,1]]]],R).
%ajoutMouvement([1,2],[[[],[],[[0,0]]],[[],[],[[1,1]]]],R).

%pion
mangeBasGauche([X,Y,pion],blanc,Blancs,Noirs,Retour):- X2 is X-1, Y2 is Y+1,X3 is X-2,Y3 is Y+2,X3 >= 0 ,Y3 =< 9,member([X2,Y2,_],Noirs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Noirs,[X2,Y2,_],Noirs2),delete(Blancs,[X,Y,pion],Blancs2),mangeTouteDirection([X3,Y3,pion],blanc,[[X3,Y3,pion]|Blancs2],Noirs2,R),creerMouvement([[X3,Y3,pion]|Blancs2],Noirs2,R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeBasGauche([X,Y,pion],noir,Blancs,Noirs,Retour):- X2 is X-1, Y2 is Y+1,X3 is X-2,Y3 is Y+2,X3 >= 0 ,Y3 =< 9,member([X2,Y2,_],Blancs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Blancs,[X2,Y2,_],Blancs2),delete(Noirs,[X,Y,pion],Noirs2),mangeTouteDirection([X3,Y3,pion],noir,Blancs2,[[X3,Y3,pion]|Noirs2],R),creerMouvement(Blancs2,[[X3,Y3,pion]|Noirs2],R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeBasGauche(_,_,_,_,[]).

mangeBasDroite([X,Y,pion],blanc,Blancs,Noirs,Retour):- X2 is X+1, Y2 is Y+1,X3 is X+2,Y3 is Y+2,X3 =< 9 ,Y3 =< 9,member([X2,Y2,_],Noirs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Noirs,[X2,Y2,_],Noirs2),delete(Blancs,[X,Y,pion],Blancs2),mangeTouteDirection([X3,Y3,pion],blanc,[[X3,Y3,pion]|Blancs2],Noirs2,R),creerMouvement([[X3,Y3,pion]|Blancs2],Noirs2,R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeBasDroite([X,Y,pion],noir,Blancs,Noirs,Retour):- X2 is X+1, Y2 is Y+1,X3 is X+2,Y3 is Y+2,X3 =< 9 ,Y3 =< 9,member([X2,Y2,_],Blancs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Blancs,[X2,Y2,_],Blancs2),delete(Noirs,[X,Y,pion],Noirs2),mangeTouteDirection([X3,Y3,pion],noir,Blancs2,[[X3,Y3,pion]|Noirs2],R),creerMouvement(Blancs2,[[X3,Y3,pion]|Noirs2],R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeBasDroite(_,_,_,_,[]).

mangeHautDroite([X,Y,pion],blanc,Blancs,Noirs,Retour):- X2 is X+1, Y2 is Y-1,X3 is X+2,Y3 is Y-2,X3 =< 9 ,Y3 >= 0,member([X2,Y2,_],Noirs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Noirs,[X2,Y2,_],Noirs2),delete(Blancs,[X,Y,pion],Blancs2),mangeTouteDirection([X3,Y3,pion],blanc,[[X3,Y3,pion]|Blancs2],Noirs2,R),creerMouvement([[X3,Y3,pion]|Blancs2],Noirs2,R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeHautDroite([X,Y,pion],noir,Blancs,Noirs,Retour):- X2 is X+1, Y2 is Y-1,X3 is X+2,Y3 is Y-2,X3 =< 9 ,Y3 >= 0,member([X2,Y2,_],Blancs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Blancs,[X2,Y2,_],Blancs2),delete(Noirs,[X,Y,pion],Noirs2),mangeTouteDirection([X3,Y3,pion],noir,Blancs2,[[X3,Y3,pion]|Noirs2],R),creerMouvement(Blancs2,[[X3,Y3,pion]|Noirs2],R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeHautDroite(_,_,_,_,[]).

mangeHautGauche([X,Y,pion],blanc,Blancs,Noirs,Retour):- X2 is X-1, Y2 is Y-1,X3 is X-2,Y3 is Y-2,X3 >= 0,Y3 >= 0,member([X2,Y2,_],Noirs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Noirs,[X2,Y2,_],Noirs2),delete(Blancs,[X,Y,pion],Blancs2),mangeTouteDirection([X3,Y3,pion],blanc,[[X3,Y3,pion]|Blancs2],Noirs2,R),creerMouvement([[X3,Y3,pion]|Blancs2],Noirs2,R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeHautGauche([X,Y,pion],noir,Blancs,Noirs,Retour):- X2 is X-1, Y2 is Y-1,X3 is X-2,Y3 is Y-2,X3 >= 0,Y3 >= 0,member([X2,Y2,_],Blancs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Blancs,[X2,Y2,_],Blancs2),delete(Noirs,[X,Y,pion],Noirs2),mangeTouteDirection([X3,Y3,pion],noir,Blancs2,[[X3,Y3,pion]|Noirs2],R),creerMouvement(Blancs2,[[X3,Y3,pion]|Noirs2],R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeHautGauche(_,_,_,_,[]).
%test mangehautgauche
% mangeHautGauche([4,4,pion],blanc,[[4,4,pion]],[[1,1,pion],[3,3,pion]],R).

%

%dame
mangeHautGauche2(blanc,Blancs,Noirs,[[X,Y]|Suite],Manges,Retour):-mangeHautGauche2(blanc,Blancs,Noirs,Suite,Manges,Solution1),
		mangeTouteDirection([X,Y,dame],blanc,Blancs,Noirs,Manges,R),
		maxSolution(Solution1,R,S),
		creerMouvement(Blancs,Noirs,S,Sortie),ajoutMouvement([X,Y],Sortie,Retour).
mangeHautGauche2(_,_,_,[],_,[]).

mangeHautGauche([X,Y,dame],blanc,Blancs,Noirs,Manges,Retour):- cherchePionHautGauche(X,Y,blanc,Blancs,Noirs,[X2,Y2,T]),
		chercheCaseVideHautGauche(X,Y,Blancs,Noirs, [], R),R \== [],!, delete(Noirs,[X2,Y2,T],Noirs2),
		delete(Blancs,[X,Y,dame],Blancs2),mangeHautGauche2(blanc,Blancs2,Noirs2,R,[[X2,Y2,dame]|Manges],Retour).
mangeHautGauche([X,Y,dame],Camp,Blancs,Noirs,Retour):-mangeHautGauche([X,Y,dame],Camp,Blancs,Noirs,[],Retour),!.



longueurChaine([],0).
longueurChaine([[_,_,T]|_],X):-length(T,X).

estSolution(Liste,Maxi,Maxi,Liste):-!.
estSolution(_,_,_,[]).

maxSolution(R1,R2,S):-longueurChaine(R1,X),longueurChaine(R2,Y),max_list([X,Y],Maxi),estSolution(R1,X,Maxi,S1),estSolution(R2,Y,Maxi,S2),
		append(S1,S2,S).
maxSolution(R1,R2,R3,R4,S):-longueurChaine(R1,X),longueurChaine(R2,Y),longueurChaine(R3,Z),longueurChaine(R4,W),max_list([X,Y,Z,W],Maxi),
		estSolution(R1,X,Maxi,S1),estSolution(R2,Y,Maxi,S2),estSolution(R3,Z,Maxi,S3),estSolution(R4,W,Maxi,S4),
		append(S1,S2,S5),append(S5,S3,S6),append(S6,S4,S).

maxSolutionJoueur([[Pion1,T1]|Suite3],[[Pion2,T2]|Suite4],S):-longueurChaine(T1,X),longueurChaine(T2,Y),max_list([X,Y],Maxi),estSolution([[Pion1,T1]|Suite3],X,Maxi,S1),estSolution([[Pion2,T2]|Suite4],Y,Maxi,S2),append(S1,S2,S).

% cherche les possiblités pour manger un pion dans chaque direction et
% retourne les solutions possibles comprenant : le chemin + l'etat du
% jeu.
% pour un Pion
mangeTouteDirection([X,Y,pion],Couleur,Blancs,Noirs,R):-mangeHautGauche([X,Y,pion],Couleur,Blancs,Noirs,R1),
		mangeHautDroite([X,Y,pion],Couleur,Blancs,Noirs,R2),mangeBasGauche([X,Y,pion],Couleur,Blancs,Noirs,R3),
		mangeBasDroite([X,Y,pion],Couleur,Blancs,Noirs,R4),maxSolution(R1,R2,R3,R4,R).
mangeTouteDirection([X,Y,dame],Couleur,Blancs,Noirs,Manges,R):-mangeHautGauche([X,Y,dame],Couleur,Blancs,Noirs,Manges,R1),
		mangeHautDroite([X,Y,dame],Couleur,Blancs,Noirs,Manges,R2),mangeBasGauche([X,Y,dame],Couleur,Blancs,Noirs,Manges,R3),
		mangeBasDroite([X,Y,dame],Couleur,Blancs,Noirs,Manges,R4),maxSolution(R1,R2,R3,R4,R).
%tests
% mangeTouteDirection([4,2,pion],blanc,[[4,2,pion]],[[5,1,pion],[3,1,pion],[1,1,pion],[1,3,pion],[3,3,pion]],R),writeln(R).


%mouvement pour un pion
moveHautGauche([X,_,_],_,_,_,[],deplacement):- X2 is X-1,X2<0,!.
moveHautGauche([_,Y,_],_,_,_,[],deplacement):- Y2 is Y-1,Y2<0,!.
moveHautGauche([X,Y,pion],Player,Blancs,Noirs,R,mange):- mangeHautGauche([X,Y,pion],Player,Blancs,Noirs,R), R\==[],!.
moveHautGauche([X,Y,pion],blanc,Blancs,Noirs,[[Blancs3, Noirs, [[X2,Y2]]]],deplacement):-  X2 is X-1,Y2 is Y-1,not(member([X2,Y2,_],Blancs)), not(member([X2,Y2,_],Noirs)),!, delete(Blancs, [X,Y,pion], Blancs2), append(Blancs2, [[X2,Y2,pion]], Blancs3).
moveHautGauche(_,_,_,_,[],deplacement).

moveHautDroite([X,_,_],_,_,_,[],deplacement):- X2 is X+1,X2>9,!.
moveHautDroite([_,Y,_],_,_,_,[],deplacement):- Y2 is Y-1,Y2<0,!.
moveHautDroite([X,Y,pion],Player,Blancs,Noirs,R,mange):- mangeHautDroite([X,Y,pion],Player,Blancs,Noirs,R), R\==[],!.
moveHautDroite([X,Y,pion],blanc,Blancs,Noirs,[[Blancs3, Noirs, [[X2,Y2]]]],deplacement):- X2 is X+1,Y2 is Y-1,not(member([X2,Y2,_],Blancs)), not(member([X2,Y2,_],Noirs)),!, delete(Blancs, [X,Y,pion], Blancs2), append(Blancs2, [[X2,Y2,pion]], Blancs3).
moveHautDroite(_,_,_,_,[],deplacement).

moveBasGauche([X,_,_],_,_,_,[],deplacement):- X2 is X-1,X2<0,!.
moveBasGauche([_,Y,_],_,_,_,[],deplacement):- Y2 is Y+1,Y2>9,!.
moveBasGauche([X,Y,pion],Player,Blancs,Noirs,R,mange):- mangeBasGauche([X,Y,pion],Player,Blancs,Noirs,R), R\==[],!.
moveBasGauche([X,Y,pion],noir,Blancs,Noirs,[[Blancs, Noirs3, [[X2,Y2]]]],deplacement):-  X2 is X-1,Y2 is Y+1,not(member([X2,Y2,_],Blancs)), not(member([X2,Y2,_],Noirs)),!, delete(Noirs, [X,Y,pion], Noirs2), append(Noirs2, [[X2,Y2,pion]], Noirs3).
moveBasGauche(_,_,_,_,[],deplacement).

moveBasDroite([X,_,_],_,_,_,[],deplacement):- X2 is X+1,X2>9,!.
moveBasDroite([_,Y,_],_,_,_,[],deplacement):- Y2 is Y+1,Y2>9,!.
moveBasDroite([X,Y,pion],Player,Blancs,Noirs,R,mange):- mangeBasDroite([X,Y,pion],Player,Blancs,Noirs,R), R\==[],!.
moveBasDroite([X,Y,pion],noir,Blancs,Noirs,[[Blancs, Noirs3, [[X2,Y2]]]],deplacement):-  X2 is X+1,Y2 is Y+1,not(member([X2,Y2,_],Blancs)), not(member([X2,Y2,_],Noirs)),!, delete(Noirs, [X,Y,pion], Noirs2), append(Noirs2, [[X2,Y2,pion]], Noirs3).
moveBasDroite(_,_,_,_,[],deplacement).
%test
% moveBasDroite([4,2,pion],blanc,[[4,2,pion]],[[5,1,pion],[3,1,pion],[5,3,pion],[3,3,pion]],R,T),writeln(R).
% moveBasDroite([4,2,pion],blanc,[[4,2,pion]],[[1,1,pion]],R,T),writeln(R).
% moveBasDroite([0,7,pion],blanc,[[0,7,pion]],[[1,1,pion]],R,T),writeln(R).
% movePossible([0,7,pion],blanc,[[0,7,pion]],[[1,1,pion]],R,T),writeln(R).
% movePossiblePlayer(blanc,[[0,7,pion]],[[1,1,pion]],[[0,7,pion]],S,Type),writeln(S).


%moveHautGauche
% moveHautGauche([4,4,pion],blanc,[[4,4,pion]],[[1,1,pion],[2,2,pion]],R,T).
% moveHautGauche([4,4,pion],blanc,[[4,4,pion]],[[1,1,pion],[3,3,pion]],R,T).
% moveHautGauche([4,2,pion],blanc,[[4,2,pion]],[[1,1,pion],[3,1,pion],[5,1,pion]],S,T),writeln(S).
%

%mouvement pour une dame (a faire)
% moveHautGauche([X,Y,dame],blanc,Blancs,Noirs,[[X2,Y2,dame]|Blancs]):-X2
% is X-1, Y2 is Y-1,
% X2>=0,Y2>=0,not(member([X2,Y2,_],Blancs)),!,not(member([X2,Y2,_],Noirs)).

verifieSolution(R1,R2,deplacement,deplacement,S,deplacement):-!,append(R1,R2,S).
verifieSolution(R1,_,mange,deplacement,R1,mange):-!.
verifieSolution(_,R2,deplacement,mange,R2,mange):-!.
verifieSolution(R1,R2,mange,mange,S,mange):-maxSolution(R1,R2,[],[],S).

verifieSolutionJoueur(R1,R2,deplacement,deplacement,S,deplacement):-!,append(R1,R2,S).
verifieSolutionJoueur(R1,_,mange,deplacement,R1,mange):-!.
verifieSolutionJoueur(_,R2,deplacement,mange,R2,mange):-!.
verifieSolutionJoueur(R1,R2,mange,mange,S,mange):- maxSolutionJoueur(R1,R2,S).
% tests
% verifieSolutionJoueur([[[3,1,pion],[[[],[[5,3,pion],[1,1,pion],[5,1,pion]],[[5,3]]]]]],[[[5,1,pion],[[[],[[3,3,pion],[1,1,pion],[3,1,pion]],[[3,3]]]]]],mange,mange,S,T),writeln(S).


% S est du type [[Blancs,Noirs,[[posX,posY],...]],...]
movePossible(E,Camp,Blancs,Noirs,S,Stype):-moveHautGauche(E,Camp,Blancs,Noirs,R1,Type1),moveHautDroite(E,Camp,Blancs,Noirs,R2,Type2),moveBasGauche(E,Camp,Blancs,Noirs,R3,Type3),moveBasDroite(E,Camp,Blancs,Noirs,R4,Type4),verifieSolution(R1,R2,Type1,Type2,S1,Stype1),verifieSolution(R3,S1,Type3,Stype1,S2,Stype2),verifieSolution(R4,S2,Type4,Stype2,S,Stype).
%tests
% movePossible([4,2,pion],blanc,[[4,2,pion]],[[5,1,pion],[3,1,pion],[1,1,pion],[1,3,pion],[3,3,pion]],R,S),writeln(R).
% movePossible([4,2,pion],blanc,[[4,2,pion]],[[5,1,pion],[3,1,pion],[5,3,pion],[3,3,pion]],R,S),writeln(R).
% movePossible([4,2,pion],blanc,[[4,2,pion]],[[1,1,pion]],R,S),writeln(R).
% movePossible([4,2,pion],blanc,[[4,2,pion]],[[1,1,pion],[3,1,pion],[5,1,pion]],S,Type),writeln(S).
% movePossible([3,1,pion],noir,[[4,2,pion]],[[1,1,pion],[3,1,pion],[5,1,pion]],S,Type),writeln(S).
% movePossible([3,1,pion],noir,[[4,2,pion]],[[1,1,pion],[3,1,pion],[5,1,pion]],S,Type),writeln(S).

%methode naive on prend la premiere solution trouvee
choixMove(X,Liste,Blancs,Noirs,ListeMouvement,E):- N is random(X), nth0(N,Liste,[E,L]),length(L,Y), N2 is random(Y),nth0(N2,L,[Blancs,Noirs,ListeMouvement]).

ajoutSiNonVide([],_,[]).
ajoutSiNonVide(L,E,[[E,L]]).

%On regarde tous les mouvements possibles pour un joueur
% S est du du type [[[X,Y,pion],[[Blancs,Noirs,ListeMouvement],...]],...]
movePossiblePlayer(Camp, Blancs, Noirs, [Tete|Pions], S,Type) :- movePossible(Tete, Camp, Blancs, Noirs, T,Type2), movePossiblePlayer(Camp, Blancs, Noirs, Pions, R,Type3),ajoutSiNonVide(T,Tete,Solution), verifieSolutionJoueur(Solution,R,Type2,Type3,S,Type),!.
movePossiblePlayer(_, _, _, [],[], deplacement).
% tests
% movePossiblePlayer(blanc,[[4,2,pion]],[[1,1,pion]],[[4,2,pion]],S,Type),writeln(S).
% movePossiblePlayer(noir,[[4,2,pion]],[[1,1,pion],[3,1,pion],[5,1,pion]],[[1,1,pion],[3,1,pion],[5,1,pion]],S,Type),writeln(S).
% movePossiblePlayer(blanc,[[4,2,pion]],[[1,1,pion],[3,1,pion],[5,1,pion]],[[4,2,pion]],S,Type),writeln(S).

ia(blanc,Blancs,Noirs,Blancs2,Noirs2,ListeMouvement,E):-movePossiblePlayer(blanc, Blancs, Noirs, Blancs, Liste,_),length(Liste,X),X>0,!,choixMove(X,Liste,Blancs2,Noirs2,ListeMouvement,E).
ia(noir,Blancs,Noirs,Blancs2,Noirs2,ListeMouvement,E):-movePossiblePlayer(noir, Blancs, Noirs, Noirs, Liste,_),length(Liste,X),X>0,choixMove(X,Liste,Blancs2,Noirs2,ListeMouvement,E).
% ia(blanc,[[4,2,pion]],[[5,1,pion],[3,1,pion],[1,1,pion],[1,3,pion],[3,3,pion]],Blancs,Noirs,L,E).


%Changer un pion en dame

changePionDame(blanc, Blancs, Noirs, ListeMouvement, [_,_,pion], Blancs2, Noirs) :- last(ListeMouvement, [X,0]), delete(Blancs, [X,0,_], L), append(L, [[X,0,dame]], Blancs2),!.
changePionDame(noir, Blancs, Noirs, ListeMouvement, [_,_,pion], Blancs, Noirs2) :- last(ListeMouvement, [X,9]), delete(Noirs, [X,9,_], L), append(L, [[X,9,dame]], Noirs2),!.
changePionDame(_, Blancs, Noirs, _, _, Blancs, Noirs).

applyMoves(Blancs, Noirs) :- retractall(blancs(_)), retractall(noirs(_)),assert(blancs(Blancs)), assert(noirs(Noirs)).
%

%lancement du jeu
% play(blanc,[[1,1,pion],[2,2,pion]],[[5,5,pion],[8,6,pion]],B,N,L,Pion).
play(Player,Blancs,Noirs,Blancs3,Noirs3,ListeMouvement,Pion,Etat):- ia(Player,Blancs,Noirs,Blancs2,Noirs2,ListeMouvement, Pion),
		changePionDame(Player, Blancs2, Noirs2, ListeMouvement, Pion, Blancs3, Noirs3),
		gameover(Blancs,Noirs,Blancs3,Noirs3,Etat).

% play(noir, [[1,6,pion],[1,4,pion],[4,5,pion],[7,6,pion],[9,6,pion],[0,7,pion],[2,7,pion],[4,7,pion],[6,7,pion],[8,7,pion],[1,8,pion],[3,8,pion],[5,8,pion],[7,8,pion],[9,8,pion],[0,9,pion],[2,9,pion],[4,9,pion],[6,9,pion],[8,9,pion]],[[1,0,pion],[3,0,pion],[5,0,pion],[7,0,pion],[9,0,pion],[0,1,pion],[2,1,pion],[4,1,pion],[6,1,pion],[8,1,pion],[1,2,pion],[3,2,pion],[5,2,pion],[7,2,pion],[9,2,pion],[0,3,pion],[2,3,pion],[5,4,pion],[6,3,pion],[7,4,pion]],B,N,L,Pion,Etat).

% play(Player):-  write('New turn for: '), ((Player==blanc, writeln('Blancs'));(Player==noir, writeln('Noirs'))),
% noirs(Noirs),
% blancs(Blancs),

% display1(0,10,_),
% writeln(" "),

% ia(Player,Blancs,Noirs,Blancs2,Noirs2,ListeMouvement, E),

% changePionDame(Player, Blancs2, Noirs2, ListeMouvement, E, Blancs3, Noirs3),

% not(((gameover(blanc), !, write('Game is Over. Winner: '), writeln('Blancs'));
		% (gameover(noir), !, write('Game is Over. Winner: '), writeln('Noirs'));
% (gameover('Draw', Blancs3, Noirs3), !, writeln('Game is Over. Draw')))),

% applyMoves(Blancs3, Noirs3),
% changePlayer(Player,NextPlayer), % Change the player before next turn
% sleep(1),
% play(NextPlayer). % next turn!

% ATTENTION : play est commenté pour pouvoir utiliser l'IHM
init :-retractall(blancs(_)), retractall(noirs(_)), retractall(cptDraw(_)), creerListe(noir,L1),creerListe(blanc,L2),assert(noirs(L1)),assert(blancs(L2)), assert(cptDraw(0)).%, play(blanc).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%																		%
%					 Code Serveur du Jeu de dame						%
%																		%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Chargement des modules pour le serveur et la gestion du JSON
:- use_module(library(http/thread_httpd)).
:- use_module(library(http/http_dispatch)).
:- use_module(library(http/html_write)).
:- use_module(library(http/json)).
:- use_module(library(http/json_convert)).
:- use_module(library(http/http_json)).

% Surcharge des urls avec les méthodes appellées pour chacune
:- http_handler(root(init), init_server, []).
:- http_handler(root(play), play_server, []).
:- http_handler(root(moves_allowed), moves_allowed_server, []).

% Creation des objets JSON utilisé dans l'application
:- json_object pion(x:integer, y:integer) + [type=pion].
:- json_object joueur(j:integer).
:- json_object dame(x:integer, y:integer) + [type=dame].
:- json_object position(x:integer, y:integer).
:- json_object blancs(blancs:list).
:- json_object noirs(noirs:list).
:- json_object positions(positions:list).
:- json_object game(joueur: integer, blancs:list, noirs:list).
:- json_object turn(etat:integer, joueur: integer, blancs:list, noirs:list, pion:compound, mouvements:list).
:- json_object posibilite(blancs:list, noirs:list, mouvements:list).
:- json_object move(pion: compound, posibilite:list).
:- json_object movesAllowed(move:list).

% Predicat qui lance le server
server(Port) :-	http_server(http_dispatch, [port(Port)]).

% Prédicat init qui est appellé quand on appelle l'url /init
% Le prédicat lance le jeu en appelant la méthode init du jeu
% Le prédicat renvoie la liste des pions blancs et noirs et le joueur qui doit jouer en format JSON
init_server(_Request) :- 	init,
							noirs(ListeNoirs),
							blancs(ListeBlancs),
							build_reply_init(ListeBlancs,ListeNoirs, 0, JSON),
							reply_json(JSON).

% Prédicat play_server qui est appellé quand on appelle l'url /play
% Le prédicat reconstruit la liste des blancs et des noirs
% Le prédicat appelle le predicat ia qui va jouer un cout
% Le prédicat renvoie la liste des pions blancs et noirs et le joueur qui doit jouer en format JSON
play_server(Request) :- http_read_json(Request, JsonIn,[json_object(term)]),
						format(user_output,"JsonIn is: ~p~n",[JsonIn]),
 						json_to_prolog(JsonIn, Data), game_get_data_informations(Data, J, Blancs, Noirs),
 						format(user_output,"Data is: ~p~n",[Data]),
 						format(user_output,"J is: ~p~n",[J]),
 						format(user_output,"Blancs is: ~p~n",[Blancs]),
 						format(user_output,"Noirs is: ~p~n",[Noirs]),
 						play(J,Blancs,Noirs,Blancs2,Noirs2,ListeMouvement,Pion,Etat),
 						format(user_output,"Blancs2 is: ~p~n",[Blancs2]),
 						format(user_output,"Noirs2 is: ~p~n",[Noirs2]),
 						format(user_output,"ListeMouvement is: ~p~n",[ListeMouvement]),
 						format(user_output,"Pion is: ~p~n",[Pion]),
 						format(user_output,"Etat is: ~p~n",[Etat]),
						build_reply_play(Blancs2,Noirs2,J,Pion,ListeMouvement,Etat,JSON),
						format(user_output,"JSON is: ~p~n",[JSON]),
				   		reply_json(JSON).


moves_allowed_server(Request) :- 	http_read_json(Request, JsonIn,[json_object(term)]),
									format(user_output,"JsonIn is: ~p~n",[JsonIn]),
 									json_to_prolog(JsonIn, Data), game_get_data_informations(Data, J, Blancs, Noirs),
 									format(user_output,"Data is: ~p~n",[Data]),
			 						format(user_output,"J is: ~p~n",[J]),
			 						format(user_output,"Blancs is: ~p~n",[Blancs]),
			 						format(user_output,"Noirs is: ~p~n",[Noirs]),
 									call_movePossiblePlayer(J,Blancs, Noirs,S),
 									format(user_output,"S is: ~p~n",[S]),
 									build_reply_moves_allowed(S,Json),
									format(user_output,"json is: ~p~n",[JSON]),
							   		reply_json(Json).


call_movePossiblePlayer(blanc, Blancs, Noirs, S) :- movePossiblePlayer(blanc, Blancs, Noirs, Blancs, S,_).
call_movePossiblePlayer(noir, Blancs, Noirs, S) :- movePossiblePlayer(noir, Blancs, Noirs, Noirs, S,_).

% Predicat qui permet de construire le JSON relatif à la reponse d'init
%	ListeBlancs = Liste contenant des pions et des dames
%	ListeNoirs = Liste contenant des pions et des dames
build_reply_init(ListeBlancs,ListeNoirs,Joueur,JSON) :- 	convert_list_pion_to_json_object(ListeBlancs, LB), 
															convert_list_pion_to_json_object(ListeNoirs, LN),
															J = game(Joueur, LB, LN), 
															prolog_to_json(J,JSON).


% Predicat qui permet de construire le JSON relatif à la reponse de play
%	ListeBlancs = Liste contenant des pions et des dames
%	ListeNoirs = Liste contenant des pions et des dames
%	Joueur = Joueur qui joue le tour (blanc/noir)
%	Pion = Le pion qui a bougé pour ce tour
%	ListeMouvement = Liste des mouvements du pion
%	Etat = Etat de la partie (blanc gagne, noir gagne, égalité, se poursuit)
build_reply_play(ListeBlancs,ListeNoirs,Joueur,Pion,ListeMouvement,Etat,JSON) :- convert_list_pion_to_json_object(ListeBlancs, LB), 
																			convert_list_pion_to_json_object(ListeNoirs, LN),
																			convert_list_position_to_json_object(ListeMouvement, LM),
																			convert_pion_to_json_object_pion(Pion, P1),
																			prolog_to_json(P1,P),
																			changePlayer(Joueur,JPredicat),
																			build_joueur_predicat_int(JPredicat,JInt),
																			build_etat_predicat_int(Etat,EInt),
																			JSONProlog = turn(EInt,JInt, LB, LN, P, LM), 
																			prolog_to_json(JSONProlog,JSON).

% Predicat qui permet de construire le JSON relatif à la reponse de moves_allowed
%	L = Liste de mouvements possible de la forme [[[X,Y,pion],[[Blancs,Noirs,ListeMouvement],...]],...]
build_reply_moves_allowed(L,JSON) :- 	build_liste_move_json(L,Moves),
										JSONProlog = movesAllowed(Moves), 
										prolog_to_json(JSONProlog,JSON).

%[[X,Y,pion],[[Blancs,Noirs,ListeMouvement],...]]
build_liste_move_json([],[]).
build_liste_move_json([H|T],Moves) :- build_liste_move_json(T,M), build_possibilites_for_pion_json(H,Move), append([Move],M,Moves).


%[X,Y,pion],[[[Blancs,Noirs,ListeMouvement],...]]
build_possibilites_for_pion_json([H|T],M) :- convert_pion_to_json_object_pion(H,P), prolog_to_json(P,Pion), build_move_possibilites_json(T,Possibilities), Move = move(Pion,Possibilities), prolog_to_json(Move, M).

%[[Blancs,Noirs,ListeMouvement],...]
build_move_possibilites_json([],[]).
build_move_possibilites_json([H|T],Poss) :- build_move_possibilites_json(T,Poss2), decompose(H,HD),build_move_possibilite_json(HD,P), append([P],Poss2,Poss).

decompose([H|_],H).
%[Blancs,Noirs,ListeMouvement]
build_move_possibilite_json([H|T],P):- convert_list_pion_to_json_object(H,B), build_move_possibilites_json_Noir_Mouvements(T,N,M), Poss = posibilite(B,N,M), prolog_to_json(Poss,P).
build_move_possibilites_json_Noir_Mouvements([H|T],N,M) :- build_move_possibilites_json_Mouvements(T,M), convert_list_pion_to_json_object(H,N).
build_move_possibilites_json_Mouvements([H|_],M) :- convert_list_position_to_json_object(H,M).

%[Blancs,Noirs,ListeMouvement]
%[Blancs,Noirs,ListeMouvement]
% Prédicat game_get_data_informations qui permet de recuperer la Liste Blancs, la Liste Noirs et le joueur dans un objet JSON de type game
game_get_data_informations(Data, J, Blancs, Noirs):- 	game_get_joueur(Data, J), 
														game_get_blancs(Data, LB), 
														convert_list_pion_json_to_object(LB,Blancs),
														game_get_noirs(Data, LN),
														convert_list_pion_json_to_object(LN,Noirs).

% Prédicat game_get_data_informations qui permet de recuperer le numero du joueur dans un objet JSON de type game
game_get_joueur(game(0,_,_),blanc). 
game_get_joueur(game(1,_,_),noir). 

% Prédicat game_get_data_informations qui permet de recuperer la Liste Blancs dans un objet JSON de type game
game_get_blancs(game(_,Y,_),Y). 

% Prédicat game_get_data_informations qui permet de recuperer la Liste Noirs dans un objet JSON de type game
game_get_noirs(game(_,_,Z),Z).

% Predicat qui permet de construire le int d'un joueur avec le predicat
build_joueur_predicat_int(blanc,0).
build_joueur_predicat_int(noir,1).

% Predicat qui permet de construire le int d'un etat avec le predicat
build_etat_predicat_int(blanc,0).
build_etat_predicat_int(noir,1).
build_etat_predicat_int(egalite,2).
build_etat_predicat_int(continuer,3).

% Predicat qui permet de construire le predicat d'un joueur avec le int
build_joueur_int_predicat(0,blanc).
build_joueur_int_predicat(1,noir).

% Predicat qui permet de construire le JSON d'une liste de pion
convert_list_pion_to_json_object([], []).
convert_list_pion_to_json_object([H|T],O2) :- convert_list_pion_to_json_object(T, O), convert_pion_to_json_object_pion(H,X), append([X],O,O2), !.

% Predicat qui permet de construire le prolog d'une liste de pion JSON
convert_list_pion_json_to_object([], []).
convert_list_pion_json_to_object([H|T],O2) :- convert_list_pion_json_to_object(T, O), convert_pion_json_to_object_pion(H,X), append([X],O,O2), !.

% Predicat qui permet de construire le JSON d'une liste de position
convert_list_position_to_json_object([], []).
convert_list_position_to_json_object([H|T],O2) :- convert_list_position_to_json_object(T, O), convert_position_to_json_object_position(H,X), append([X],O,O2), !.

% Méthode de conversion d'un pion ou d'une dame ([1, 2, pion] ou [1, 2, dame]) en Objet JSON Prolog (pion(1,2) ou dame(1,2))
convert_pion_to_json_object_pion(L,O) :- 	convert_pion_to_json_object_pion_X(L,X), convert_pion_to_json_object_pion_Y(L,X,Y), convert_pion_to_json_object_pion_Name(L,X,Y,O), !.
convert_pion_to_json_object_pion_X([H|_],X) :- X = H.
convert_pion_to_json_object_pion_Y([],_,_).
convert_pion_to_json_object_pion_Y([X|T],X,Y) :- convert_pion_to_json_object_pion_Y(T,X,Y).
convert_pion_to_json_object_pion_Y([H|_],_,Y) :- Y = H.
convert_pion_to_json_object_pion_Name([],_).
convert_pion_to_json_object_pion_Name([pion|_], X, Y, O) :- O = pion(X,Y).
convert_pion_to_json_object_pion_Name([dame|_], X, Y, O) :- O = dame(X,Y).
convert_pion_to_json_object_pion_Name([_|T], X, Y, O) :- convert_pion_to_json_object_pion_Name(T,X,Y,O).

% Méthode de conversion d'un pion ou d'une dame JSON (pion(1,2) ou dame(1,2)) en Objet Prolog ([1, 2, pion] ou [1, 2, dame])
convert_pion_json_to_object_pion(L,O4) :- 	convert_pion_json_to_object_pion_X(L,X), convert_pion_json_to_object_pion_Y(L,Y), convert_pion_json_to_object_pion_Name(L,N),append([N],[],O2),append([Y],O2,O3), append([X],O3,O4), !.
convert_pion_json_to_object_pion_X(pion(X,_),X).
convert_pion_json_to_object_pion_X(dame(X,_),X).
convert_pion_json_to_object_pion_Y(pion(_,Y),Y).
convert_pion_json_to_object_pion_Y(dame(_,Y),Y).
convert_pion_json_to_object_pion_Name(pion(_,_),pion).
convert_pion_json_to_object_pion_Name(dame(_,_),dame).

% Méthode de conversion d'une position ([1, 2]) en Objet JSON Prolog (position(1,2))
convert_position_to_json_object_position(L,O) :- convert_position_to_json_object_position_X(L,X), convert_position_to_json_object_position_Y(L,X,Y), O = position(X,Y), !.
convert_position_to_json_object_position_X([H|_],X) :- X = H.
convert_position_to_json_object_position_Y([],_,_).
convert_position_to_json_object_position_Y([X|T],X,Y) :- convert_position_to_json_object_position_Y(T,X,Y).
convert_position_to_json_object_position_Y([H|_],_,Y) :- Y = H.