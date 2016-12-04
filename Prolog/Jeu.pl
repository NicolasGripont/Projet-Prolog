:- dynamic blancs/1.
:- dynamic noirs/1.
:- dynamic cptDraw/1.


%%%% Test is the game is finished %%%
gameover(_,_,Blancs,Noirs,Winner) :- winner(Winner, Blancs, Noirs), !.  % There exists a winning configuration: We cut!
gameover(Blancs,Noirs,Blancs2,Noirs2,egalite) :- length(Noirs, X), length(Noirs2, X), length(Blancs, Y), length(Blancs2, Y),
										cptDraw(CptDraw), NewCptDraw is CptDraw+1, retractall(cptDraw(_)), assert(cptDraw(NewCptDraw)),
										NewCptDraw == 25,!.
gameover(Blancs,Noirs,Blancs2,Noirs2,continuer) :- length(Noirs, X), length(Noirs2, X), length(Blancs, Y), length(Blancs2, Y),!.
gameover(_,_,_,_,continuer) :- retractall(cptDraw(_)), assert(cptDraw(0)).

% tests
% gameover([[5,4,pion],[8,7,pion],[4,3,pion],[7,4,pion],[5,8,pion],[2,2,pion]],[[1,1,pion]],[[5,4,pion],[8,7,pion],[4,3,pion],[7,4,pion],[5,8,pion],[0,0,dame]],[],Winner).


%%%% Test de victoire pour les joueurs.
winner(blanc, Blancs, Noirs) :- (length(Noirs, 0);(movePossiblePlayer(noir, Blancs, Noirs, Noirs, [],_))), !.
winner(noir, Blancs, Noirs) :- (length(Blancs, 0);(movePossiblePlayer(blanc, Blancs, Noirs, Blancs, [],_))), !.

%%%% Predicate to get the next player
changePlayer(blanc,noir).
changePlayer(noir,blanc).

%%%% Predicate to get the camp of the current player
returnPionsCamp(blanc, Blancs, _, Blancs).
returnPionsCamp(noir, _, Noirs, Noirs).

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
		not(member([X1,Y1,T],Noirs)),not(member([X1,Y1,_],Manges)),cherchePionHautGauche(X1,Y1,Camp,Blancs,Noirs,[X2,Y2,T],Manges).

cherchePionHautDroite(X,Y,blanc,Blancs,Noirs,[X1,Y1,T],Manges):-X1 is X+1, Y1 is Y-1, X1=<9, Y1>=0, not(member([X1,Y1,_],Blancs)),
		not(member([X1,Y1,_],Manges)),member([X1,Y1,T],Noirs),!.
cherchePionHautDroite(X,Y,noir,Blancs,Noirs,[X1,Y1,T],Manges):-X1 is X+1, Y1 is Y-1, X1=<9, Y1>=0, not(member([X1,Y1,_],Noirs)),
		not(member([X1,Y1,_],Manges)),member([X1,Y1,T],Blancs),!.
cherchePionHautDroite(X,Y,Camp,Blancs,Noirs,[X2,Y2,T],Manges):-X1 is X+1, Y1 is Y-1, X1=<9, Y1>=0, not(member([X1,Y1,_],Blancs)),
		not(member([X1,Y1,T],Noirs)),not(member([X1,Y1,_],Manges)),cherchePionHautDroite(X1,Y1,Camp,Blancs,Noirs,[X2,Y2,T],Manges).

cherchePionBasGauche(X,Y,blanc,Blancs,Noirs,[X1,Y1,T],Manges):-X1 is X-1, Y1 is Y+1, X1>=0, Y1=<9, not(member([X1,Y1,_],Blancs)),
		not(member([X1,Y1,_],Manges)),member([X1,Y1,T],Noirs),!.
cherchePionBasGauche(X,Y,noir,Blancs,Noirs,[X1,Y1,T],Manges):-X1 is X-1, Y1 is Y+1, X1>=0, Y1=<9, not(member([X1,Y1,_],Noirs)),
		not(member([X1,Y1,_],Manges)),member([X1,Y1,T],Blancs),!.
cherchePionBasGauche(X,Y,Camp,Blancs,Noirs,[X2,Y2,T],Manges):-X1 is X-1, Y1 is Y+1, X1>=0, Y1=<9, not(member([X1,Y1,_],Blancs)),
		not(member([X1,Y1,T],Noirs)),not(member([X1,Y1,_],Manges)),cherchePionBasGauche(X1,Y1,Camp,Blancs,Noirs,[X2,Y2,T],Manges).

cherchePionBasDroite(X,Y,blanc,Blancs,Noirs,[X1,Y1,T],Manges):-X1 is X+1, Y1 is Y+1, X1=<9, Y1=<9, not(member([X1,Y1,_],Blancs)),
		not(member([X1,Y1,_],Manges)),member([X1,Y1,T],Noirs),!.
cherchePionBasDroite(X,Y,noir,Blancs,Noirs,[X1,Y1,T],Manges):-X1 is X+1, Y1 is Y+1, X1=<9, Y1=<9, not(member([X1,Y1,_],Noirs)),
		not(member([X1,Y1,_],Manges)),member([X1,Y1,T],Blancs),!.
cherchePionBasDroite(X,Y,Camp,Blancs,Noirs,[X2,Y2,T],Manges):-X1 is X+1, Y1 is Y+1, X1=<9, Y1=<9, not(member([X1,Y1,_],Blancs)),
		not(member([X1,Y1,T],Noirs)),not(member([X1,Y1,_],Manges)),cherchePionBasDroite(X1,Y1,Camp,Blancs,Noirs,[X2,Y2,T],Manges).
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

chercheCaseVideHautDroite(X,Y,Blancs,Noirs, Manges, [[X1,Y1]|L]) :-
    X1 is X+1, Y1 is Y-1, X1=<9, Y1>=0,
    not(member([X1,Y1,_],Blancs)),
    not(member([X1,Y1,_],Noirs)),
    not(member([X1,Y1],Manges)),
    chercheCaseVideHautDroite(X1,Y1,Blancs,Noirs,Manges,L),!.
