:- dynamic blancs/1.
:- dynamic noirs/1.
:- dynamic cptDraw/1.


%%%% Test is the game is finished %%%
gameover(Winner) :- noirs(Noirs), blancs(Blancs), winner(Winner, Blancs, Noirs), !.  % There exists a winning configuration: We cut!
gameover('Draw', NewNoirs, NewBlancs) :- noirs(Noirs), blancs(Blancs), (length(Noirs, X), length(NewNoirs, X), length(Blancs, Y), length(NewBlancs, Y),
										cptDraw(CptDraw), NewCptDraw is CptDraw+1, retract(cptDraw(CptDraw)), assert(cptDraw(NewCptDraw)),!,
										NewCptDraw == 10).
gameover('Draw', _, _) :- cptDraw(CptDraw), retract(cptDraw(CptDraw)), assert(cptDraw(0)), false.

%%%% Test de victoire pour les joueurs.
winner(blanc, Blancs, Noirs) :- (length(Noirs, 0);(movePossiblePlayer(noir, Blancs, Noirs, Noirs, []))), !.
winner(noir, Blancs, Noirs) :- (length(Blancs, 0);(movePossiblePlayer(blanc, Blancs, Noirs, Blancs, []))), !.

%%%% Predicate to get the next player
changePlayer(blanc,noir).
changePlayer(noir,blanc).

%%%% Verify if the place has a pion
isPion(X,Y,blanc) :- blancs(Blancs), member([X, Y, _],Blancs).
isPion(X,Y,noir) :- noirs(Noirs), member([X, Y, _],Noirs).

%%%% DISPLAY
display1(I,Taille, [Liste|R2]) :- I<Taille, !,Y is I+1,display3(0,Taille,I,Liste), writeln(Liste), display1(Y,Taille, R2).
display1(_,_,[]).
display3(X,Taille,Ligne,[V|R]):-X<Taille,!,Y is X+1, display3(Y,Taille,Ligne,R), dessineCase(X, Ligne, V).
display3(_,_,_,[]).

%%%% Dessine la contenu de la case
dessineCase(X,Y, 'b') :- isPion( X,Y,blanc),!.
dessineCase(X,Y, 'n') :- isPion( X,Y,noir),!.
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

