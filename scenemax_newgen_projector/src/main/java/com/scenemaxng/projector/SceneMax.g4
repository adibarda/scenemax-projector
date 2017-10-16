// 
grammar SceneMax;
prog
   : (statement ';') * EOF
   ;
statement
   : define_resource	# defResource
   | define_variable	# defVar
   | action_statement   # actionStatement
   ;

// THE LANGUAGE SYNTAX
define_resource
   : define_model 	# defModel
   | define_sprite 	# defSprite
   ;

define_model : res_var_decl isa_expr model_expr from_expr file_var_decl ;

define_sprite : res_var_decl isa_expr sprite_expr from_expr file_var_decl ;

define_variable : var_decl isa_expr res_var_decl ;


// Action statements
action_statement : action_operation 'async'* ;

action_operation
   : rotate		# rotateStatement
   | move		# moveStatement
   | play		# playStatement
   | animate    # animateStatement
   ;

rotate : var_decl '.' 'rotate' '(' (axis_expr (',' axis_expr)*) ')' 'in' speed_expr ;

move : var_decl '.' 'move' '(' (axis_expr (',' axis_expr)*) ')' ;

play : var_decl '.' 'play' '(' frames_expr ')' ;

animate : var_decl '.' 'animate' '(' (anim_expr ('then' anim_expr)*) ')'  ;

///////////////////////////////////////////////////////////////////////////

anim_expr : animation_name speed_of_expr ;

speed_of_expr : 'at' 'speed' 'of' number ;

animation_name : ID ;

speed_expr : number 'seconds' ;

frames_expr: 'frames' integer 'to' integer ;

axis_expr : axis_id number_sign? number ;

axis_id : X | Y | Z ;

res_type_expr : model_expr | sprite_expr ;

model_expr : 'MODEL' | 'model' ;

sprite_expr : 'SPRITE' | 'sprite' ;

from_expr : 'from' ;

isa_expr : 'is' 'a' ;

var_decl : ID ;

res_var_decl : ID ;

file_var_decl : ID ;


number_sign : NumberSign ;

number: DecimalDigit ('.' DecimalDigit)* ;
integer : DecimalDigit ;

X : 'X' | 'x' ;
Y : 'Y' | 'y' ;
Z : 'Z' | 'z' ;

NumberSign : [+-] ;
DecimalDigit : [0-9]+ ;
ID : [a-zA-Z_$][a-zA-Z_$0-9]* ;

// : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
WS : [ \r\n\t] + -> channel (HIDDEN) ;
