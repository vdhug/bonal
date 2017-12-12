//PACKAGE BR.COM.EMPRESA.BONAL.ENTIDADES;
//
//IMPORT JAVA.UTIL.CALENDAR;
//
//IMPORT JAVAX.PERSISTENCE.CASCADETYPE;
//IMPORT JAVAX.PERSISTENCE.COLUMN;
//IMPORT JAVAX.PERSISTENCE.DISCRIMINATORCOLUMN;
//IMPORT JAVAX.PERSISTENCE.ENTITY;
//IMPORT JAVAX.PERSISTENCE.GENERATEDVALUE;
//IMPORT JAVAX.PERSISTENCE.GENERATIONTYPE;
//IMPORT JAVAX.PERSISTENCE.ID;
//IMPORT JAVAX.PERSISTENCE.INHERITANCE;
//IMPORT JAVAX.PERSISTENCE.INHERITANCETYPE;
//IMPORT JAVAX.PERSISTENCE.JOINCOLUMN;
//IMPORT JAVAX.PERSISTENCE.MANYTOONE;
//IMPORT JAVAX.PERSISTENCE.TEMPORAL;
//IMPORT JAVAX.PERSISTENCE.TEMPORALTYPE;
//IMPORT JAVAX.PERSISTENCE.VERSION;
//
//@ENTITY
//@INHERITANCE(STRATEGY = INHERITANCETYPE.JOINED)
//@DISCRIMINATORCOLUMN(NAME = "TIPO")
//PUBLIC CLASS PESSOA {
//
//	@ID
//	@GENERATEDVALUE(STRATEGY = GENERATIONTYPE.IDENTITY)
//	PRIVATE LONG ID;
//
//	PRIVATE STRING NOME;
//	PRIVATE STRING EMAIL;
//	PRIVATE STRING TELEFONE;
//
//	/*
//	 * VERSION E DATAREGISTRO DEVEM ESTAR EM TODOS AS ENTIDADES QUE POSSUEM
//	 * REPOSIT�RIO
//	 */
//	@VERSION
//	PRIVATE INTEGER VERSION;
//
//	@TEMPORAL(TEMPORALTYPE.DATE)
//	@COLUMN(NAME = "DATA_REGISTRO")
//	PRIVATE CALENDAR DATAREGISTRO = CALENDAR.GETINSTANCE();
//
//	// ---------- INICIO RELACIONAMENTOS ----------
//	@MANYTOONE(CASCADE = CASCADETYPE.MERGE)
//	@JOINCOLUMN(NAME = "ENDERECO_ID")
//	PRIVATE ENDERECO ENDERECO;
//
//	// @ONETOONE(MAPPEDBY = "PESSOA")
//	// PRIVATE FUNCIONARIO FUNCIONARIO;
//	// ---------- FIM RELACIONAMENTOS ----------
//
//	PUBLIC LONG GETID() {
//		RETURN ID;
//	}
//
//	PUBLIC STRING GETNOME() {
//		RETURN NOME;
//	}
//
//	PUBLIC VOID SETNOME(STRING NOME) {
//		THIS.NOME = NOME;
//	}
//
//	PUBLIC STRING GETEMAIL() {
//		RETURN EMAIL;
//	}
//
//	PUBLIC VOID SETEMAIL(STRING EMAIL) {
//		THIS.EMAIL = EMAIL;
//	}
//
//	PUBLIC STRING GETTELEFONE() {
//		RETURN TELEFONE;
//	}
//
//	PUBLIC VOID SETTELEFONE(STRING TELEFONE) {
//		THIS.TELEFONE = TELEFONE;
//	}
//
//	PUBLIC ENDERECO GETENDERECO() {
//		RETURN ENDERECO;
//	}
//
//	PUBLIC VOID SETENDERECO(ENDERECO ENDERECO) {
//		THIS.ENDERECO = ENDERECO;
//	}
//
//	PUBLIC INTEGER GETVERSION() {
//		RETURN VERSION;
//	}
//
//	PUBLIC CALENDAR GETDATAREGISTRO() {
//		RETURN DATAREGISTRO;
//	}
//
//	@OVERRIDE
//	PUBLIC STRING TOSTRING() {
//		STRINGBUILDER BUILDER = NEW STRINGBUILDER();
//		BUILDER.APPEND(GETCLASS().GETSIMPLENAME() + " {");
//		BUILDER.APPEND("\N\TID= " + GETID());
//		BUILDER.APPEND("\N\TNOME= " + GETNOME());
//		BUILDER.APPEND("\N\TEMAIL= " + GETEMAIL());
//		BUILDER.APPEND("\N\TTELEFONE= " + GETTELEFONE());
//		BUILDER.APPEND("\N\TDATAREGISTRO= " + GETDATAREGISTRO());
//		BUILDER.APPEND("\N\TVERSION= " + GETVERSION());
//		BUILDER.APPEND("\N }");
//		RETURN BUILDER.TOSTRING();
//	}
//
//	// FALTA HASHCODE
//
//}