mangeBasGauche([X,Y,pion],blanc,Blancs,Noirs,Retour):- X2 is X-1, Y2 is Y+1,X3 is X-2,Y3 is Y+2,X3 >= 0 ,Y3 =< 9,member([X2,Y2,_],Noirs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Noirs,[X2,Y2,_],Noirs2),delete(Blancs,[X,Y,pion],Blancs2),mangeTouteDirection([X3,Y3,pion],blanc,[[X3,Y3,pion]|Blancs2],Noirs2,R),creerMouvement([[X3,Y3,pion]|Blancs2],Noirs2,R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeBasGauche([X,Y,pion],noir,Blancs,Noirs,Retour):- X2 is X-1, Y2 is Y+1,X3 is X-2,Y3 is Y+2,X3 >= 0 ,Y3 =< 9,member([X2,Y2,_],Blancs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Blancs,[X2,Y2,_],Blancs2),delete(Noirs,[X,Y,pion],Noirs2),mangeTouteDirection([X3,Y3,pion],noir,Blancs2,[[X3,Y3,pion]|Noirs2],R),creerMouvement(Blancs2,[[X3,Y3,pion]|Noirs2],R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeBasGauche(_,_,_,_,[]).

mangeBasDroite([X,Y,pion],blanc,Blancs,Noirs,Retour):- X2 is X+1, Y2 is Y+1,X3 is X+2,Y3 is Y+2,X3 =< 9 ,Y3 =< 9,member([X2,Y2,_],Noirs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Noirs,[X2,Y2,_],Noirs2),delete(Blancs,[X,Y,pion],Blancs2),mangeTouteDirection([X3,Y3,pion],blanc,[[X3,Y3,pion]|Blancs2],Noirs2,R),creerMouvement([[X3,Y3,pion]|Blancs2],Noirs2,R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeBasDroite([X,Y,pion],noir,Blancs,Noirs,Retour):- X2 is X+1, Y2 is Y+1,X3 is X+2,Y3 is Y+2,X3 =< 9 ,Y3 =< 9,member([X2,Y2,_],Blancs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Blancs,[X2,Y2,_],Blancs2),delete(Noirs,[X,Y,pion],Noirs2),mangeTouteDirection([X3,Y3,pion],noir,Blancs2,[[X3,Y3,pion]|Noirs2],R),creerMouvement(Blancs2,[[X3,Y3,pion]|Noirs2],R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeBasDroite(_,_,_,_,[]).

mangeHautDroite([X,Y,pion],blanc,Blancs,Noirs,Retour):- X2 is X+1, Y2 is Y-1,X3 is X+2,Y3 is Y-2,X3 =< 9 ,Y3 >= 0,member([X2,Y2,_],Noirs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Noirs,[X2,Y2,_],Noirs2),delete(Blancs,[X,Y,pion],Blancs2),mangeTouteDirection([X3,Y3,pion],blanc,[[X3,Y3,pion]|Blancs2],Noirs2,R),creerMouvement([[X3,Y3,pion]|Blancs2],Noirs2,R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeHautDroite([X,Y,pion],noir,Blancs,Noirs,Retour):- X2 is X+1, Y2 is Y-1,X3 is X+2,Y3 is Y-2,X3 =< 9 ,Y3 >= 0,member([X2,Y2,_],Blancs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Blancs,[X2,Y2,_],Blancs2),delete(Noirs,[X,Y,pion],Noirs2),mangeTouteDirection([X3,Y3,pion],noir,Blancs2,[[X3,Y3,pion]|Noirs2],R),creerMouvement(Blancs2,[[X3,Y3,pion]|Noirs2],R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeHautDroite(_,_,_,_,[]).

creerMouvement(B,N,[],[[B,N,[]]]):-!.
creerMouvement(_,_,S,S).
%tests
%creerMouvement([],[],[],R).

ajoutMouvement(Pos,[[Blancs,Noirs,ListeMouvement]|Suite],[[Blancs,Noirs,[Pos|ListeMouvement]]|R]):-!,ajoutMouvement(Pos,Suite,R).
ajoutMouvement(_,[],[]).
%Tests
%ajoutMouvement([1,2],[[[],[],[[0,0]]],[[],[],[[1,1]]]],R).
%ajoutMouvement([1,2],[[[],[],[[0,0]]],[[],[],[[1,1]]]],R).

mangeHautGauche([X,Y,pion],blanc,Blancs,Noirs,Retour):- X2 is X-1, Y2 is Y-1,X3 is X-2,Y3 is Y-2,X3 >= 0,Y3 >= 0,member([X2,Y2,_],Noirs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Noirs,[X2,Y2,_],Noirs2),delete(Blancs,[X,Y,pion],Blancs2),mangeTouteDirection([X3,Y3,pion],blanc,[[X3,Y3,pion]|Blancs2],Noirs2,R),creerMouvement([[X3,Y3,pion]|Blancs2],Noirs2,R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeHautGauche([X,Y,pion],noir,Blancs,Noirs,Retour):- X2 is X-1, Y2 is Y-1,X3 is X-2,Y3 is Y-2,X3 >= 0,Y3 >= 0,member([X2,Y2,_],Blancs),not(member([X3,Y3,_],Noirs)),not(member([X3,Y3,_],Blancs)),!,delete(Blancs,[X2,Y2,_],Blancs2),delete(Noirs,[X,Y,pion],Noirs2),mangeTouteDirection([X3,Y3,pion],noir,Blancs2,[[X3,Y3,pion]|Noirs2],R),creerMouvement(Blancs2,[[X3,Y3,pion]|Noirs2],R,Sortie),ajoutMouvement([X3,Y3],Sortie,Retour).
mangeHautGauche(_,_,_,_,[]).
%test mangehautgauche
% mangeHautGauche([4,4,pion],blanc,[[4,4,pion]],[[1,1,pion],[3,3,pion]],R).

longueurChaine([],0).
longueurChaine([[_,_,T]|_],X):-length(T,X).

estSolution(Liste,Maxi,Maxi,Liste):-!.
estSolution(_,_,_,[]).


maxSolution(R1,R2,R3,R4,S):-longueurChaine(R1,X),longueurChaine(R2,Y),longueurChaine(R3,Z),longueurChaine(R4,W),max_list([X,Y,Z,W],Maxi),estSolution(R1,X,Maxi,S1),estSolution(R2,Y,Maxi,S2),estSolution(R3,Z,Maxi,S3),estSolution(R4,W,Maxi,S4),append(S1,S2,S5),append(S5,S3,S6),append(S6,S4,S).


% cherche les possiblités pour manger un pion dans chaque direction et
% retourne les solutions possibles comprenant : le chemin + l'etat du
% jeu.
% pour un Pion
mangeTouteDirection([X,Y,pion],Couleur,Blancs,Noirs,R):-mangeHautGauche([X,Y,pion],Couleur,Blancs,Noirs,R1),mangeHautDroite([X,Y,pion],Couleur,Blancs,Noirs,R2),mangeBasGauche([X,Y,pion],Couleur,Blancs,Noirs,R3),mangeBasDroite([X,Y,pion],Couleur,Blancs,Noirs,R4),maxSolution(R1,R2,R3,R4,R).
%tests
% mangeTouteDirection([4,2,pion],blanc,[[4,2,pion]],[[5,1,pion],[3,1,pion],[1,1,pion],[1,3,pion],[3,3,pion]],R),writeln(R).


%mouvement pour un pion
moveHautGauche([X,_,_],_,_,_,[]):- X2 is X-1,X2<0,!.
moveHautGauche([_,Y,_],_,_,_,[]):- Y2 is Y-1,Y2<0,!.
moveHautGauche([X,Y,pion],Player,Blancs,Noirs,R):- mangeHautGauche([X,Y,pion],Player,Blancs,Noirs,R), R\==[],!.
moveHautGauche([X,Y,pion],blanc,Blancs,Noirs,[[Blancs3, Noirs, [[X2,Y2]]]]):-  X2 is X-1,Y2 is Y-1,not(member([X2,Y2,_],Blancs)), not(member([X2,Y2,_],Noirs)),!, delete(Blancs, [X,Y,pion], Blancs2), append(Blancs2, [[X2,Y2,pion]], Blancs3).
moveHautGauche(_,_,_,_,[]).

moveHautDroite([X,_,_],_,_,_,[]):- X2 is X+1,X2>9,!.
moveHautDroite([_,Y,_],_,_,_,[]):- Y2 is Y-1,Y2<0,!.
moveHautDroite([X,Y,pion],Player,Blancs,Noirs,R):- mangeHautDroite([X,Y,pion],Player,Blancs,Noirs,R), R\==[],!.
moveHautDroite([X,Y,pion],blanc,Blancs,Noirs,[[Blancs3, Noirs, [[X2,Y2]]]]):- X2 is X+1,Y2 is Y-1,not(member([X2,Y2,_],Blancs)), not(member([X2,Y2,_],Noirs)),!, delete(Blancs, [X,Y,pion], Blancs2), append(Blancs2, [[X2,Y2,pion]], Blancs3).
moveHautDroite(_,_,_,_,[]).

moveBasGauche([X,_,_],_,_,_,[]):- X2 is X-1,X2<0,!.
moveBasGauche([_,Y,_],_,_,_,[]):- Y2 is Y+1,Y2>9,!.
moveBasGauche([X,Y,pion],Player,Blancs,Noirs,R):- mangeBasGauche([X,Y,pion],Player,Blancs,Noirs,R), R\==[],!.
moveBasGauche([X,Y,pion],noir,Blancs,Noirs,[[Blancs, Noirs3, [[X2,Y2]]]]):-  X2 is X-1,Y2 is Y+1,not(member([X2,Y2,_],Blancs)), not(member([X2,Y2,_],Noirs)),!, delete(Noirs, [X,Y,pion], Noirs2), append(Noirs2, [[X2,Y2,pion]], Noirs3).
moveBasGauche(_,_,_,_,[]).

moveBasDroite([X,_,_],_,_,_,[]):- X2 is X+1,X2>9,!.
moveBasDroite([_,Y,_],_,_,_,[]):- Y2 is Y+1,Y2>9,!.
moveBasDroite([X,Y,pion],Player,Blancs,Noirs,R):- mangeBasDroite([X,Y,pion],Player,Blancs,Noirs,R), R\==[],!.
moveBasDroite([X,Y,pion],noir,Blancs,Noirs,[[Blancs, Noirs3, [[X2,Y2]]]]):-  X2 is X+1,Y2 is Y+1,not(member([X2,Y2,_],Blancs)), not(member([X2,Y2,_],Noirs)),!, delete(Noirs, [X,Y,pion], Noirs2), append(Noirs2, [[X2,Y2,pion]], Noirs3).
moveBasDroite(_,_,_,_,[]).
%test
% moveBasDroite([4,2,pion],blanc,[[4,2,pion]],[[5,1,pion],[3,1,pion],[5,3,pion],[3,3,pion]],R),writeln(R).




%moveHautGauche
% moveHautGauche([4,4,pion],blanc,[[4,4,pion]],[[1,1,pion],[2,2,pion]],R).
% moveHautGauche([4,4,pion],blanc,[[4,4,pion]],[[1,1,pion],[3,3,pion]],R).
%
%

%mouvement pour une dame (a faire)
% moveHautGauche([X,Y,dame],blanc,Blancs,Noirs,[[X2,Y2,dame]|Blancs]):-X2
% is X-1, Y2 is Y-1,
% X2>=0,Y2>=0,not(member([X2,Y2,_],Blancs)),!,not(member([X2,Y2,_],Noirs)).
%

% S est du type [[Blancs,Noirs,[[posX,posY],...]],...]
movePossible(E,Camp,Blancs,Noirs,S):-moveHautGauche(E,Camp,Blancs,Noirs,R1),moveHautDroite(E,Camp,Blancs,Noirs,R2),moveBasGauche(E,Camp,Blancs,Noirs,R3),moveBasDroite(E,Camp,Blancs,Noirs,R4),maxSolution(R1,R2,R3,R4,S).
%tests
% movePossible([4,2,pion],blanc,[[4,2,pion]],[[5,1,pion],[3,1,pion],[1,1,pion],[1,3,pion],[3,3,pion]],R),writeln(R).
% movePossible([4,2,pion],blanc,[[4,2,pion]],[[5,1,pion],[3,1,pion],[5,3,pion],[3,3,pion]],R),writeln(R).
% movePossible([4,2,pion],blanc,[[4,2,pion]],[[1,1,pion]],R),writeln(R).

%methode naive on prend la premiere solution trouvee
choixMove([_,_,_],[[Blancs,Noirs,ListeMouvement]|_],Blancs,Noirs,ListeMouvement):-!.
choixMove(_,[],Blancs,Noirs,[]):- blancs(Blancs),noirs(Noirs).


ia(blanc,Blancs,Noirs,Blancs2,Noirs2,ListeMouvement,E):-repeat,length(Blancs,X),Index is random(X),nth0(Index,Blancs,E),movePossible(E,blanc,Blancs,Noirs,L),L \== [],!,choixMove(E,L,Blancs2,Noirs2,ListeMouvement).
ia(noir,Blancs,Noirs,Blancs2,Noirs2,ListeMouvement,E):-repeat,length(Noirs,X),Index is random(X),nth0(Index,Noirs,E),movePossible(E,noir,Blancs,Noirs,L),L \== [],!,choixMove(E,L,Blancs2,Noirs2,ListeMouvement).
% ia(blanc,[[4,2,pion]],[[5,1,pion],[3,1,pion],[1,1,pion],[1,3,pion],[3,3,pion]],Blancs,Noirs,L,E).

%On regarde tous les mouvements possibles pour un joueur
movePossiblePlayer(Camp, Blancs, Noirs, [Tete|Pions], S) :- movePossible(Tete, Camp, Blancs, Noirs, T), movePossiblePlayer(Camp, Blancs, Noirs, Pions, R), append(T,R,S),!.
movePossiblePlayer(_, _, _, [], _).


%Changer un pion en dame

changePionDame(blanc, Noirs, Blancs, ListeMouvement, [_,_,pion], Blancs2, Noirs) :- last(ListeMouvement, [X,0]), delete(Blancs, [X,0,_], L), append(L, [X,0,dame], Blancs2),!.
changePionDame(noir, Noirs, Blancs, ListeMouvement, [_,_,pion], Blancs, Noirs2) :- last(ListeMouvement, [X,9]), delete(Noirs, [X,9,_], L), append(L, [X,9,dame], Noirs2),!.
changePionDame(_, Noirs, Blancs, _, _, Blancs, Noirs).

applyMoves(Blancs, Noirs, Blancs2, Noirs2) :- retract(blancs(Blancs)), retract(noirs(Noirs)),assert(blancs(Blancs2)), assert(noirs(Noirs2)).
%

play(Player):-  write('New turn for: '), ((Player==blanc, writeln('Blancs'));(Player==noir, writeln('Noirs'))),
		noirs(Noirs),
		blancs(Blancs),

		display1(0,10,_),
		writeln(" "),

		ia(Player,Blancs,Noirs,Blancs2,Noirs2,ListeMouvement, E),

		%TODO
		%Envoi données

		%changePionDame(Player, Noirs2, Blancs2, ListeMouvement, E, Blancs3, Noirs3),

		not(((gameover(blanc), !, write('Game is Over. Winner: '), writeln('Blancs'));
				(gameover(noir), !, write('Game is Over. Winner: '), writeln('Noirs'));
		(gameover('Draw', Blancs, Noirs), !, writeln('Game is Over. Draw')))),

		applyMoves(Blancs, Noirs, Blancs2, Noirs2),
	    changePlayer(Player,NextPlayer), % Change the player before next turn
	    sleep(1),
		play(NextPlayer). % next turn!


init :-creerListe(noir,L1),creerListe(blanc,L2),assert(noirs(L1)),assert(blancs(L2)), assert(cptDraw(0)), play(blanc).

