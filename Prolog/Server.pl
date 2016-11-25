% Chargement des modules pour le serveur et la gestion du JSON
:- use_module(library(http/thread_httpd)).
:- use_module(library(http/http_dispatch)).
:- use_module(library(http/html_write)).
:- use_module(library(http/json)).
:- use_module(library(http/json_convert)).
:- use_module(library(http/http_json)).

% Surcharge des urls avec les méthodes appellé pour chacune
:- http_handler(root(blancs), request_blancs, []).
:- http_handler(root(noirs), request_noirs, []).
:- http_handler(root(pion), request_pion, []).
:- http_handler(root(positions), request_positions, []).

% Creation des objets JSON utilisé dans l'application
:- json_object pion(x:integer, y:integer) + [type=pion].
:- json_object position(x:integer, y:integer).
:- json_object blancs(blancs:list).
:- json_object noirs(noirs:list).
:- json_object positions(positions:list).

% Predicat qui lance le server
server(Port) :-	http_server(http_dispatch, [port(Port)]).

% Prédicat appellé pour l'url localhost:port/blancs
request_blancs(_Request) :- prolog_to_json(blancs([pion(1,2),pion(1,4),dame(1,2)]),JSON), reply_json(JSON).

% Prédicat appellé pour l'url localhost:port/noirs
request_noirs(_Request) :- prolog_to_json(noirs([pion(1,2),pion(1,4),dame(1,2)]),JSON), reply_json(JSON).

% Prédicat appellé pour l'url localhost:port/pion
%request_pion(_Request) :- prolog_to_json(pion(1,2)),JSON), reply_json(JSON).

% Prédicat appellé pour l'url localhost:port/positions
%request_positions(_Request) :- prolog_to_json(positions([pion(1,2),pion(1,4),dame(1,2)]),JSON), reply_json(JSON).

convert_list_pion_to_json_object([], _).
convert_list_pion_to_json_object([H|T],O2) :- convert_list_pion_to_json_object(T, O), convert_pion_to_json_object_pion(H,X), append([X],O,O2), !.

convert_list_position_to_json_object([], _):- !.
convert_list_position_to_json_object([H|T],O2) :- convert_list_position_to_json_object(T, O), convert_position_to_json_object_position(H,X), writeln(X), append([X],O,O2), !.

% Méthode de conversion d'un pion ou d'une dame ([1, 2, pion] ou [1, 2, dame]) en Objet JSON Prolog (pion(1,2,"pion") ou pion(1,2,"dame"))
convert_pion_to_json_object_pion(L,O) :- convert_pion_to_json_object_pion_X(L,X), convert_pion_to_json_object_pion_Y(L,X,Y), convert_pion_to_json_object_pion_Name(L,N), O = pion(X,Y,N), !.
convert_pion_to_json_object_pion_X([H|_],X) :- X = H.
convert_pion_to_json_object_pion_Y([],_,_).
convert_pion_to_json_object_pion_Y([X|T],X,Y) :- convert_pion_to_json_object_pion_Y(T,X,Y).
convert_pion_to_json_object_pion_Y([H|_],_,Y) :- Y = H.
convert_pion_to_json_object_pion_Name([],_).
convert_pion_to_json_object_pion_Name([pion|_], N) :- N = "pion".
convert_pion_to_json_object_pion_Name([dame|_], N) :- N = "dame".
convert_pion_to_json_object_pion_Name([_|T], N) :- convert_pion_to_json_object_pion_Name(T,N).

% Méthode de conversion d'une postion ([1, 2]) en Objet JSON Prolog (pion(1,2,"pion"))
convert_position_to_json_object_position(L,O) :- convert_position_to_json_object_position_X(L,X), convert_position_to_json_object_position_Y(L,X,Y), O = postion(X,Y), !.
convert_position_to_json_object_position_X([H|_],X) :- X = H.
convert_position_to_json_object_position_Y([],_,_).
convert_position_to_json_object_position_Y([X|T],X,Y) :- convert_position_to_json_object_position_Y(T,X,Y).
convert_position_to_json_object_position_Y([H|_],_,Y) :- Y = H.

tests(List) :- prolog_to_json(pions([pion(1,2),pion(1,4),dame(1,2)]),List).
%request_blancs(_Request) :-  List = [[1,2,pion],[3,4,dame]], with_output_to(codes(Codes), write(List)), format('Content-type: text/plain~n~n'),format("~s", [Codes]).
%request_noirs(_Request) :-  List = [[1,2,pion],[3,4,dame]], with_output_to(codes(Codes), write(List)), format('Content-type: text/plain~n~n'),format("~s", [Codes]).

