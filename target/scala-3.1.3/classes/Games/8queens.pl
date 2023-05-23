:- use_module(library(clpfd)).
n_queens(N, Init, Solution) :-
    length(Solution, N),
    Solution ins 1..N,
    Init ins 0..N,
    safe_queens(Solution),
    match_lists(Init, Solution).

safe_queens([]).
safe_queens([Q|Qs]) :- safe_queens(Qs, Q, 1), safe_queens(Qs).

safe_queens([], _, _).
safe_queens([Q|Qs], Q0, D0) :-
    Q0 #\= Q,
    abs(Q0 - Q) #\= D0,
    D1 #= D0 + 1,
    safe_queens(Qs, Q0, D1).

match_lists([], []).
match_lists([X|Xs], [Y|Ys]) :-
    (X =:= 0 ; X = Y),
    match_lists(Xs, Ys).