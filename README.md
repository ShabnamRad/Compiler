Fixed Grammar (without left-recursion, with factorization):
1. program -> declaration-list EOF
2. declaration-list -> declaration-list declaration | ϵ

	2.1. declaration-list -> declaration-rest
	
	2.2. declaration-rest -> declaration declaration-rest | ϵ
	
=> declaration-list -> declaration declaration-list | ϵ
3. declaration -> var-declaration | fun-declaration
4. var-declaration -> type-specifier ID ; | type-specifier ID [ NUM ] ;
	4.1. var-declaration -> type-specifier ID num
	4.2. num -> ; | [ NUM ] ;
5. type-specifier -> int | void
6. fun-declaration -> type-specifier ID ( params ) compound-stmt
7. params -> param-list | void
8. param-list -> param-list , param | param
	8.1. param-list -> param param-rest
	8.2. param-rest -> , param param-rest | ϵ
9. param -> type-specifier ID | type-specifier ID [ ]
	9.1. param -> type-specifier ID brackets
	9.2. brackets -> [ ] | ϵ
10. compound-stmt -> { declaration-list statement-list }
11. statement-list -> statement-list statement | ϵ
	11.1. statement-list -> stmt-rest
	11.2. stmt-rest -> statement stmt-rest | ϵ
		=> statement-list -> statement statement-list | ϵ
12. statement -> expression-stmt | compound-stmt | selection-stmt | iteration-stmt |
return-stmt | switch-stmt
13. expression-stmt -> expression ; | continue ; | break ; | ;
14. selection-stmt -> if ( expression ) statement else statement
15. iteration-stmt -> while ( expression ) statement
16. return-stmt -> return ; | return expression ;
	16.1. return-stmt -> return expr-gen
	16.2. expr-gen -> expression ; | ;
17. switch-stmt -> switch ( expression ) { case-stmts default-stmt }
18. case-stmts -> case-stmts case-stmt | ϵ
	11.1. case-stmts -> case-stmt-rest
	11.2. case-stmt-rest -> case-stmt case-stmt-rest | ϵ
		=> case-stmts -> case-stmt case-stmts | ϵ
19. case-stmt -> case NUM : statement-list
20. default-stmt -> default : statement-list | ϵ
21. expression -> var = expression | simple-expression
22. var -> ID | ID [ expression ]
23. simple-expression -> additive-expression relop additive-expression | additive-expression
	23.1. simple-expression -> additive-expression add-expr-gen
	23.2. add-expr-gen -> relop additive-expression | ϵ
24. relop -> < | ==
25. additive-expression -> additive-expression addop term | term
	25.1. additive-expression -> term next-term
	25.2. next-term -> addop term next-term | ϵ
26. addop -> + | -
27. term -> term * factor | factor
	27.1. term -> factor next-factor
	27.2. next-factor -> * factor next-factor | ϵ
28. factor -> ( expression ) | var | call | NUM
29. call -> ID ( args )
30. args -> arg-list | ϵ
31. arg-list -> arg-list , expression | expression
	31.1. arg-list -> expression arg-rest
	31.2. arg-rest -> , expression arg-rest | ϵ

