package br.lncc.sinapad.cli.utils;

/**
 * This interface contains the result codes expected by the cli operations.
 * 
 * @author egaldino
 *
 */
public interface CLIResultCodes {

	/**
	 * Result code when the request was successfully done.
	 */
	int OK = 200;
	/**
	 * Result code when do not exist data to return.
	 */
	int NO_CONTENT = 204;
	/**
	 * Result code when some data inputed by the user is not valid.
	 */
	int USER_ERROR = 400;
	/**
	 * Result code when the file already exists in the chosen directory.
	 */
	int FILE_ALREADY_EXISTS = 491;
	/**
	 * Result code when the file does not exists.
	 */
	int FILE_DOES_NOT_EXIST = 492;
	/**
	 * Result code when the job does not exists.
	 */
	int JOB_DOES_NOT_EXIST = 493;
	/**
	 * Result code when the job does not exists.
	 */
	int JOB_CANNOT_BE_CANCELLED = 494;
	/**
	 * Result code when some internal server error occur.
	 */
	int INTERNAL_SERVER_ERROR = 500;
	/**
	 * Result code when the user is not valid.
	 */
	int USER_NOT_AUTHORIZED = 591;
	/**
	 * Result code when the inputed project was not found.
	 */
	int PROJECT_NOT_FOUND = 592;
}
