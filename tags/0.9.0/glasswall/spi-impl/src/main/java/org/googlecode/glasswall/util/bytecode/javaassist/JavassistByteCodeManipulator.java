package org.googlecode.glasswall.util.bytecode.javaassist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import org.googlecode.glasswall.util.bytecode.ByteCodeManipulator;

/**
 * 
 * @author chenjianjx
 * 
 */
public class JavassistByteCodeManipulator implements ByteCodeManipulator {

	private static JavassistByteCodeManipulator instance;

	private ClassPool classPool;

	private JavassistByteCodeManipulator() {
		classPool = ClassPool.getDefault();
	}

	public Class<?> addClass(String className, List<String> fieldDeclarations,
			List<String> methods) {
		try {

			CtClass ctClass = classPool.makeClass(className);

			for (String fd : fieldDeclarations) {
				CtField ctField = CtField.make(fd, ctClass);
				ctClass.addField(ctField);
			}

			for (String method : methods) {
				ctClass.addMethod(CtNewMethod.make(method, ctClass));
			}

			return ctClass.toClass();
		} catch (CannotCompileException e) {
			throw new RuntimeException(e);
		}

	}

	public static JavassistByteCodeManipulator getInstance(
			ClassLoader classLoader) {
		if (null == instance) {
			instance = new JavassistByteCodeManipulator();
		}

		if (classLoader != null) {
			instance.registerClassLoader(classLoader);
		}
		return instance;
	}

	private void registerClassLoader(ClassLoader classLoader) {
		classPool.insertClassPath(new LoaderClassPath(classLoader));

	}

	public byte[] insertAtMethodStart(String className, String methodName,
			List<String> argTypes, String newStatements) {
		try {
			CtClass clazz = getCtClass(className);
			CtMethod m = getCtMethod(methodName, argTypes, clazz);
			m.insertBefore(JavaassistVariableResolver
					.resolveToJavassistVariables(newStatements));
			return clazz.toBytecode();
		} catch (CannotCompileException e) {
			throw new IllegalStateException(e);
		} catch (NotFoundException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

	public byte[] insertAtMethodEnd(String className, String methodName,
			List<String> argTypes, String newStatements) {
		try {
			CtClass clazz = getCtClass(className);
			CtMethod m = getCtMethod(methodName, argTypes, clazz);
			m.insertAfter(JavaassistVariableResolver
					.resolveToJavassistVariables(newStatements));
			return clazz.toBytecode();
		} catch (CannotCompileException e) {
			throw new IllegalStateException(e);
		} catch (NotFoundException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

	private CtMethod getCtMethod(String methodName, List<String> argTypes,
			CtClass clazz) throws NotFoundException {
		List<CtClass> params = new ArrayList<CtClass>(argTypes.size());
		for (String argType : argTypes) {
			params.add(classPool.getCtClass(argType));
		}

		CtMethod m = clazz.getDeclaredMethod(methodName,
				(CtClass[]) params.toArray(new CtClass[params.size()]));
		return m;
	}

	private CtClass getCtClass(String className) throws NotFoundException {
		CtClass clazz = classPool.get(className);
		if (clazz.isFrozen()) {
			clazz.defrost();
		}
		return clazz;
	}

	public byte[] addMethods(String className, String... method) {

		try {
			CtClass ctClass = getCtClass(className);
			for (String m : method) {
				ctClass.addMethod(CtNewMethod.make(m, ctClass));
			}
			return ctClass.toBytecode();
		} catch (CannotCompileException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}

	}

	public byte[] addField(String className, String... fieldDeclaration) {

		try {
			CtClass ctClass = getCtClass(className);
			for (String f : fieldDeclaration) {
				CtField ctField = CtField.make(f, ctClass);
				ctClass.addField(ctField);
			}
			return ctClass.toBytecode();
		} catch (CannotCompileException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean hasField(String className, String fieldName) {
		CtClass ctClass = null;
		try {
			ctClass = getCtClass(className);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
		try {
			CtField cf = ctClass.getDeclaredField(fieldName);
			return cf != null;
		} catch (NotFoundException e) {
			return false;
		}

	}

	public boolean hasMethod(String className, String methodName,
			List<String> argTypes) {
		CtClass ctClass = null;
		try {
			ctClass = getCtClass(className);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}

		try {
			CtMethod cm = getCtMethod(methodName, argTypes, ctClass);
			return cm != null;
		} catch (NotFoundException e) {
			return false;
		}

	}

	public byte[] getClassAsBytes(String className) {

		try {
			CtClass ctClass = getCtClass(className);
			return ctClass.toBytecode();
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (CannotCompileException e) {
			throw new RuntimeException(e);
		}
	}

}
