package org.googlecode.glasswall.util.bytecode;

import java.util.List;

/**
 * 
 * @author chenjianjx
 * 
 */
public interface ByteCodeManipulator {

	/**
	 * add a class and put it in effect
	 * 
	 * @param className
	 * @param fieldDeclarations
	 * @param methods
	 * @return
	 */
	Class<?> addClass(String className, List<String> fieldDeclarations,
			List<String> methods);

	/**
	 * insert some statements at the BEGINNING of a method and then returns the
	 * new byte code in bytes
	 * 
	 * @return
	 */
	byte[] insertAtMethodStart(String className, String methodName,
			List<String> argTypes, String newStatements);

	/**
	 * insert some statements at the END of a method and then returns the new
	 * byte code in bytes
	 * 
	 * @param className
	 * @param methodName
	 * @param argTypes
	 * @param newStatements
	 * @return
	 */
	byte[] insertAtMethodEnd(String className, String methodName,
			List<String> argTypes, String newStatements);

	byte[] addMethods(String className, String... method);

	byte[] addField(String className, String... fieldDeclaration);

	boolean hasField(String className, String fieldName);

	boolean hasMethod(String className, String methodName, List<String> argTypes);

	byte[] getClassAsBytes(String className);

}
