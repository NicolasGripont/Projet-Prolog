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

% Creation des objets JSON utilisé dans l'application
:- json_object pion(x:integer, y:integer) + [type=pion].
:- json_object joueur(j:integer).
:- json_object dame(x:integer, y:integer) + [type=dame].
:- json_object position(x:integer, y:integer).
:- json_object blancs(blancs:list).
:- json_object noirs(noirs:list).
:- json_object positions(positions:list).
:- json_object game(joueur: integer, blancs:list, noirs:list).

% predicat([[Blancs,Noirs,[Positions]]]) => L
%
% predicat([[Pion, L]])
% Predicat qui lance le server
server(Port) :-	http_server(http_dispatch, [port(Port)]).

% Prédicat init qui est appellé pour débuter le jeu
% On va brancher avec le init du Jeu.pl
init_server(Request) :- ListeBlancs = [[1,2,dame],[3,4,pion]], ListeNoirs = [[1,3,pion],[8,4,pion]], build_reply_init(ListeBlancs,ListeNoirs, 0, JSON), reply_json(JSON).

% Prédicat appellé pour l'url localhost:port/blancs
%		
play_server(Request) :- http_read_json(Request, JsonIn,[json_object(term)]), json_to_prolog(JsonIn, Data), game_get_data_informations(Data, J, Blancs, Noirs), 
					format(user_output,"Joueur is: ~p~n",[J]),
				   	JsonOut=JsonIn,
				   	reply_json(JsonOut).

game_get_data_informations(Game, J, Blancs, Noirs):- game_get_joueur(Data, J), game_get_blancs(Data, Blancs), game_get_noirs(Data, Noirs).
game_get_joueur(game(X,Y,Z),X). 
game_get_blancs(game(X,Y,Z),Y). 
game_get_noirs(game(X,Y,Z),Z).

% Predicat qui permet de construire le JSON relatif à la reponse d'init
%	ListeBlancs = Liste contenant des pions et des dames
%	ListeNoirs = Liste contenant des pions et des dames
%	Joueur = Soit 0, soit 1
build_reply_init(ListeBlancs,ListeNoirs, Joueur, JSON) :- build_list_blancs(ListeBlancs, LB), build_list_noirs(ListeNoirs, LN), J = game(Joueur, LB, LN), prolog_to_json(J,JSON).
build_list_blancs(ListeBlancs, LB) :- convert_list_pion_to_json_object(ListeBlancs,LB).
build_list_noirs(ListeNoirs, LN) :- convert_list_pion_to_json_object(ListeNoirs,LN).

convert_list_pion_to_json_object([], []):- !.
convert_list_pion_to_json_object([H|T],O2) :- convert_list_pion_to_json_object(T, O), convert_pion_to_json_object_pion(H,X), append([X],O,O2), !.

convert_list_position_to_json_object([], []):- !.
convert_list_position_to_json_object([H|T],O2) :- convert_list_position_to_json_object(T, O), convert_position_to_json_object_position(H,X), writeln(X), append([X],O,O2), !.

% Méthode de conversion d'un pion ou d'une dame ([1, 2, pion] ou [1, 2, dame]) en Objet JSON Prolog (pion(1,2) ou dame(1,2))
convert_pion_to_json_object_pion(L,O) :- convert_pion_to_json_object_pion_X(L,X), convert_pion_to_json_object_pion_Y(L,X,Y), convert_pion_to_json_object_pion_Name(L,X,Y,O), !.
convert_pion_to_json_object_pion_X([H|_],X) :- X = H.
convert_pion_to_json_object_pion_Y([],_,_).
convert_pion_to_json_object_pion_Y([X|T],X,Y) :- convert_pion_to_json_object_pion_Y(T,X,Y).
convert_pion_to_json_object_pion_Y([H|_],_,Y) :- Y = H.
convert_pion_to_json_object_pion_Name([],_).
convert_pion_to_json_object_pion_Name([pion|_], X, Y, O) :- O = pion(X,Y).
convert_pion_to_json_object_pion_Name([dame|_], X, Y, O) :- O = dame(X,Y).
convert_pion_to_json_object_pion_Name([_|T], X, Y, O) :- convert_pion_to_json_object_pion_Name(T,X,Y,O).

% Méthode de conversion d'une position ([1, 2]) en Objet JSON Prolog (position(1,2))
convert_position_to_json_object_position(L,O) :- convert_position_to_json_object_position_X(L,X), convert_position_to_json_object_position_Y(L,X,Y), O = position(X,Y), !.
convert_position_to_json_object_position_X([H|_],X) :- X = H.
convert_position_to_json_object_position_Y([],_,_).
convert_position_to_json_object_position_Y([X|T],X,Y) :- convert_position_to_json_object_position_Y(T,X,Y).
convert_position_to_json_object_position_Y([H|_],_,Y) :- Y = H.

%tests(List) :- convert_list_pion_to_json_object([[1,2,pion],[1,4,dame]],Y), X = blancs(Y), prolog_to_json(Y,List).
%tests(List) :- Y = [pion(1,2,"pion"),pion(1,2,"dame")], X = blancs(Y), prolog_to_json(Y,List).
%tests2(List) :- Y = [position(1,2), position(1,2)], X = positions(Y), prolog_to_json(X,List).
%L = [[1,2,pion],[1,4,dame]], convert_list_pion_to_json_object(L,X), Y = blancs(X), prolog_to_json(Y,JSON), reply_json(JSON).
%tests(List) :- prolog_to_json(pion(1,2),List).

% on veut ça game(0,[dame(1,4)],[dame(1,4)] ===> json([joueur=0, blancs=[json([x=1, y=4, ... = ...])], noirs=[json([x=1, ... = ...|...])]])
%tests(J) :- build_reply_init([[1,2,dame]], [[3,4,pion]], 0, L), writeln(L), X = game(0,[dame(1,4)],[dame(1,4)]), writeln(X), prolog_to_json(L,J).
%build(List,L) :- ListeBlancs = [[1,4,dame]], ListeNoirs = [[1,4,pion]], Joueur is 0, build_reply_init(ListeBlancs,ListeNoirs, Joueur, L).%, prolog_to_json(L, List).
%tests(List) :- L = [[1,4,dame]], convert_list_pion_to_json_object(L,X), Y = blancs(X), writeln(Y), prolog_to_json(Y,List).
%request_blancs(_Request) :-  List = [[1,2,pion],[3,4,dame]], with_output_to(codes(Codes), write(List)), format('Content-type: text/plain~n~n'),format("~s", [Codes]).
%request_noirs(_Request) :-  List = [[1,2,pion],[3,4,dame]], with_output_to(codes(Codes), write(List)), format('Content-type: text/plain~n~n'),format("~s", [Codes]).

