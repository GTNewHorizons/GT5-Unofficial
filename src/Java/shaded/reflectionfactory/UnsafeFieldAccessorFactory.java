/*
 * Copyright (c) 2001, 2011, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package shaded.reflectionfactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class UnsafeFieldAccessorFactory {
	static FieldAccessor newFieldAccessor(Field field, boolean override) {
		Class<?> type = field.getType();
		boolean isStatic = Modifier.isStatic(field.getModifiers());
		boolean isFinal = Modifier.isFinal(field.getModifiers());
		boolean isVolatile = Modifier.isVolatile(field.getModifiers());
		boolean isQualified = isFinal || isVolatile;
		boolean isReadOnly = isFinal && (isStatic || !override);
		if (isStatic) {
			// This code path does not guarantee that the field's
			// declaring class has been initialized, but it must be
			// before performing reflective operations.
			UnsafeFieldAccessorImpl.unsafe.ensureClassInitialized(field.getDeclaringClass());

			if (!isQualified) {
				if (type == Boolean.TYPE) {
					return new UnsafeStaticBooleanFieldAccessorImpl(field);
				} else if (type == Byte.TYPE) {
					return new UnsafeStaticByteFieldAccessorImpl(field);
				} else if (type == Short.TYPE) {
					return new UnsafeStaticShortFieldAccessorImpl(field);
				} else if (type == Character.TYPE) {
					return new UnsafeStaticCharacterFieldAccessorImpl(field);
				} else if (type == Integer.TYPE) {
					return new UnsafeStaticIntegerFieldAccessorImpl(field);
				} else if (type == Long.TYPE) {
					return new UnsafeStaticLongFieldAccessorImpl(field);
				} else if (type == Float.TYPE) {
					return new UnsafeStaticFloatFieldAccessorImpl(field);
				} else if (type == Double.TYPE) {
					return new UnsafeStaticDoubleFieldAccessorImpl(field);
				} else {
					return new UnsafeStaticObjectFieldAccessorImpl(field);
				}
			} else {
				if (type == Boolean.TYPE) {
					return new UnsafeQualifiedStaticBooleanFieldAccessorImpl(field, isReadOnly);
				} else if (type == Byte.TYPE) {
					return new UnsafeQualifiedStaticByteFieldAccessorImpl(field, isReadOnly);
				} else if (type == Short.TYPE) {
					return new UnsafeQualifiedStaticShortFieldAccessorImpl(field, isReadOnly);
				} else if (type == Character.TYPE) {
					return new UnsafeQualifiedStaticCharacterFieldAccessorImpl(field, isReadOnly);
				} else if (type == Integer.TYPE) {
					return new UnsafeQualifiedStaticIntegerFieldAccessorImpl(field, isReadOnly);
				} else if (type == Long.TYPE) {
					return new UnsafeQualifiedStaticLongFieldAccessorImpl(field, isReadOnly);
				} else if (type == Float.TYPE) {
					return new UnsafeQualifiedStaticFloatFieldAccessorImpl(field, isReadOnly);
				} else if (type == Double.TYPE) {
					return new UnsafeQualifiedStaticDoubleFieldAccessorImpl(field, isReadOnly);
				} else {
					return new UnsafeQualifiedStaticObjectFieldAccessorImpl(field, isReadOnly);
				}
			}
		} else {
			if (!isQualified) {
				if (type == Boolean.TYPE) {
					return new UnsafeBooleanFieldAccessorImpl(field);
				} else if (type == Byte.TYPE) {
					return new UnsafeByteFieldAccessorImpl(field);
				} else if (type == Short.TYPE) {
					return new UnsafeShortFieldAccessorImpl(field);
				} else if (type == Character.TYPE) {
					return new UnsafeCharacterFieldAccessorImpl(field);
				} else if (type == Integer.TYPE) {
					return new UnsafeIntegerFieldAccessorImpl(field);
				} else if (type == Long.TYPE) {
					return new UnsafeLongFieldAccessorImpl(field);
				} else if (type == Float.TYPE) {
					return new UnsafeFloatFieldAccessorImpl(field);
				} else if (type == Double.TYPE) {
					return new UnsafeDoubleFieldAccessorImpl(field);
				} else {
					return new UnsafeObjectFieldAccessorImpl(field);
				}
			} else {
				if (type == Boolean.TYPE) {
					return new UnsafeQualifiedBooleanFieldAccessorImpl(field, isReadOnly);
				} else if (type == Byte.TYPE) {
					return new UnsafeQualifiedByteFieldAccessorImpl(field, isReadOnly);
				} else if (type == Short.TYPE) {
					return new UnsafeQualifiedShortFieldAccessorImpl(field, isReadOnly);
				} else if (type == Character.TYPE) {
					return new UnsafeQualifiedCharacterFieldAccessorImpl(field, isReadOnly);
				} else if (type == Integer.TYPE) {
					return new UnsafeQualifiedIntegerFieldAccessorImpl(field, isReadOnly);
				} else if (type == Long.TYPE) {
					return new UnsafeQualifiedLongFieldAccessorImpl(field, isReadOnly);
				} else if (type == Float.TYPE) {
					return new UnsafeQualifiedFloatFieldAccessorImpl(field, isReadOnly);
				} else if (type == Double.TYPE) {
					return new UnsafeQualifiedDoubleFieldAccessorImpl(field, isReadOnly);
				} else {
					return new UnsafeQualifiedObjectFieldAccessorImpl(field, isReadOnly);
				}
			}
		}
	}

	public static abstract class UnsafeStaticFieldAccessorImpl extends UnsafeFieldAccessorImpl {
		static {
			Reflection.registerFieldsToFilter(UnsafeStaticFieldAccessorImpl.class,
					new String[] { "base" });
		}

		protected final Object base; // base

		UnsafeStaticFieldAccessorImpl(Field field) {
			super(field);
			base = unsafe.staticFieldBase(field);
		}
	}


	public static class UnsafeStaticBooleanFieldAccessorImpl extends UnsafeStaticFieldAccessorImpl {
		UnsafeStaticBooleanFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Boolean(getBoolean(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			return unsafe.getBoolean(base, fieldOffset);
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			throw newGetLongIllegalArgumentException();
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			throw newGetFloatIllegalArgumentException();
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			throw newGetDoubleIllegalArgumentException();
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Boolean) {
				unsafe.putBoolean(base, fieldOffset, ((Boolean) value).booleanValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isFinal) {
				throwFinalFieldIllegalAccessException(z);
			}
			unsafe.putBoolean(base, fieldOffset, z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeStaticByteFieldAccessorImpl extends UnsafeStaticFieldAccessorImpl {
		UnsafeStaticByteFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Byte(getByte(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			return unsafe.getByte(base, fieldOffset);
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putByte(base, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isFinal) {
				throwFinalFieldIllegalAccessException(b);
			}
			unsafe.putByte(base, fieldOffset, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeStaticCharacterFieldAccessorImpl extends UnsafeStaticFieldAccessorImpl {
		UnsafeStaticCharacterFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Character(getChar(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			return unsafe.getChar(base, fieldOffset);
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			return getChar(obj);
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return getChar(obj);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getChar(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getChar(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Character) {
				unsafe.putChar(base, fieldOffset, ((Character) value).charValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isFinal) {
				throwFinalFieldIllegalAccessException(c);
			}
			unsafe.putChar(base, fieldOffset, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeStaticDoubleFieldAccessorImpl extends UnsafeStaticFieldAccessorImpl {
		UnsafeStaticDoubleFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Double(getDouble(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			throw newGetLongIllegalArgumentException();
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			throw newGetFloatIllegalArgumentException();
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return unsafe.getDouble(base, fieldOffset);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putDouble(base, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putDouble(base, fieldOffset, ((Short) value).shortValue());
				return;
			}
			if (value instanceof Character) {
				unsafe.putDouble(base, fieldOffset, ((Character) value).charValue());
				return;
			}
			if (value instanceof Integer) {
				unsafe.putDouble(base, fieldOffset, ((Integer) value).intValue());
				return;
			}
			if (value instanceof Long) {
				unsafe.putDouble(base, fieldOffset, ((Long) value).longValue());
				return;
			}
			if (value instanceof Float) {
				unsafe.putDouble(base, fieldOffset, ((Float) value).floatValue());
				return;
			}
			if (value instanceof Double) {
				unsafe.putDouble(base, fieldOffset, ((Double) value).doubleValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isFinal) {
				throwFinalFieldIllegalAccessException(d);
			}
			unsafe.putDouble(base, fieldOffset, d);
		}
	}

	public static class UnsafeStaticFloatFieldAccessorImpl extends UnsafeStaticFieldAccessorImpl {
		UnsafeStaticFloatFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Float(getFloat(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			throw newGetLongIllegalArgumentException();
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return unsafe.getFloat(base, fieldOffset);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getFloat(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putFloat(base, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putFloat(base, fieldOffset, ((Short) value).shortValue());
				return;
			}
			if (value instanceof Character) {
				unsafe.putFloat(base, fieldOffset, ((Character) value).charValue());
				return;
			}
			if (value instanceof Integer) {
				unsafe.putFloat(base, fieldOffset, ((Integer) value).intValue());
				return;
			}
			if (value instanceof Long) {
				unsafe.putFloat(base, fieldOffset, ((Long) value).longValue());
				return;
			}
			if (value instanceof Float) {
				unsafe.putFloat(base, fieldOffset, ((Float) value).floatValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isFinal) {
				throwFinalFieldIllegalAccessException(f);
			}
			unsafe.putFloat(base, fieldOffset, f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeStaticIntegerFieldAccessorImpl extends UnsafeStaticFieldAccessorImpl {
		UnsafeStaticIntegerFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Integer(getInt(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			return unsafe.getInt(base, fieldOffset);
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return getInt(obj);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getInt(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getInt(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putInt(base, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putInt(base, fieldOffset, ((Short) value).shortValue());
				return;
			}
			if (value instanceof Character) {
				unsafe.putInt(base, fieldOffset, ((Character) value).charValue());
				return;
			}
			if (value instanceof Integer) {
				unsafe.putInt(base, fieldOffset, ((Integer) value).intValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setInt(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			setInt(obj, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			setInt(obj, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isFinal) {
				throwFinalFieldIllegalAccessException(i);
			}
			unsafe.putInt(base, fieldOffset, i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeStaticLongFieldAccessorImpl extends UnsafeStaticFieldAccessorImpl {
		UnsafeStaticLongFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Long(getLong(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return unsafe.getLong(base, fieldOffset);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getLong(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getLong(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putLong(base, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putLong(base, fieldOffset, ((Short) value).shortValue());
				return;
			}
			if (value instanceof Character) {
				unsafe.putLong(base, fieldOffset, ((Character) value).charValue());
				return;
			}
			if (value instanceof Integer) {
				unsafe.putLong(base, fieldOffset, ((Integer) value).intValue());
				return;
			}
			if (value instanceof Long) {
				unsafe.putLong(base, fieldOffset, ((Long) value).longValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setLong(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			setLong(obj, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			setLong(obj, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			setLong(obj, i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isFinal) {
				throwFinalFieldIllegalAccessException(l);
			}
			unsafe.putLong(base, fieldOffset, l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeStaticObjectFieldAccessorImpl extends UnsafeStaticFieldAccessorImpl {
		UnsafeStaticObjectFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return unsafe.getObject(base, fieldOffset);
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			throw newGetLongIllegalArgumentException();
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			throw newGetFloatIllegalArgumentException();
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			throw newGetDoubleIllegalArgumentException();
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value != null) {
				if (!field.getType().isAssignableFrom(value.getClass())) {
					throwSetIllegalArgumentException(value);
				}
			}
			unsafe.putObject(base, fieldOffset, value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeStaticShortFieldAccessorImpl extends UnsafeStaticFieldAccessorImpl {
		UnsafeStaticShortFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Short(getShort(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			return unsafe.getShort(base, fieldOffset);
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			return getShort(obj);
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return getShort(obj);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getShort(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getShort(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putShort(base, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putShort(base, fieldOffset, ((Short) value).shortValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setShort(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isFinal) {
				throwFinalFieldIllegalAccessException(s);
			}
			unsafe.putShort(base, fieldOffset, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeQualifiedBooleanFieldAccessorImpl
	extends UnsafeQualifiedFieldAccessorImpl
	{
		UnsafeQualifiedBooleanFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Boolean(getBoolean(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getBooleanVolatile(obj, fieldOffset);
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			throw newGetLongIllegalArgumentException();
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			throw newGetFloatIllegalArgumentException();
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			throw newGetDoubleIllegalArgumentException();
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Boolean) {
				unsafe.putBooleanVolatile(obj, fieldOffset, ((Boolean) value).booleanValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(z);
			}
			unsafe.putBooleanVolatile(obj, fieldOffset, z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeQualifiedByteFieldAccessorImpl
	extends UnsafeQualifiedFieldAccessorImpl
	{
		UnsafeQualifiedByteFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Byte(getByte(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getByteVolatile(obj, fieldOffset);
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putByteVolatile(obj, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(b);
			}
			unsafe.putByteVolatile(obj, fieldOffset, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeQualifiedCharacterFieldAccessorImpl
	extends UnsafeQualifiedFieldAccessorImpl
	{
		UnsafeQualifiedCharacterFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Character(getChar(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getCharVolatile(obj, fieldOffset);
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			return getChar(obj);
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return getChar(obj);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getChar(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getChar(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Character) {
				unsafe.putCharVolatile(obj, fieldOffset, ((Character) value).charValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(c);
			}
			unsafe.putCharVolatile(obj, fieldOffset, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeQualifiedDoubleFieldAccessorImpl
	extends UnsafeQualifiedFieldAccessorImpl
	{
		UnsafeQualifiedDoubleFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Double(getDouble(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			throw newGetLongIllegalArgumentException();
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			throw newGetFloatIllegalArgumentException();
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getDoubleVolatile(obj, fieldOffset);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putDoubleVolatile(obj, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putDoubleVolatile(obj, fieldOffset, ((Short) value).shortValue());
				return;
			}
			if (value instanceof Character) {
				unsafe.putDoubleVolatile(obj, fieldOffset, ((Character) value).charValue());
				return;
			}
			if (value instanceof Integer) {
				unsafe.putDoubleVolatile(obj, fieldOffset, ((Integer) value).intValue());
				return;
			}
			if (value instanceof Long) {
				unsafe.putDoubleVolatile(obj, fieldOffset, ((Long) value).longValue());
				return;
			}
			if (value instanceof Float) {
				unsafe.putDoubleVolatile(obj, fieldOffset, ((Float) value).floatValue());
				return;
			}
			if (value instanceof Double) {
				unsafe.putDoubleVolatile(obj, fieldOffset, ((Double) value).doubleValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(d);
			}
			unsafe.putDoubleVolatile(obj, fieldOffset, d);
		}
	}

	public static class UnsafeQualifiedFloatFieldAccessorImpl
	extends UnsafeQualifiedFieldAccessorImpl
	{
		UnsafeQualifiedFloatFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Float(getFloat(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			throw newGetLongIllegalArgumentException();
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getFloatVolatile(obj, fieldOffset);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getFloat(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putFloatVolatile(obj, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putFloatVolatile(obj, fieldOffset, ((Short) value).shortValue());
				return;
			}
			if (value instanceof Character) {
				unsafe.putFloatVolatile(obj, fieldOffset, ((Character) value).charValue());
				return;
			}
			if (value instanceof Integer) {
				unsafe.putFloatVolatile(obj, fieldOffset, ((Integer) value).intValue());
				return;
			}
			if (value instanceof Long) {
				unsafe.putFloatVolatile(obj, fieldOffset, ((Long) value).longValue());
				return;
			}
			if (value instanceof Float) {
				unsafe.putFloatVolatile(obj, fieldOffset, ((Float) value).floatValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(f);
			}
			unsafe.putFloatVolatile(obj, fieldOffset, f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeQualifiedIntegerFieldAccessorImpl
	extends UnsafeQualifiedFieldAccessorImpl
	{
		UnsafeQualifiedIntegerFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Integer(getInt(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getIntVolatile(obj, fieldOffset);
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return getInt(obj);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getInt(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getInt(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putIntVolatile(obj, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putIntVolatile(obj, fieldOffset, ((Short) value).shortValue());
				return;
			}
			if (value instanceof Character) {
				unsafe.putIntVolatile(obj, fieldOffset, ((Character) value).charValue());
				return;
			}
			if (value instanceof Integer) {
				unsafe.putIntVolatile(obj, fieldOffset, ((Integer) value).intValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setInt(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			setInt(obj, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			setInt(obj, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(i);
			}
			unsafe.putIntVolatile(obj, fieldOffset, i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeQualifiedLongFieldAccessorImpl
	extends UnsafeQualifiedFieldAccessorImpl
	{
		UnsafeQualifiedLongFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Long(getLong(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getLongVolatile(obj, fieldOffset);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getLong(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getLong(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putLongVolatile(obj, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putLongVolatile(obj, fieldOffset, ((Short) value).shortValue());
				return;
			}
			if (value instanceof Character) {
				unsafe.putLongVolatile(obj, fieldOffset, ((Character) value).charValue());
				return;
			}
			if (value instanceof Integer) {
				unsafe.putLongVolatile(obj, fieldOffset, ((Integer) value).intValue());
				return;
			}
			if (value instanceof Long) {
				unsafe.putLongVolatile(obj, fieldOffset, ((Long) value).longValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setLong(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			setLong(obj, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			setLong(obj, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			setLong(obj, i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(l);
			}
			unsafe.putLongVolatile(obj, fieldOffset, l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeQualifiedObjectFieldAccessorImpl
	extends UnsafeQualifiedFieldAccessorImpl
	{
		UnsafeQualifiedObjectFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getObjectVolatile(obj, fieldOffset);
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			throw newGetLongIllegalArgumentException();
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			throw newGetFloatIllegalArgumentException();
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			throw newGetDoubleIllegalArgumentException();
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value != null) {
				if (!field.getType().isAssignableFrom(value.getClass())) {
					throwSetIllegalArgumentException(value);
				}
			}
			unsafe.putObjectVolatile(obj, fieldOffset, value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeQualifiedShortFieldAccessorImpl
	extends UnsafeQualifiedFieldAccessorImpl
	{
		UnsafeQualifiedShortFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Short(getShort(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getShortVolatile(obj, fieldOffset);
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			return getShort(obj);
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return getShort(obj);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getShort(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getShort(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putShortVolatile(obj, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putShortVolatile(obj, fieldOffset, ((Short) value).shortValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setShort(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(s);
			}
			unsafe.putShortVolatile(obj, fieldOffset, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeBooleanFieldAccessorImpl extends UnsafeFieldAccessorImpl {
		UnsafeBooleanFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Boolean(getBoolean(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getBoolean(obj, fieldOffset);
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			throw newGetLongIllegalArgumentException();
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			throw newGetFloatIllegalArgumentException();
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			throw newGetDoubleIllegalArgumentException();
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Boolean) {
				unsafe.putBoolean(obj, fieldOffset, ((Boolean) value).booleanValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isFinal) {
				throwFinalFieldIllegalAccessException(z);
			}
			unsafe.putBoolean(obj, fieldOffset, z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeByteFieldAccessorImpl extends UnsafeFieldAccessorImpl {
		UnsafeByteFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Byte(getByte(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getByte(obj, fieldOffset);
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putByte(obj, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isFinal) {
				throwFinalFieldIllegalAccessException(b);
			}
			unsafe.putByte(obj, fieldOffset, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeCharacterFieldAccessorImpl extends UnsafeFieldAccessorImpl {
		UnsafeCharacterFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Character(getChar(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getChar(obj, fieldOffset);
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			return getChar(obj);
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return getChar(obj);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getChar(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getChar(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Character) {
				unsafe.putChar(obj, fieldOffset, ((Character) value).charValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isFinal) {
				throwFinalFieldIllegalAccessException(c);
			}
			unsafe.putChar(obj, fieldOffset, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeDoubleFieldAccessorImpl extends UnsafeFieldAccessorImpl {
		UnsafeDoubleFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Double(getDouble(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			throw newGetLongIllegalArgumentException();
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			throw newGetFloatIllegalArgumentException();
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getDouble(obj, fieldOffset);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putDouble(obj, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putDouble(obj, fieldOffset, ((Short) value).shortValue());
				return;
			}
			if (value instanceof Character) {
				unsafe.putDouble(obj, fieldOffset, ((Character) value).charValue());
				return;
			}
			if (value instanceof Integer) {
				unsafe.putDouble(obj, fieldOffset, ((Integer) value).intValue());
				return;
			}
			if (value instanceof Long) {
				unsafe.putDouble(obj, fieldOffset, ((Long) value).longValue());
				return;
			}
			if (value instanceof Float) {
				unsafe.putDouble(obj, fieldOffset, ((Float) value).floatValue());
				return;
			}
			if (value instanceof Double) {
				unsafe.putDouble(obj, fieldOffset, ((Double) value).doubleValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isFinal) {
				throwFinalFieldIllegalAccessException(d);
			}
			unsafe.putDouble(obj, fieldOffset, d);
		}
	}

	public static class UnsafeFloatFieldAccessorImpl extends UnsafeFieldAccessorImpl {
		UnsafeFloatFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Float(getFloat(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			throw newGetLongIllegalArgumentException();
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getFloat(obj, fieldOffset);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getFloat(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putFloat(obj, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putFloat(obj, fieldOffset, ((Short) value).shortValue());
				return;
			}
			if (value instanceof Character) {
				unsafe.putFloat(obj, fieldOffset, ((Character) value).charValue());
				return;
			}
			if (value instanceof Integer) {
				unsafe.putFloat(obj, fieldOffset, ((Integer) value).intValue());
				return;
			}
			if (value instanceof Long) {
				unsafe.putFloat(obj, fieldOffset, ((Long) value).longValue());
				return;
			}
			if (value instanceof Float) {
				unsafe.putFloat(obj, fieldOffset, ((Float) value).floatValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isFinal) {
				throwFinalFieldIllegalAccessException(f);
			}
			unsafe.putFloat(obj, fieldOffset, f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}



	public static class UnsafeIntegerFieldAccessorImpl extends UnsafeFieldAccessorImpl {
		UnsafeIntegerFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Integer(getInt(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getInt(obj, fieldOffset);
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return getInt(obj);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getInt(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getInt(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putInt(obj, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putInt(obj, fieldOffset, ((Short) value).shortValue());
				return;
			}
			if (value instanceof Character) {
				unsafe.putInt(obj, fieldOffset, ((Character) value).charValue());
				return;
			}
			if (value instanceof Integer) {
				unsafe.putInt(obj, fieldOffset, ((Integer) value).intValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setInt(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			setInt(obj, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			setInt(obj, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isFinal) {
				throwFinalFieldIllegalAccessException(i);
			}
			unsafe.putInt(obj, fieldOffset, i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeLongFieldAccessorImpl extends UnsafeFieldAccessorImpl {
		UnsafeLongFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Long(getLong(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getLong(obj, fieldOffset);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getLong(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getLong(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putLong(obj, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putLong(obj, fieldOffset, ((Short) value).shortValue());
				return;
			}
			if (value instanceof Character) {
				unsafe.putLong(obj, fieldOffset, ((Character) value).charValue());
				return;
			}
			if (value instanceof Integer) {
				unsafe.putLong(obj, fieldOffset, ((Integer) value).intValue());
				return;
			}
			if (value instanceof Long) {
				unsafe.putLong(obj, fieldOffset, ((Long) value).longValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setLong(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			setLong(obj, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			setLong(obj, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			setLong(obj, i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isFinal) {
				throwFinalFieldIllegalAccessException(l);
			}
			unsafe.putLong(obj, fieldOffset, l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeObjectFieldAccessorImpl extends UnsafeFieldAccessorImpl {
		UnsafeObjectFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getObject(obj, fieldOffset);
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			throw newGetLongIllegalArgumentException();
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			throw newGetFloatIllegalArgumentException();
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			throw newGetDoubleIllegalArgumentException();
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value != null) {
				if (!field.getType().isAssignableFrom(value.getClass())) {
					throwSetIllegalArgumentException(value);
				}
			}
			unsafe.putObject(obj, fieldOffset, value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeShortFieldAccessorImpl extends UnsafeFieldAccessorImpl {
		UnsafeShortFieldAccessorImpl(Field field) {
			super(field);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Short(getShort(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			ensureObj(obj);
			return unsafe.getShort(obj, fieldOffset);
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			return getShort(obj);
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return getShort(obj);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getShort(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getShort(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isFinal) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putShort(obj, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putShort(obj, fieldOffset, ((Short) value).shortValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setShort(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			ensureObj(obj);
			if (isFinal) {
				throwFinalFieldIllegalAccessException(s);
			}
			unsafe.putShort(obj, fieldOffset, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}


	public static class UnsafeQualifiedStaticBooleanFieldAccessorImpl
	extends UnsafeQualifiedStaticFieldAccessorImpl
	{
		UnsafeQualifiedStaticBooleanFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Boolean(getBoolean(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			return unsafe.getBooleanVolatile(base, fieldOffset);
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			throw newGetLongIllegalArgumentException();
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			throw newGetFloatIllegalArgumentException();
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			throw newGetDoubleIllegalArgumentException();
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Boolean) {
				unsafe.putBooleanVolatile(base, fieldOffset, ((Boolean) value).booleanValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(z);
			}
			unsafe.putBooleanVolatile(base, fieldOffset, z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeQualifiedStaticByteFieldAccessorImpl
	extends UnsafeQualifiedStaticFieldAccessorImpl
	{
		UnsafeQualifiedStaticByteFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Byte(getByte(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			return unsafe.getByteVolatile(base, fieldOffset);
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getByte(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putByteVolatile(base, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(b);
			}
			unsafe.putByteVolatile(base, fieldOffset, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeQualifiedStaticCharacterFieldAccessorImpl
	extends UnsafeQualifiedStaticFieldAccessorImpl
	{
		UnsafeQualifiedStaticCharacterFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Character(getChar(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			return unsafe.getCharVolatile(base, fieldOffset);
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			return getChar(obj);
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return getChar(obj);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getChar(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getChar(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Character) {
				unsafe.putCharVolatile(base, fieldOffset, ((Character) value).charValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(c);
			}
			unsafe.putCharVolatile(base, fieldOffset, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeQualifiedStaticDoubleFieldAccessorImpl
	extends UnsafeQualifiedStaticFieldAccessorImpl
	{
		UnsafeQualifiedStaticDoubleFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Double(getDouble(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			throw newGetLongIllegalArgumentException();
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			throw newGetFloatIllegalArgumentException();
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return unsafe.getDoubleVolatile(base, fieldOffset);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putDoubleVolatile(base, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putDoubleVolatile(base, fieldOffset, ((Short) value).shortValue());
				return;
			}
			if (value instanceof Character) {
				unsafe.putDoubleVolatile(base, fieldOffset, ((Character) value).charValue());
				return;
			}
			if (value instanceof Integer) {
				unsafe.putDoubleVolatile(base, fieldOffset, ((Integer) value).intValue());
				return;
			}
			if (value instanceof Long) {
				unsafe.putDoubleVolatile(base, fieldOffset, ((Long) value).longValue());
				return;
			}
			if (value instanceof Float) {
				unsafe.putDoubleVolatile(base, fieldOffset, ((Float) value).floatValue());
				return;
			}
			if (value instanceof Double) {
				unsafe.putDoubleVolatile(base, fieldOffset, ((Double) value).doubleValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			setDouble(obj, f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(d);
			}
			unsafe.putDoubleVolatile(base, fieldOffset, d);
		}
	}

	public static class UnsafeQualifiedStaticFloatFieldAccessorImpl
	extends UnsafeQualifiedStaticFieldAccessorImpl
	{
		UnsafeQualifiedStaticFloatFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Float(getFloat(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			throw newGetLongIllegalArgumentException();
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return unsafe.getFloatVolatile(base, fieldOffset);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getFloat(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putFloatVolatile(base, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putFloatVolatile(base, fieldOffset, ((Short) value).shortValue());
				return;
			}
			if (value instanceof Character) {
				unsafe.putFloatVolatile(base, fieldOffset, ((Character) value).charValue());
				return;
			}
			if (value instanceof Integer) {
				unsafe.putFloatVolatile(base, fieldOffset, ((Integer) value).intValue());
				return;
			}
			if (value instanceof Long) {
				unsafe.putFloatVolatile(base, fieldOffset, ((Long) value).longValue());
				return;
			}
			if (value instanceof Float) {
				unsafe.putFloatVolatile(base, fieldOffset, ((Float) value).floatValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			setFloat(obj, l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(f);
			}
			unsafe.putFloatVolatile(base, fieldOffset, f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeQualifiedStaticIntegerFieldAccessorImpl
	extends UnsafeQualifiedStaticFieldAccessorImpl
	{
		UnsafeQualifiedStaticIntegerFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Integer(getInt(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			return unsafe.getIntVolatile(base, fieldOffset);
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return getInt(obj);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getInt(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getInt(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putIntVolatile(base, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putIntVolatile(base, fieldOffset, ((Short) value).shortValue());
				return;
			}
			if (value instanceof Character) {
				unsafe.putIntVolatile(base, fieldOffset, ((Character) value).charValue());
				return;
			}
			if (value instanceof Integer) {
				unsafe.putIntVolatile(base, fieldOffset, ((Integer) value).intValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setInt(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			setInt(obj, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			setInt(obj, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(i);
			}
			unsafe.putIntVolatile(base, fieldOffset, i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeQualifiedStaticLongFieldAccessorImpl
	extends UnsafeQualifiedStaticFieldAccessorImpl
	{
		UnsafeQualifiedStaticLongFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Long(getLong(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return unsafe.getLongVolatile(base, fieldOffset);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getLong(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getLong(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putLongVolatile(base, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putLongVolatile(base, fieldOffset, ((Short) value).shortValue());
				return;
			}
			if (value instanceof Character) {
				unsafe.putLongVolatile(base, fieldOffset, ((Character) value).charValue());
				return;
			}
			if (value instanceof Integer) {
				unsafe.putLongVolatile(base, fieldOffset, ((Integer) value).intValue());
				return;
			}
			if (value instanceof Long) {
				unsafe.putLongVolatile(base, fieldOffset, ((Long) value).longValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setLong(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			setLong(obj, c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			setLong(obj, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			setLong(obj, i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(l);
			}
			unsafe.putLongVolatile(base, fieldOffset, l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeQualifiedStaticObjectFieldAccessorImpl
	extends UnsafeQualifiedStaticFieldAccessorImpl
	{
		UnsafeQualifiedStaticObjectFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return unsafe.getObjectVolatile(base, fieldOffset);
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			throw newGetShortIllegalArgumentException();
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			throw newGetIntIllegalArgumentException();
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			throw newGetLongIllegalArgumentException();
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			throw newGetFloatIllegalArgumentException();
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			throw newGetDoubleIllegalArgumentException();
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value != null) {
				if (!field.getType().isAssignableFrom(value.getClass())) {
					throwSetIllegalArgumentException(value);
				}
			}
			unsafe.putObjectVolatile(base, fieldOffset, value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}

	public static class UnsafeQualifiedStaticShortFieldAccessorImpl
	extends UnsafeQualifiedStaticFieldAccessorImpl
	{
		UnsafeQualifiedStaticShortFieldAccessorImpl(Field field, boolean isReadOnly) {
			super(field, isReadOnly);
		}

		public Object get(Object obj) throws IllegalArgumentException {
			return new Short(getShort(obj));
		}

		public boolean getBoolean(Object obj) throws IllegalArgumentException {
			throw newGetBooleanIllegalArgumentException();
		}

		public byte getByte(Object obj) throws IllegalArgumentException {
			throw newGetByteIllegalArgumentException();
		}

		public char getChar(Object obj) throws IllegalArgumentException {
			throw newGetCharIllegalArgumentException();
		}

		public short getShort(Object obj) throws IllegalArgumentException {
			return unsafe.getShortVolatile(base, fieldOffset);
		}

		public int getInt(Object obj) throws IllegalArgumentException {
			return getShort(obj);
		}

		public long getLong(Object obj) throws IllegalArgumentException {
			return getShort(obj);
		}

		public float getFloat(Object obj) throws IllegalArgumentException {
			return getShort(obj);
		}

		public double getDouble(Object obj) throws IllegalArgumentException {
			return getShort(obj);
		}

		public void set(Object obj, Object value)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(value);
			}
			if (value == null) {
				throwSetIllegalArgumentException(value);
			}
			if (value instanceof Byte) {
				unsafe.putShortVolatile(base, fieldOffset, ((Byte) value).byteValue());
				return;
			}
			if (value instanceof Short) {
				unsafe.putShortVolatile(base, fieldOffset, ((Short) value).shortValue());
				return;
			}
			throwSetIllegalArgumentException(value);
		}

		public void setBoolean(Object obj, boolean z)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(z);
		}

		public void setByte(Object obj, byte b)
				throws IllegalArgumentException, IllegalAccessException
		{
			setShort(obj, b);
		}

		public void setChar(Object obj, char c)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(c);
		}

		public void setShort(Object obj, short s)
				throws IllegalArgumentException, IllegalAccessException
		{
			if (isReadOnly) {
				throwFinalFieldIllegalAccessException(s);
			}
			unsafe.putShortVolatile(base, fieldOffset, s);
		}

		public void setInt(Object obj, int i)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(i);
		}

		public void setLong(Object obj, long l)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(l);
		}

		public void setFloat(Object obj, float f)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(f);
		}

		public void setDouble(Object obj, double d)
				throws IllegalArgumentException, IllegalAccessException
		{
			throwSetIllegalArgumentException(d);
		}
	}


}
