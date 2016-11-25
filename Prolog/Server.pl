:- use_module(library(http/thread_httpd)).
:- use_module(library(http/http_dispatch)).
:- use_module(library(http/html_write)).
:- use_module(library(http/json)).
:- use_module(library(http/json_convert)).
:- use_module(library(http/http_json)).

:- http_handler(root(blancs), request_blancs, []).
:- http_handler(root(noirs), request_noirs, []).


server(Port) :-	http_server(http_dispatch, [port(Port)]).
prolog_to_json
request_blancs(_Request) :-  prolog_to_json([[1,2,pion],[3,4,dame]],List), with_output_to(codes(Codes), write(List)), format('Content-type: text/plain~n~n'),format("~s", [Codes]).

%request_blancs(_Request) :-  List = [[1,2,pion],[3,4,dame]], with_output_to(codes(Codes), write(List)), format('Content-type: text/plain~n~n'),format("~s", [Codes]).
%request_noirs(_Request) :-  List = [[1,2,pion],[3,4,dame]], with_output_to(codes(Codes), write(List)), format('Content-type: text/plain~n~n'),format("~s", [Codes]).