chercheCaseVideHautDroite(_,_,_,_,_,[]).

chercheCaseVideBasGauche(X,Y,Blancs,Noirs, Manges, [[X1,Y1]|L]) :-
    X1 is X-1, Y1 is Y+1, X1>=0, Y1=<9,
    not(member([X1,Y1,_],Blancs)),
    not(member([X1,Y1,_],Noirs)),
    not(member([X1,Y1],Manges)),
    chercheCaseVideBasGauche(X1,Y1,Blancs,Noirs,Manges,L),!.
chercheCaseVideBasGauche(_,_,_,_,_,[]).

chercheCaseVideBasDroite(X,Y,Blancs,Noirs, Manges, [[X1,Y1]|L]) :-
    X1 is X+1, Y1 is Y+1, X1=<9, Y1=<9,
    not(member([X1,Y1,_],Blancs)),
    not(member([X1,Y1,_],Noirs)),
    not(member([X1,Y1],Manges)),
    chercheCaseVideBasDroite(X1,Y1,Blancs,Noirs,Manges,L),!.
chercheCaseVideBasDroite(_,_,_,_,_,[]).

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

%pion et premier appel dame
mangeBasGauche([X,Y,pion],blanc,Blancs,Noirs,Retour):- X2 is X-1, Y2 is Y+1,X3 is X-2,Y3 is Y+2,X3 >= 0 ,Y3 =< 9,member([X2,Y2,_],Noirs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Noirs,[X2,Y2,_],Noirs2),delete(Blancs,[X,Y,pion],Blancs2),mangeTouteDirection([X3,Y3,pion],blanc,[[X3,Y3,pion]|Blancs2],Noirs2,R),creerMouvement([[X3,Y3,pion]|Blancs2],Noirs2,R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeBasGauche([X,Y,pion],noir,Blancs,Noirs,Retour):- X2 is X-1, Y2 is Y+1,X3 is X-2,Y3 is Y+2,X3 >= 0 ,Y3 =< 9,member([X2,Y2,_],Blancs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Blancs,[X2,Y2,_],Blancs2),delete(Noirs,[X,Y,pion],Noirs2),mangeTouteDirection([X3,Y3,pion],noir,Blancs2,[[X3,Y3,pion]|Noirs2],R),creerMouvement(Blancs2,[[X3,Y3,pion]|Noirs2],R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeBasGauche([X,Y,dame],Camp,Blancs,Noirs,Retour):-mangeBasGauche([X,Y,dame],Camp,Blancs,Noirs,[],Retour),!.
mangeBasGauche(_,_,_,_,[]).

mangeBasDroite([X,Y,pion],blanc,Blancs,Noirs,Retour):- X2 is X+1, Y2 is Y+1,X3 is X+2,Y3 is Y+2,X3 =< 9 ,Y3 =< 9,member([X2,Y2,_],Noirs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Noirs,[X2,Y2,_],Noirs2),delete(Blancs,[X,Y,pion],Blancs2),mangeTouteDirection([X3,Y3,pion],blanc,[[X3,Y3,pion]|Blancs2],Noirs2,R),creerMouvement([[X3,Y3,pion]|Blancs2],Noirs2,R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeBasDroite([X,Y,pion],noir,Blancs,Noirs,Retour):- X2 is X+1, Y2 is Y+1,X3 is X+2,Y3 is Y+2,X3 =< 9 ,Y3 =< 9,member([X2,Y2,_],Blancs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Blancs,[X2,Y2,_],Blancs2),delete(Noirs,[X,Y,pion],Noirs2),mangeTouteDirection([X3,Y3,pion],noir,Blancs2,[[X3,Y3,pion]|Noirs2],R),creerMouvement(Blancs2,[[X3,Y3,pion]|Noirs2],R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeBasDroite([X,Y,dame],Camp,Blancs,Noirs,Retour):-mangeBasDroite([X,Y,dame],Camp,Blancs,Noirs,[],Retour),!.
mangeBasDroite(_,_,_,_,[]).

mangeHautDroite([X,Y,pion],blanc,Blancs,Noirs,Retour):- X2 is X+1, Y2 is Y-1,X3 is X+2,Y3 is Y-2,X3 =< 9 ,Y3 >= 0,member([X2,Y2,_],Noirs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Noirs,[X2,Y2,_],Noirs2),delete(Blancs,[X,Y,pion],Blancs2),mangeTouteDirection([X3,Y3,pion],blanc,[[X3,Y3,pion]|Blancs2],Noirs2,R),creerMouvement([[X3,Y3,pion]|Blancs2],Noirs2,R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeHautDroite([X,Y,pion],noir,Blancs,Noirs,Retour):- X2 is X+1, Y2 is Y-1,X3 is X+2,Y3 is Y-2,X3 =< 9 ,Y3 >= 0,member([X2,Y2,_],Blancs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Blancs,[X2,Y2,_],Blancs2),delete(Noirs,[X,Y,pion],Noirs2),mangeTouteDirection([X3,Y3,pion],noir,Blancs2,[[X3,Y3,pion]|Noirs2],R),creerMouvement(Blancs2,[[X3,Y3,pion]|Noirs2],R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeHautDroite([X,Y,dame],Camp,Blancs,Noirs,Retour):-mangeHautDroite([X,Y,dame],Camp,Blancs,Noirs,[],Retour),!.
mangeHautDroite(_,_,_,_,[]).

mangeHautGauche([X,Y,pion],blanc,Blancs,Noirs,Retour):- X2 is X-1, Y2 is Y-1,X3 is X-2,Y3 is Y-2,X3 >= 0,Y3 >= 0,member([X2,Y2,_],Noirs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Noirs,[X2,Y2,_],Noirs2),delete(Blancs,[X,Y,pion],Blancs2),mangeTouteDirection([X3,Y3,pion],blanc,[[X3,Y3,pion]|Blancs2],Noirs2,R),creerMouvement([[X3,Y3,pion]|Blancs2],Noirs2,R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeHautGauche([X,Y,pion],noir,Blancs,Noirs,Retour):- X2 is X-1, Y2 is Y-1,X3 is X-2,Y3 is Y-2,X3 >= 0,Y3 >= 0,member([X2,Y2,_],Blancs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Blancs,[X2,Y2,_],Blancs2),delete(Noirs,[X,Y,pion],Noirs2),mangeTouteDirection([X3,Y3,pion],noir,Blancs2,[[X3,Y3,pion]|Noirs2],R),creerMouvement(Blancs2,[[X3,Y3,pion]|Noirs2],R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeHautGauche([X,Y,dame],Camp,Blancs,Noirs,Retour):-mangeHautGauche([X,Y,dame],Camp,Blancs,Noirs,[],Retour),!.
mangeHautGauche(_,_,_,_,[]).

%test mangehautgauche
% mangeHautGauche([4,4,pion],blanc,[[4,4,pion]],[[1,1,pion],[3,3,pion]],R).
% mangeHautGauche([4,2,dame],blanc,[[4,2,dame]],[[3,1,pion],[1,1,pion],[3,3,pion]],R).


% cherche pour chaque case de la liste [[X,Y]|Suite] les solutions possibles pour manger
mangeSuite(blanc,Blancs,Noirs,[[X,Y]|Suite],Manges,Retour):-!,mangeSuite(blanc,Blancs,Noirs,Suite,Manges,Solution1),
		mangeTouteDirection([X,Y,dame],blanc,[[X,Y,dame]|Blancs],Noirs,Manges,R),
		creerMouvement([[X,Y,dame]|Blancs],Noirs,R,R2),
		ajoutMouvement([X,Y],R2,Sortie),
		maxSolution(Solution1,Sortie,Retour).
mangeSuite(noir,Blancs,Noirs,[[X,Y]|Suite],Manges,Retour):-!,mangeSuite(noir,Blancs,Noirs,Suite,Manges,Solution1),
		mangeTouteDirection([X,Y,dame],noir,Blancs,[[X,Y,dame]|Noirs],Manges,R),
		creerMouvement(Blancs,[[X,Y,dame]|Noirs],R,R2),
		ajoutMouvement([X,Y],R2,Sortie),
		maxSolution(Solution1,Sortie,Retour).
mangeSuite(_,_,_,[],_,[]).

%dame
mangeHautGauche([X,Y,dame],blanc,Blancs,Noirs,Manges,Retour):- cherchePionHautGauche(X,Y,blanc,Blancs,Noirs,[X2,Y2,T],Manges),
		chercheCaseVideHautGauche(X2,Y2,Blancs,Noirs, [], R),R \== [],!, delete(Noirs,[X2,Y2,T],Noirs2),
		delete(Blancs,[X,Y,dame],Blancs2),mangeSuite(blanc,Blancs2,Noirs2,R,[[X2,Y2,T]|Manges],Retour).
mangeHautGauche([X,Y,dame],noir,Blancs,Noirs,Manges,Retour):- cherchePionHautGauche(X,Y,noir,Blancs,Noirs,[X2,Y2,T],Manges),
		chercheCaseVideHautGauche(X2,Y2,Blancs,Noirs, [], R),R \== [],!, delete(Blancs,[X2,Y2,T],Blancs2),
		delete(Noirs,[X,Y,dame],Noirs2),mangeSuite(noir,Blancs2,Noirs2,R,[[X2,Y2,T]|Manges],Retour).
mangeHautGauche(_,_,_,_,_,[]).

mangeHautDroite([X,Y,dame],blanc,Blancs,Noirs,Manges,Retour):- cherchePionHautDroite(X,Y,blanc,Blancs,Noirs,[X2,Y2,T],Manges),
		chercheCaseVideHautDroite(X2,Y2,Blancs,Noirs, [], R),R \== [],!, delete(Noirs,[X2,Y2,T],Noirs2),
		delete(Blancs,[X,Y,dame],Blancs2),mangeSuite(blanc,Blancs2,Noirs2,R,[[X2,Y2,T]|Manges],Retour).
mangeHautDroite([X,Y,dame],noir,Blancs,Noirs,Manges,Retour):- cherchePionHautDroite(X,Y,noir,Blancs,Noirs,[X2,Y2,T],Manges),
		chercheCaseVideHautDroite(X2,Y2,Blancs,Noirs, [], R),R \== [],!, delete(Blancs,[X2,Y2,T],Blancs2),
		delete(Noirs,[X,Y,dame],Noirs2),mangeSuite(noir,Blancs2,Noirs2,R,[[X2,Y2,T]|Manges],Retour).
mangeHautDroite(_,_,_,_,_,[]).

mangeBasDroite([X,Y,dame],blanc,Blancs,Noirs,Manges,Retour):- cherchePionBasDroite(X,Y,blanc,Blancs,Noirs,[X2,Y2,T],Manges),
		chercheCaseVideBasDroite(X2,Y2,Blancs,Noirs, [], R),R \== [],!, delete(Noirs,[X2,Y2,T],Noirs2),
		delete(Blancs,[X,Y,dame],Blancs2),mangeSuite(blanc,Blancs2,Noirs2,R,[[X2,Y2,T]|Manges],Retour).
mangeBasDroite([X,Y,dame],noir,Blancs,Noirs,Manges,Retour):- cherchePionBasDroite(X,Y,noir,Blancs,Noirs,[X2,Y2,T],Manges),
		chercheCaseVideBasDroite(X2,Y2,Blancs,Noirs, [], R),R \== [],!, delete(Blancs,[X2,Y2,T],Blancs2),
		delete(Noirs,[X,Y,dame],Noirs2),mangeSuite(noir,Blancs2,Noirs2,R,[[X2,Y2,T]|Manges],Retour).
mangeBasDroite(_,_,_,_,_,[]).

mangeBasGauche([X,Y,dame],blanc,Blancs,Noirs,Manges,Retour):- cherchePionBasGauche(X,Y,blanc,Blancs,Noirs,[X2,Y2,T],Manges),
		chercheCaseVideBasGauche(X2,Y2,Blancs,Noirs, [], R),R \== [],!, delete(Noirs,[X2,Y2,T],Noirs2),
		delete(Blancs,[X,Y,dame],Blancs2),mangeSuite(blanc,Blancs2,Noirs2,R,[[X2,Y2,T]|Manges],Retour).
mangeBasGauche([X,Y,dame],noir,Blancs,Noirs,Manges,Retour):- cherchePionBasGauche(X,Y,noir,Blancs,Noirs,[X2,Y2,T],Manges),
		chercheCaseVideBasGauche(X2,Y2,Blancs,Noirs, [], R),R \== [],!, delete(Blancs,[X2,Y2,T],Blancs2),
		delete(Noirs,[X,Y,dame],Noirs2),mangeSuite(noir,Blancs2,Noirs2,R,[[X2,Y2,T]|Manges],Retour).
mangeBasGauche(_,_,_,_,_,[]).



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

%pour une dame
mangeTouteDirection([X,Y,dame],Couleur,Blancs,Noirs,Manges,R):-mangeHautGauche([X,Y,dame],Couleur,Blancs,Noirs,Manges,R1),
		mangeHautDroite([X,Y,dame],Couleur,Blancs,Noirs,Manges,R2),mangeBasGauche([X,Y,dame],Couleur,Blancs,Noirs,Manges,R3),
		mangeBasDroite([X,Y,dame],Couleur,Blancs,Noirs,Manges,R4),maxSolution(R1,R2,R3,R4,R).
%tests
% mangeTouteDirection([4,2,pion],blanc,[[4,2,pion]],[[5,1,pion],[3,1,pion],[1,1,pion],[1,3,pion],[3,3,pion]],R),writeln(R).
% mangeTouteDirection([4,2,dame],blanc,[[4,2,dame]],[[5,1,pion],[3,1,pion],[1,1,pion],[1,3,pion],[3,3,pion]],[],R),writeln(R).
% mangeHautGauche([4,2,dame],blanc,[[4,2,dame]],[[5,1,pion],[3,1,pion],[1,1,pion],[1,3,pion],[3,3,pion]],R),writeln(R).
% mangeHautDroite([4,2,dame],blanc,[[4,2,dame]],[[5,1,pion],[3,1,pion],[1,1,pion],[1,3,pion],[3,3,pion]],R),writeln(R).
% mangeBasGauche([4,2,dame],blanc,[[4,2,dame]],[[5,1,pion],[3,1,pion],[1,1,pion],[1,3,pion],[3,3,pion]],R),writeln(R).
% mangeBasDroite([4,2,dame],blanc,[[4,2,dame]],[[5,1,pion],[3,1,pion],[1,1,pion],[1,3,pion],[3,3,pion]],R),writeln(R).


%mouvement pour un pion
moveHautGauche([X,_,_],_,_,_,[],deplacement):- X2 is X-1,X2<0,!.
moveHautGauche([_,Y,_],_,_,_,[],deplacement):- Y2 is Y-1,Y2<0,!.
moveHautGauche([X,Y,Pion],Player,Blancs,Noirs,R,mange):- mangeHautGauche([X,Y,Pion],Player,Blancs,Noirs,R), R\==[],!.
moveHautGauche([X,Y,pion],blanc,Blancs,Noirs,[[Blancs3, Noirs, [[X2,Y2]]]],deplacement):-  X2 is X-1,Y2 is Y-1,not(member([X2,Y2,_],Blancs)), not(member([X2,Y2,_],Noirs)),!, delete(Blancs, [X,Y,pion], Blancs2), append(Blancs2, [[X2,Y2,pion]], Blancs3).
moveHautGauche([X,Y,dame],Player,Blancs,Noirs,S,deplacement):- chercheCaseVideHautGauche(X,Y,Blancs,Noirs,[],Retour), !, delete(Blancs, [X,Y,dame], Blancs2), moveDameHautGauche(Player, Blancs2, Noirs, Retour, S).
moveHautGauche(_,_,_,_,[],deplacement).

moveDameHautGauche(blanc, Blancs2, Noirs, [[X,Y]|Suite], Liste) :- !, moveDameHautGauche(blanc, Blancs2, Noirs, Suite,Resultat), append(Resultat, [[[[X,Y,dame]|Blancs2], Noirs, [[X,Y]]]], Liste).
moveDameHautGauche(noir, Blancs, Noirs2, [[X,Y]|Suite], Liste) :- !, moveDameHautGauche(noir, Blancs, Noirs2, Suite,Resultat), append(Resultat, [[Blancs, [[X,Y,dame]|Noirs2], [[X,Y]]]], Liste).
moveDameHautGauche(_,_,_,[],[]).

moveHautDroite([X,_,_],_,_,_,[],deplacement):- X2 is X+1,X2>9,!.
moveHautDroite([_,Y,_],_,_,_,[],deplacement):- Y2 is Y-1,Y2<0,!.
moveHautDroite([X,Y,Pion],Player,Blancs,Noirs,R,mange):- mangeHautDroite([X,Y,Pion],Player,Blancs,Noirs,R), R\==[],!.
moveHautDroite([X,Y,pion],blanc,Blancs,Noirs,[[Blancs3, Noirs, [[X2,Y2]]]],deplacement):- X2 is X+1,Y2 is Y-1,not(member([X2,Y2,_],Blancs)), not(member([X2,Y2,_],Noirs)),!, delete(Blancs, [X,Y,pion], Blancs2), append(Blancs2, [[X2,Y2,pion]], Blancs3).
moveHautDroite([X,Y,dame],Player,Blancs,Noirs,S,deplacement):- chercheCaseVideHautDroite(X,Y,Blancs,Noirs,[],Retour), !, delete(Blancs, [X,Y,dame], Blancs2), moveDameHautDroite(Player, Blancs2, Noirs, Retour, S).
moveHautDroite(_,_,_,_,[],deplacement).

moveDameHautDroite(blanc, Blancs2, Noirs, [[X,Y]|Suite], Liste) :- !, moveDameHautDroite(blanc, Blancs2, Noirs, Suite,Resultat), append(Resultat, [[[[X,Y,dame]|Blancs2], Noirs, [[X,Y]]]], Liste).
moveDameHautDroite(noir, Blancs, Noirs2, [[X,Y]|Suite], Liste) :- !, moveDameHautDroite(noir, Blancs, Noirs2, Suite,Resultat), append(Resultat, [[Blancs, [[X,Y,dame]|Noirs2], [[X,Y]]]], Liste).
moveDameHautDroite(_,_,_,[],[]).

moveBasGauche([X,_,_],_,_,_,[],deplacement):- X2 is X-1,X2<0,!.
moveBasGauche([_,Y,_],_,_,_,[],deplacement):- Y2 is Y+1,Y2>9,!.
moveBasGauche([X,Y,Pion],Player,Blancs,Noirs,R,mange):- mangeBasGauche([X,Y,Pion],Player,Blancs,Noirs,R), R\==[],!.
moveBasGauche([X,Y,pion],noir,Blancs,Noirs,[[Blancs, Noirs3, [[X2,Y2]]]],deplacement):-  X2 is X-1,Y2 is Y+1,not(member([X2,Y2,_],Blancs)), not(member([X2,Y2,_],Noirs)),!, delete(Noirs, [X,Y,pion], Noirs2), append(Noirs2, [[X2,Y2,pion]], Noirs3).
moveBasGauche([X,Y,dame],Player,Blancs,Noirs,S,deplacement):- chercheCaseVideBasGauche(X,Y,Blancs,Noirs,[],Retour), !, delete(Blancs, [X,Y,dame], Blancs2), moveDameBasGauche(Player, Blancs2, Noirs, Retour, S).
moveBasGauche(_,_,_,_,[],deplacement).

moveDameBasGauche(blanc, Blancs2, Noirs, [[X,Y]|Suite], Liste) :- !, moveDameBasGauche(blanc, Blancs2, Noirs, Suite,Resultat), append(Resultat, [[[[X,Y,dame]|Blancs2], Noirs, [[X,Y]]]], Liste).
moveDameBasGauche(noir, Blancs, Noirs2, [[X,Y]|Suite], Liste) :- !, moveDameBasGauche(noir, Blancs, Noirs2, Suite,Resultat), append(Resultat, [[Blancs, [[X,Y,dame]|Noirs2], [[X,Y]]]], Liste).
moveDameBasGauche(_,_,_,[],[]).

moveBasDroite([X,_,_],_,_,_,[],deplacement):- X2 is X+1,X2>9,!.
moveBasDroite([_,Y,_],_,_,_,[],deplacement):- Y2 is Y+1,Y2>9,!.
moveBasDroite([X,Y,Pion],Player,Blancs,Noirs,R,mange):- mangeBasDroite([X,Y,Pion],Player,Blancs,Noirs,R), R\==[],!.
moveBasDroite([X,Y,pion],noir,Blancs,Noirs,[[Blancs, Noirs3, [[X2,Y2]]]],deplacement):-  X2 is X+1,Y2 is Y+1,not(member([X2,Y2,_],Blancs)), not(member([X2,Y2,_],Noirs)),!, delete(Noirs, [X,Y,pion], Noirs2), append(Noirs2, [[X2,Y2,pion]], Noirs3).
moveBasDroite([X,Y,dame],Player,Blancs,Noirs,S,deplacement):- chercheCaseVideBasDroite(X,Y,Blancs,Noirs,[],Retour), !, delete(Blancs, [X,Y,dame], Blancs2), moveDameBasDroite(Player, Blancs2, Noirs, Retour, S).
moveBasDroite(_,_,_,_,[],deplacement).

moveDameBasDroite(blanc, Blancs2, Noirs, [[X,Y]|Suite], Liste) :- !, moveDameBasDroite(blanc, Blancs2, Noirs, Suite,Resultat), append(Resultat, [[[[X,Y,dame]|Blancs2], Noirs, [[X,Y]]]], Liste).
moveDameBasDroite(noir, Blancs, Noirs2, [[X,Y]|Suite], Liste) :- !, moveDameBasDroite(noir, Blancs, Noirs2, Suite,Resultat), append(Resultat, [[Blancs, [[X,Y,dame]|Noirs2], [[X,Y]]]], Liste).
moveDameBasDroite(_,_,_,[],[]).

%test
% moveBasDroite([4,2,pion],blanc,[[4,2,pion]],[[5,1,pion],[3,1,pion],[5,3,pion],[3,3,pion]],R,T),writeln(R).
% moveBasDroite([4,2,pion],blanc,[[4,2,pion]],[[1,1,pion]],R,T),writeln(R).
% moveBasDroite([0,7,pion],blanc,[[0,7,pion]],[[1,1,pion]],R,T),writeln(R).
% movePossible([0,7,pion],blanc,[[0,7,pion]],[[1,1,pion]],R,T),writeln(R).



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
movePossible(E,Camp,Blancs,Noirs,Solution2,Stype):-moveHautGauche(E,Camp,Blancs,Noirs,R1,Type1),
		moveHautDroite(E,Camp,Blancs,Noirs,R2,Type2),
		moveBasGauche(E,Camp,Blancs,Noirs,R3,Type3),
		moveBasDroite(E,Camp,Blancs,Noirs,R4,Type4),
		verifieSolution(R1,R2,Type1,Type2,S1,Stype1),
		verifieSolution(R3,S1,Type3,Stype1,S2,Stype2),
		verifieSolution(R4,S2,Type4,Stype2,S,Stype),
		changePionDameListeSolution(Camp,E,S,Solution2).
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
% movePossiblePlayer(blanc,[[0,7,pion]],[[1,1,pion]],[[0,7,pion]],S,Type),writeln(S).


remplitRetourExplore(EnTete, [Tete|Reste], [Retour1|Retour2]) :- append(EnTete, Tete, Retour1), remplitRetourExplore(EnTete,Reste, Retour2). 
remplitRetourExplore(_, [], []).

% explore l'arbre des mouvements possibles à partir de chaque mouvement initial d'un pion donné
% TODO : Format de retour qui merde, trouver une solution pour que une ligne = une suite de mouvements différente
%         et pas que tout s'agglutine sur la même ligne
%         C'est à cause de ma concaténation avec le M1 rendu par explore
%         Il faudrait rajouter à chaque ligne de M1 à l'en-tête [Pion, Blancs2, Noirs2, Moves] de manière séparée
exploreMoves(Camp, Pion, [[Blancs2, Noirs2, Moves]|Reste], [[[Pion, Blancs2, Noirs2, Moves]|M1]|M2], Cpt) :-
    returnPionsCamp(Camp, Blancs2, Noirs2, Pions), explore(Camp, Blancs2, Noirs2, Pions, M1, Cpt), exploreMoves(Camp, Pion, Reste, M2, Cpt).
exploreMoves(_,_,[],[],_).
    
% explore l'arbre des mouvements possibles pour chaque pion
explorePion(Camp, [[Pion, MovesPion]|RestePions], [M1|M2], Cpt) :-
    exploreMoves(Camp, Pion, MovesPion, M1, Cpt), explorePion(Camp, RestePions, M2, Cpt).
explorePion(_,[],[],_).

% explore l'arbre des mouvements possibles (on alternera chaque fois entre les camps)
% le retour M attendu devra être de la forme suivante :
% 	[ [Pion1,Blancs,Noirs,Move1], [Pion1, Blancs,Noirs,Move2], [Pion2,Blancs,Noirs,Move3],... ]
% Ainsi on pourra par la suite selectionner le dernier membre de chaque élément de la liste,
% pour étudier le nombre de pièces blanches et noires
% et sélectionner en conséquence le mouvement à effectuer d'après la meilleure évolution possible du jeu
% 
% TODO : Vérifier formats retour
% explore avec mange
explore(Camp, Blancs, Noirs, Pions, M, Cpt) :-
    Cpt > 0, NewCpt is Cpt-1, movePossiblePlayer(Camp, Blancs, Noirs, Pions, PionsMoves, mange), PionsMoves \== [], !, changePlayer(Camp, Camp2), explorePion(Camp2, PionsMoves, M, NewCpt).
% explore avec deplacement
explore(Camp, Blancs, Noirs, Pions, M, Cpt) :-
    Cpt > 0, NewCpt is Cpt-1, movePossiblePlayer(Camp, Blancs, Noirs, Pions, PionsMoves, deplacement),PionsMoves \== [],!, changePlayer(Camp, Camp2), explorePion(Camp2, PionsMoves, M, NewCpt).
% critère d'arrêt de la récursivité -> Cpt à 0 ou pas de move possible
explore(_,_,_,_,[],_).

%TESTS
% Degré 1
%   Déplacement
%     explore(blanc, [[4,2,pion]], [[4,3,pion]], [[4,2,pion]], M, 1).
%   Mange
%     explore(blanc, [[4,2,pion]], [[3,3,pion]], [[4,2,pion]], M, 1).
% Degré 2
%   2 déplacements
%     explore(blanc, [[4,2,pion]], [[4,3,pion]], [[4,2,pion]], M, 2).


ia(blanc,Blancs,Noirs,Blancs2,Noirs2,ListeMouvement,E):-movePossiblePlayer(blanc, Blancs, Noirs, Blancs, Liste,_),length(Liste,X),X>0,!,choixMove(X,Liste,Blancs2,Noirs2,ListeMouvement,E).
ia(noir,Blancs,Noirs,Blancs2,Noirs2,ListeMouvement,E):-movePossiblePlayer(noir, Blancs, Noirs, Noirs, Liste,_),length(Liste,X),X>0,choixMove(X,Liste,Blancs2,Noirs2,ListeMouvement,E).
% ia(blanc,[[4,2,pion]],[[5,1,pion],[3,1,pion],[1,1,pion],[1,3,pion],[3,3,pion]],Blancs,Noirs,L,E).


%Changer un pion en dame
changePionDame(blanc, Blancs, Noirs, ListeMouvement, [_,_,pion], Blancs2, Noirs) :- last(ListeMouvement, [X,0]), delete(Blancs, [X,0,_], L), append(L, [[X,0,dame]], Blancs2),!.
changePionDame(noir, Blancs, Noirs, ListeMouvement, [_,_,pion], Blancs, Noirs2) :- last(ListeMouvement, [X,9]), delete(Noirs, [X,9,_], L), append(L, [[X,9,dame]], Noirs2),!.
changePionDame(_, Blancs, Noirs, _, _, Blancs, Noirs).

changePionDameListeSolution(Camp,Pion,[[Blancs,Noirs,L]|S],[[Blancs2,Noirs2,L]|S2]):-!,
		changePionDame(Camp, Blancs, Noirs, L, Pion, Blancs2, Noirs2),
		changePionDameListeSolution(Camp,Pion,S,S2).
changePionDameListeSolution(_,_,[],[]).



applyMoves(Blancs, Noirs) :- retractall(blancs(_)), retractall(noirs(_)),assert(blancs(Blancs)), assert(noirs(Noirs)).
%

%lancement du jeu
play(Player,Blancs,Noirs,Blancs2,Noirs2,ListeMouvement,Pion,Etat):- ia(Player,Blancs,Noirs,Blancs2,Noirs2,ListeMouvement, Pion),
		gameover(Blancs,Noirs,Blancs2,Noirs2,Etat).
		
%tests play
% play(blanc,[[5,4,pion],[8,7,pion],[4,3,pion],[7,4,pion],[5,8,pion],[2,2,pion]],[[1,1,pion]],B,N,L,P,Winner).
% play(blanc,[[1,1,pion],[2,2,pion]],[[5,5,pion],[8,6,pion]],B,N,L,Pion,Etat).
% play(noir,[[2,7,pion],[7,8,pion]],[[2,5,pion],[2,1,pion],[6,1,pion],[8,3,pion],[8,1,pion],[1,2,pion],[8,9,pion],[5,2,pion],[6,3,pion],[5,4,pion],[5,6,pion],[9,4,pion]]]

% play(noir, [[1,6,pion],[1,4,pion],[4,5,pion],[7,6,pion],[9,6,pion],[0,7,pion],[2,7,pion],[4,7,pion],[6,7,pion],[8,7,pion],[1,8,pion],[3,8,pion],[5,8,pion],[7,8,pion],[9,8,pion],[0,9,pion],[2,9,pion],[4,9,pion],[6,9,pion],[8,9,pion]],[[1,0,pion],[3,0,pion],[5,0,pion],[7,0,pion],[9,0,pion],[0,1,pion],[2,1,pion],[4,1,pion],[6,1,pion],[8,1,pion],[1,2,pion],[3,2,pion],[5,2,pion],[7,2,pion],[9,2,pion],[0,3,pion],[2,3,pion],[5,4,pion],[6,3,pion],[7,4,pion]],B,N,L,Pion,Etat),writeln(B),writeln(N),writeln(L).
% reponse
% blancs [[1,6,pion],[4,5,pion],[7,6,pion],[9,6,pion],[0,7,pion],[2,7,pion],[4,7,pion],[6,7,pion],[8,7,pion],[1,8,pion],[3,8,pion],[5,8,pion],[7,8,pion],[9,8,pion],[0,9,pion],[2,9,pion],[4,9,pion],[6,9,pion],[8,9,pion]]
% noirs [[0,5,pion],[1,0,pion],[3,0,pion],[5,0,pion],[7,0,pion],[9,0,pion],[0,1,pion],[2,1,pion],[4,1,pion],[6,1,pion],[8,1,pion],[1,2,pion],[3,2,pion],[5,2,pion],[7,2,pion],[9,2,pion],[0,3,pion],[5,4,pion],[6,3,pion],[7,4,pion]]
% liste [[0,5]]
% pion [2, 3, pion]
% Etat continuer
% build_reply_play([[1,6,pion],[4,5,pion],[7,6,pion],[9,6,pion],[0,7,pion],[2,7,pion],[4,7,pion],[6,7,pion],[8,7,pion],[1,8,pion],[3,8,pion],[5,8,pion],[7,8,pion],[9,8,pion],[0,9,pion],[2,9,pion],[4,9,pion],[6,9,pion],[8,9,pion]],[[0,5,pion],[1,0,pion],[3,0,pion],[5,0,pion],[7,0,pion],[9,0,pion],[0,1,pion],[2,1,pion],[4,1,pion],[6,1,pion],[8,1,pion],[1,2,pion],[3,2,pion],[5,2,pion],[7,2,pion],[9,2,pion],[0,3,pion],[5,4,pion],[6,3,pion],[7,4,pion]],noir,[2, 3, pion],[[0,5]],continuer,JSON),writeln(JSON).
 
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
:- http_handler(root(game_state), game_state, []).

% Creation des objets JSON utilisé dans l'application
:- json_object pion(x:integer, y:integer) + [type=pion].
:- json_object joueur(j:integer).
:- json_object dame(x:integer, y:integer) + [type=dame].
:- json_object position(x:integer, y:integer).
:- json_object blancs(blancs:list).
:- json_object noirs(noirs:list).
:- json_object positions(positions:list).
:- json_object game(joueur: integer, blancs:list, noirs:list).
:- json_object requestState(blancs:list, noirs:list,blancs2:list, noirs2:list).
:- json_object turn(etat:integer, joueur: integer, blancs:list, noirs:list, pion:compound, mouvements:list).
:- json_object posibilite(blancs:list, noirs:list, mouvements:list).
:- json_object move(pion: compound, posibilite:list).
:- json_object movesAllowed(move:list).
:- json_object state(etat:integer).

% Predicat qui lance le server
server(Port) :-	http_server(http_dispatch, [port(Port)]).

% Prédicat init qui est appellé quand on appelle l'url //init
% Le prédicat lance le jeu en appelant la méthode init du jeu
% Le prédicat renvoie la liste des pions blancs et noirs et le joueur qui doit jouer en format JSON
init_server(_Request) :-	init,
							noirs(ListeNoirs),
							blancs(ListeBlancs),
							build_reply_init(ListeBlancs,ListeNoirs, 0, JSON),
							reply_json(JSON).

% Prédicat play_server qui est appellé quand on appelle l'url //play
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


moves_allowed_server(Request) :-	http_read_json(Request, JsonIn,[json_object(term)]),
									%format(user_output,"JsonIn is: ~p~n",[JsonIn]),
									json_to_prolog(JsonIn, Data), game_get_data_informations(Data, J, Blancs, Noirs),
									%format(user_output,"Data is: ~p~n",[Data]),
									%format(user_output,"J is: ~p~n",[J]),
									%format(user_output,"Blancs is: ~p~n",[Blancs]),
									%format(user_output,"Noirs is: ~p~n",[Noirs]),
									call_movePossiblePlayer(J,Blancs, Noirs,S),
									%format(user_output,"S is: ~p~n",[S]),
									build_reply_moves_allowed(S,Json),
									%format(user_output,"json is: ~p~n",[Json]),
									reply_json(Json).


game_state(Request) :- 	http_read_json(Request, JsonIn,[json_object(term)]),
						json_to_prolog(JsonIn, Data), requestState_get_data_informations(Data, Blancs, Noirs, Blancs2, Noirs2),
						gameover(Blancs, Noirs, Blancs2, Noirs2, Winner),
						build_etat_predicat_int(Winner,W),
						S = state(W),
						prolog_to_json(S,Json),
						reply_json(Json).

call_movePossiblePlayer(blanc, Blancs, Noirs, S) :- movePossiblePlayer(blanc, Blancs, Noirs, Blancs, S,_).
call_movePossiblePlayer(noir, Blancs, Noirs, S) :- movePossiblePlayer(noir, Blancs, Noirs, Noirs, S,_).

% Predicat qui permet de construire le JSON relatif à la reponse d'init
%	ListeBlancs = Liste contenant des pions et des dames
%	ListeNoirs = Liste contenant des pions et des dames
build_reply_init(ListeBlancs,ListeNoirs,Joueur,JSON) :-		convert_list_pion_to_json_object(ListeBlancs, LB),
															convert_list_pion_to_json_object(ListeNoirs, LN),
															J = game(Joueur, LB, LN),
															prolog_to_json(J,JSON).


% Predicat qui permet de construire le JSON relatif à la reponse de play
%	ListeBlancs = Liste contenant des pions et des dames
%	ListeNoirs = Liste contenant des pions et des dames
%	Joueur = Joueur qui joue le tour (blanc//noir)
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
build_reply_moves_allowed(L,JSON) :-	build_liste_move_json(L,Moves),
										JSONProlog = movesAllowed(Moves),
										prolog_to_json(JSONProlog,JSON).

%[[X,Y,pion],[[Blancs,Noirs,ListeMouvement],...]]
build_liste_move_json([],[]).
build_liste_move_json([H|T],Moves) :- build_liste_move_json(T,M), build_possibilites_for_pion_json(H,Move), append([Move],M,Moves).


%[Pion, [[Blancs,Noirs,ListeMouvement],...]]
build_possibilites_for_pion_json([H|T],M) :- convert_pion_to_json_object_pion(H,P), prolog_to_json(P,Pion), decompose(T,TD), build_move_possibilites_json(TD,Possibilities), Move = move(Pion,Possibilities), prolog_to_json(Move, M).

%OK ! Tested : [[ [[1,6,pion]],[[3,0,pion],[5,0,pion],[7,0,pion],[2,1,pion]],[[2,1]] ],[ [[1,6,pion]],[[3,0,pion],[5,0,pion],[7,0,pion],[2,1,pion]],[[2,1]] ]]  % [[Blancs,Noirs,ListeMouvement],...]
build_move_possibilites_json([],[]).
build_move_possibilites_json([H|T],Poss) :- build_move_possibilites_json(T,Poss2), build_move_possibilite_json(H,P), append([P],Poss2,Poss).

decompose([H|_],H).

%OK ! Tested : [ [[1,6,pion]],[[3,0,pion],[5,0,pion],[7,0,pion],[2,1,pion]],[[2,1]] ] % [Blancs,Noirs,ListeMouvement]
build_move_possibilite_json([H|T],P):- convert_list_pion_to_json_object(H,B), build_move_possibilite_json_Noir_Mouvements(T,N,M), Poss = posibilite(B,N,M), prolog_to_json(Poss,P).
build_move_possibilite_json_Noir_Mouvements([H|T],N,M) :- build_move_possibilite_json_Mouvements(T,M), convert_list_pion_to_json_object(H,N).
build_move_possibilite_json_Mouvements([H|_],M) :- convert_list_position_to_json_object(H,M).

requestState_get_data_informations(Data, B, N, B2, N2) :- 	requestState_get_blancs(Data, LB),
															convert_list_pion_json_to_object(LB,B),
															requestState_get_noirs(Data, LN),
															convert_list_pion_json_to_object(LN,N),
															requestState_get_blancs2(Data, LB2),
															convert_list_pion_json_to_object(LB2,B2),
															requestState_get_noirs2(Data, LN2),
															convert_list_pion_json_to_object(LN2,N2).


% Prédicat game_get_data_informations qui permet de recuperer la Liste Blancs, la Liste Noirs et le joueur dans un objet JSON de type game
game_get_data_informations(Data, J, Blancs, Noirs):-	game_get_joueur(Data, J),
														game_get_blancs(Data, LB),
														convert_list_pion_json_to_object(LB,Blancs),
														game_get_noirs(Data, LN),
														convert_list_pion_json_to_object(LN,Noirs).

% Prédicat game_get_data_informations qui permet de recuperer le numero du joueur dans un objet JSON de type game
game_get_joueur(game(0,_,_),blanc).
game_get_joueur(game(1,_,_),noir).

% Prédicat game_get_data_informations qui permet de recuperer la Liste Blancs dans un objet JSON de type game
game_get_blancs(game(_,X,_),X).

% Prédicat game_get_data_informations qui permet de recuperer la Liste Noirs dans un objet JSON de type game
game_get_noirs(game(_,_,X),X).

% Prédicat requestState_get_blancs qui permet de recuperer la Liste Blancs (1 ou 2) dans un objet JSON de type requestState
requestState_get_blancs(requestState(X,_,_,_),X).
requestState_get_blancs2(requestState(_,_,X,_),X).

% Prédicat requestState_get_noirs qui permet de recuperer la Liste Noirs (1 ou 2) dans un objet JSON de type requestState
requestState_get_noirs(requestState(_,X,_,_),X).
requestState_get_noirs2(requestState(_,_,_,X),X).

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
convert_list_pion_json_to_object([H|T],O2) :- convert_list_pion_json_to_object(T, O), convert_pion_to_json_object_pion(X,H), append([X],O,O2), !.

% Predicat qui permet de construire le JSON d'une liste de position
convert_list_position_to_json_object([], []).
convert_list_position_to_json_object([H|T],O2) :- convert_list_position_to_json_object(T, O), convert_position_to_json_object_position(H,X), append([X],O,O2), !.

% Méthode de conversion d'un pion ou d'une dame ([1, 2, pion] ou [1, 2, dame]) en Objet JSON Prolog (pion(1,2) ou dame(1,2))
convert_pion_to_json_object_pion([X,Y,pion],pion(X,Y)) :- !.
convert_pion_to_json_object_pion([X,Y,dame],dame(X,Y)).

% Méthode de conversion d'une position ([1, 2]) en Objet JSON Prolog (position(1,2))
convert_position_to_json_object_position([X,Y],position(X,Y)).
