package org.brijframework.util.objects;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.Vector;

import org.brijframework.util.asserts.AssertMessage;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.casting.CastingUtil;
import org.brijframework.util.reflect.ClassUtil;
import org.brijframework.util.reflect.FieldUtil;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.LogicUnit;
import org.brijframework.util.resouces.FileUtil;
import org.brijframework.util.support.Constants;
import org.brijframework.util.text.StringUtil;

public abstract class ObjectUtil {
	private static final Object[] emptyArray = new Object[0];
	private static final List<?> emptyList = new ArrayList<>();

	/**
	 * Swap the Object array
	 * 
	 * @param array
	 * @param index1
	 * @param index2
	 * @serialData tested
	 */
	public static void swapArray(Object[] array, int index1, int index2) {
		Object temp = array[index1];
		array[index1] = array[index2];
		array[index2] = temp;
	}

	/**
	 * Reverse the Object array;
	 * 
	 * @param array
	 * @serialData tested
	 */
	public static void reverseArray(Object[] array) {
		for (int i = array.length - 1, j = 0; i > array.length / 2; i--, j++) {
			swapArray(array, i, j);
		}
	}

	/**
	 * Sorting the Object array;
	 * 
	 * @param array
	 */
	public static void sortArray(Object[] array,boolean isDesc) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 1; j < array.length; j++) {
				swapArray(array, i, j);
			}
		}
	}

	/**
	 * Remove the all element that match the specified element from Object
	 * array.
	 * 
	 * @param array
	 *            Object array.
	 * @param property
	 *            and val will be match then it to be removed
	 * @return array after removing element.
	 */
	public static Object[] removeElement(Object[] array, String property, String val) {
		int length = array.length;
		for (int i = 0; i < length; i++)
			if (length < array.length) {
				Object array1[] = new Object[length];
				System.arraycopy(array, 0, array1, 0, length);
				return array1;
			}
		return array;
	}

	/**
	 * Remove the all element that match the specified element from Object
	 * array.
	 * 
	 * @param array
	 *            Object array.
	 * @param element
	 *            to be removed
	 * @return array after removing element.
	 */
	public static Object[] removeElement(Object[] array, Object element) {
		int length = array.length;
		for (int i = 0; i < length; i++)
			if (element == array[i]) {
				for (int j = i; j < length - 1; j++)
					array[j] = array[j + 1];
				length--;
			}
		if (length < array.length) {
			Object array1[] = new Object[length];
			System.arraycopy(array, 0, array1, 0, length);
			return array1;
		}
		return array;
	}

	/**
	 * Swap the Integer array value
	 * 
	 * @param array
	 * @param index1
	 * @param index2
	 * 
	 */
	public static void swapIntArray(int[] array, int index1, int index2) {
		array[index1] = array[index1] ^ array[index2];
		array[index2] = array[index1] ^ array[index2];
		array[index1] = array[index2] ^ array[index1];
	}

	/**
	 * Reverse the integer array;
	 * 
	 * @param array
	 */
	public static void reverseIntArray(int[] array) {
		for (int i = array.length - 1, j = 0; i > array.length / 2; i--, j++)
			swapIntArray(array, i, j);

	}

	/**
	 * Return maximum value from array.
	 * 
	 * @param array
	 * 
	 * @return maximum value
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Number max(Number[] array) {
		List list = Arrays.asList(array);
		Collections.sort(list);
		return (Integer) list.get(array.length - 1);
	}

	/**
	 * Return minimum value from array.
	 * 
	 * @param array
	 * @return minimum value
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Number min(Number[] array) {
		List list = Arrays.asList(array);
		Collections.sort(list);
		return (Integer) list.get(0);
	}

	/**
	 * Remove the all element that match the specified element from integer
	 * array.
	 * 
	 * @param array
	 *            integer array.
	 * @param element
	 *            to be removed
	 * @return array after removing element.
	 */
	public static Number[] removeElement(Number[] array, Number element) {
		int length = array.length;
		for (int i = 0; i < length; i++)
			if (element == array[i]) {
				for (int j = i; j < length - 1; j++)
					array[j] = array[j + 1];
				length--;
			}
		if (length < array.length) {
			Number array1[] = new Number[length];
			System.arraycopy(array, 0, array1, 0, length);
			return array1;
		}
		return array;
	}

	/**
	 * Sort the array using bubble sort.
	 * 
	 * @param array
	 */
	public static void sortIntegerArray(int[] array) {
		for (int i = array.length - 1; i >= 0; i--) {
			for (int j = 0; j < i; j++) {
				if (array[j] > array[j + 1]) {
					swapIntArray(array, j, j + 1);
				}
			}
		}
	}

	/**
	 * 
	 * @param size
	 * @param array
	 * @return
	 */
	public static String[] dynArray(int size, String array[]) {
		int counter = 0;
		String temp = "";
		if (size == array.length) {
			for (int i = 0; i < array.length; i++) {
				if (array[i].matches("[a-zA-z\\s]")) {
					size--;
					temp = array[i];
					if (!(array[counter].equalsIgnoreCase(array[i]))) {
						array[i] = array[counter];
						array[counter] = temp;
						counter++;
					}
				}
			}
			String[] tmpArray = new String[size];
			System.arraycopy(array, counter, tmpArray, 0, size);
			return tmpArray;
		} else {
			return null;
		}
	}

	/**
	 * Searches the given array for the given object (which can be null) by
	 * calling equals() on each item.
	 * 
	 * @param _array
	 *            - array of Objects to be searched
	 * @param _object
	 *            - object to search
	 * @return true if the array contains the _object, false otherwise
	 */

	public static boolean contains(final Object[] _array, final Object _object) {
		if (_array == null || _array.length == 0) {
			return false;
		}

		for (int i = 0; i < _array.length; i++) {
			if (_object == _array[i]) {
				return true;
			}

			if (_object != null && _object.equals(_array[i])) {
				return true;
			}
		}

		return false;
	}
	/**
	 * Searches the given array for the given object backwards and returns the
	 * index of the last slot containing that object. 'null' values are allowed.
	 * 
	 * @param _array
	 *            - an array of objects
	 * @param _object
	 *            - the object to be searched for (or null)
	 * @return the last index of object in the array, or -1 if it wasn't found
	 */
	public static int lastIndexOfObjectIdenticalTo(final Object[] _array, final Object _object) {
		if (_array == null || _array.length == 0) {
			return -1;
		}

		for (int i = _array.length - 1; i >= 0; i--) {
			if (_array[i] == _object) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * Searches the given array for the given object backwards and returns the
	 * index of the last slot containing that object. 'null' values are allowed.
	 * 
	 * @param _array
	 *            - an array of objects
	 * @param _object
	 *            - the object to be searched for (or null)
	 * @return the last index of object in the array, or -1 if it wasn't found
	 */
	public static int lastIndexOfObjectEqualTo(Object[] _array, Object _object) {
		if (_array == null || _array.length == 0) {
			return -1;
		}

		for (int i = _array.length - 1; i >= 0; i--) {
			if (_array[i] == _object) {
				return i;
			}
			if (_object == null || _array[i] == null) {
				continue; /* one of the two is null */
			}
			if (_array[i].equals(_object)) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * This methods takes a list and separates it into a set of batches.
	 * <p>
	 * Example: batchToList(source, 2):
	 * 
	 * <pre>
	 *   source: ( 1, 2, 3, 4, 5 )
	 *   result: ( ( 1, 2 ), ( 3, 4 ), ( 5 ) )
	 * </pre>
	 * 
	 * @param _list
	 *            - the list which should be split
	 * @param _batchSize
	 *            - the size of a batch
	 * @return a list of lists containing the objects
	 */
	public static List<?> batchToList(final List<?> _list, final int _batchSize) {
		if (_list == null) {
			return emptyList;
		}

		List<List<Object>> batches;
		final int len = _list.size();

		if (_batchSize >= len || _batchSize < 1) {
			batches = new ArrayList<List<Object>>(1);
			batches.add(new ArrayList<Object>(_list));
			return batches;
		}

		int batchCount = len / _batchSize;
		if (len % _batchSize != 0) {
			batchCount++;
		}
		batches = new ArrayList<List<Object>>(batchCount);

		List<Object> batch = new ArrayList<Object>(_batchSize);
		batches.add(batch);

		int fillSize = 0;
		for (int i = 0; i < len; i++) {
			if (fillSize == _batchSize) {
				batch = new ArrayList<Object>(_batchSize);
				batches.add(batch);
				fillSize = 0;
			}

			batch.add(_list.get(i));
			fillSize++;
		}

		return batches;
	}

	/**
	 * This methods takes an array and separates it into a set of batches.
	 * <p>
	 * Example: batchToList(source, 2):
	 * 
	 * <pre>
	 *   source: [ 1, 2, 3, 4, 5 ]
	 *   result: ( ( 1, 2 ), ( 3, 4 ), ( 5 ) )
	 * </pre>
	 * 
	 * @param _array
	 *            - the array which should be split
	 * @param _batchSize
	 *            - the size of a batch
	 * @return a list of lists containing the objects
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List batchToList(final Object[] _array, final int _batchSize) {
		if (_array == null) {
			return emptyList;
		}

		List<List<Object>> batches;
		int len = _array.length;

		if (_batchSize >= len || _batchSize < 1) {
			batches = new ArrayList<List<Object>>(1);
			batches.add((List<Object>) asList(_array));
			return batches;
		}

		int batchCount = len / _batchSize;
		if (len % _batchSize != 0) {
			batchCount++;
		}
		batches = new ArrayList<List<Object>>(batchCount);

		List<Object> batch = new ArrayList<Object>(_batchSize);
		batches.add(batch);

		int fillSize = 0;
		for (int i = 0; i < len; i++) {
			if (fillSize == _batchSize) {
				batch = new ArrayList<Object>(_batchSize);
				batches.add(batch);
				fillSize = 0;
			}

			batch.add(_array[i]);
			fillSize++;
		}

		return batches;
	}

	/**
	 * Iterates over all objects in the list, retrieves the value for the given
	 * key and stores it into a new list in the same order. This method is
	 * slightly faster than using getPropertyValue, which of course can also be
	 * used for simple keys.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * List projectIds = valuesForKeys(projects, &quot;logID&quot;);
	 * </pre>
	 * <p>
	 * The returned List should be considered immutable.
	 * 
	 * @see getPropertyValue()
	 * 
	 * @param _objects
	 *            the objects to retrieve the values from
	 * @param _key
	 *            the key of the values to be retrieved
	 * @return a list containing the values for the given key
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<T> getPropertyValue(Collection<?> _objects, String _key) {
		if (_objects == null) {
			return (List<T>) emptyList;
		}
		int len = _objects.size();
		if (len == 0) {
			return (List<T>) emptyList;
		}

		final List<T> values = new ArrayList<>(len);
		for (Object o : _objects) {
			if (o == null) {
				;
			} else if (o instanceof Map) {
				o = ((Map) o).get(_key);
			} else {
				o = LogicUnit.getField(o, _key);
			}
			if (!values.contains(o)) {
				values.add((T) o);
			}
		}

		return values;
	}

	/**
	 * Iterates over all objects in the list, retrieves the value for the given
	 * key path and stores it into a new list in the same order.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * List ownerIds = getPropertyValue(projects, &quot;owner.id&quot;);
	 * </pre>
	 * <p>
	 * The returned List should be considered immutable.
	 * 
	 * @see valuesForKey()
	 * 
	 * @param _objects
	 *            the objects to retrieve the values from
	 * @param _path
	 *            the keypath of the values to be retrieved
	 * @return a list containing the values for the given key
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getPropertyValue(final List<Object> _objects, final String _path) {
		/* separate method for performance reasons */
		if (_objects == null) {
			return (List<T>) emptyList;
		}

		final int len = _objects.size();
		if (len == 0) {
			return (List<T>) emptyList;
		}

		final List<T> values = new ArrayList<>(len);
		for (int i = 0; i < len; i++) {
			Object o = _objects.get(i);

			if (o == null) {
				;
			} else {
				o = LogicUnit.getField(o, _path);
			}
			if (!values.contains(o)) {
				values.add((T) o);
			}
		}

		return values;
	}

	/**
	 * Iterates over all objects in the array, retrieves the value for the given
	 * key and stores it into a new array in the same order. This method is
	 * slightly faster than using getPropertyValue, which of course can also be
	 * used for simple keys.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * Object[] projectIds = valuesForKeys(projects, &quot;logID&quot;);
	 * </pre>
	 * 
	 * @see getPropertyValue()
	 * 
	 * @param _objects
	 *            the objects to retrieve the values from
	 * @param _key
	 *            the key of the values to be retrieved
	 * @return an array containing the values for the given key
	 */
	public static Object[] valuesForKey(Object[] _objects, String _key) {
		if (_objects == null) {
			return emptyArray;
		}

		final int len = _objects.length;
		if (len == 0) {
			return emptyArray;
		}

		final Object[] values = new Object[len];
		for (int i = 0; i < len; i++) {
			Object o = _objects[i];

			if (o == null) {
				;
			} else if (o instanceof Map) {
				o = ((Map<?, ?>) o).get(_key);
			} else {
				o = LogicUnit.getField(o, _key);
			}

			values[i] = o;
		}

		return values;
	}

	/**
	 * Iterates over all objects in the array, retrieves the value for the given
	 * key path and stores it into a new array in the same order.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * Object[] ownerIds = getPropertyValue(projects, &quot;owner.id&quot;);
	 * </pre>
	 * 
	 * @see valuesForKey()
	 * 
	 * @param _objects
	 *            the objects to retrieve the values from
	 * @param _path
	 *            the keypath of the values to be retrieved
	 * @return an array containing the values for the given key
	 */
	public static Object[] getPropertyValue(Object[] _objects, String _path) {
		/* separate method for performance reasons */
		if (_objects == null) {
			return emptyArray;
		}

		int len = _objects.length;
		if (len == 0) {
			return emptyArray;
		}

		final Object[] values = new Object[len];
		for (int i = 0; i < len; i++) {
			Object o = _objects[i];

			if (o == null) {
				;
			} else {
				o = LogicUnit.getField(o, _path);
			}

			values[i] = o;
		}

		return values;
	}

	/**
	 * This method walks over a collection and groups the contained object based
	 * on the value of some keypath. Example:
	 * 
	 * <pre>
	 * Map groupedByEntity = groupByKeyPath(myObjects, "entity.name")
	 * </pre>
	 * 
	 * This will iterate over the given objects and group them by the entity, a
	 * result could look like:
	 * 
	 * <pre>
	 * { Person = [ xyz, def ]; Team = [ abc, def ]; }
	 * </pre>
	 * 
	 * @param _objects
	 *            - objects which should be grouped by the given keypath
	 * @param _keyPath
	 *            - the criteria to group on
	 * @return a Map which contains the group values at the keys
	 */
	public static HashMap<Object, List<Object>> groupByKeyPath(final Collection<?> _objects, final String _keyPath) {
		if (_objects == null) {
			return null;
		}

		final HashMap<Object, List<Object>> resultMap = new HashMap<Object, List<Object>>(16);

		for (Object object : _objects) {
			/* we do the instanceof because the first case is the usual one */
			final Object group = LogicUnit.getField(object, _keyPath);

			List<Object> groupValues = resultMap.get(group);
			if (groupValues == null) {
				groupValues = new ArrayList<Object>(4);
				resultMap.put(group, groupValues);
			}

			groupValues.add(object);
		}

		return resultMap;
	}

	/**
	 * This method walks over a collection and groups the contained object based
	 * on the value of some key. Example:
	 * 
	 * <pre>
	 * Map groupedByCity = groupByKey(myObjects, "city")
	 * </pre>
	 * 
	 * This will iterate over the given objects and group them by the city, a
	 * result could look like:
	 * 
	 * <pre>
	 * {  Magdeburg = [
	 *     { city = Madgeburg; name = "SWM"; },
	 *     { city = Magdeburg; name = "Skyrix AG"; }
	 *   ];
	 *   Dortmund = [ ... ]; }
	 * </pre>
	 * <p>
	 * This method currently calls groupByKeyPath() with the given key as the
	 * keypath.
	 * 
	 * @param _objects
	 *            objects which should be grouped by the given key
	 * @param _key
	 *            the criteria to group on (eg 'city' or 'lastname')
	 * @return a Map which contains the group values at the keys
	 */
	public static HashMap<Object, List<Object>> groupByKey(final Collection<?> _objects, final String _key) {
		// TBD: we could optimize that with a specific implementation
		return groupByKeyPath(_objects, _key);
	}

	/**
	 * This method groups a collection based on a set of criterias. Eg given
	 * those keys: [ city, zip ] a result could look like:
	 * 
	 * <pre>
	 * {
	 *  Magdeburg = {
	 *    39104 = [ { name = Skyrix; ...}, { name = TLG; ...} ];
	 *    39122 = [ { name = Telekom; ...} ];
	 *  };
	 *  Berlin = {
	 *    10233 = ...
	 *    23882 = ...
	 *  };
	 * }
	 * </pre>
	 * 
	 * Note that only the last object in the tree is a List, all the parent
	 * groups are Maps.
	 * <p>
	 * 
	 * @param _objects
	 *            the objects to be grouped
	 * @param _keyPathes
	 *            the keypathes to group the objects on
	 * @return a Map of Maps or Lists representing the grouping-tree
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map groupByKeyPathes(final Collection _objects, final String[] _keyPathes) {
		final int keyPathCount = _keyPathes != null ? _keyPathes.length : 0;
		if (_objects == null || keyPathCount == 0) {
			return null;
		}

		if (keyPathCount == 1) {
			return groupByKeyPath(_objects, _keyPathes[0]);
		}

		final Map<Object, Object> resultMap = new HashMap<Object, Object>(16);

		for (Object object : _objects) {
			Map groupCursor = resultMap;
			Object group;

			/* iterate until the last group, which contains a list, not a map */

			for (int i = 1; i < keyPathCount; i++) {
				/* we do the instanceof because the first case is the usual one */
				group = LogicUnit.getField(object, _keyPathes[i - 1]);

				Map groupContent = (HashMap) groupCursor.get(group);
				if (groupContent == null) {
					groupContent = new HashMap(16);
					groupCursor.put(group, groupContent);
				}

				groupCursor = groupContent;
			}

			/* process the last group */

			group = LogicUnit.getField(object, _keyPathes[keyPathCount - 1]);

			List<Object> groupValues = (List<Object>) groupCursor.get(group);
			if (groupValues == null) {
				groupValues = new ArrayList<Object>(4);
				groupCursor.put(group, groupValues);
			}

			groupValues.add(object);
		}

		return resultMap;
	}

	/**
	 * Extracts a Map from an array of values.
	 * <p>
	 * Using the _keyIndices array you can use one value of the _array multiple
	 * times, eg:
	 * 
	 * <pre>
	 *   array   = [ 'Donald', '1954-12-12' ]
	 *   keys    = [ 'name', 'birthdayAsText', 'birthdayAsDate' ]
	 *   indices = [ 0, 1, 1 ]
	 *   formats = [ null, null, new SimpleDateFormat() ]
	 * </pre>
	 * 
	 * Results in:
	 * 
	 * <pre>
	 *   { name           = Donald;
	 *     birthdayAsText = '1954-12-12';
	 *     birthdayAsDate = &lt;CalendarDate: 1954-12-12&gt;; }
	 * </pre>
	 * 
	 * And another simple example:
	 * 
	 * <pre>
	 *   extractRecordFromArray(new Object[] { 'Donald', '1954-12-12' ] });
	 * </pre>
	 * 
	 * Results in:
	 * 
	 * <pre>
	 *   { 0 = Donald; 1 = '1954-12-12'; }
	 * </pre>
	 * 
	 * <p>
	 * If no keys are specified, numeric Integer keys are generated
	 * (0,1,2,3,...).
	 * <p>
	 * If a format throws a ParseException, the exception is stored in the slot.
	 * 
	 * @param _array
	 *            - array of values
	 * @param _keys
	 *            - array of Map keys to generate
	 * @param _keyIndices
	 *            - if null, _keys and _array indices must match
	 * @param _keyFormats
	 *            - value Format objects, when null, use value as-is
	 * @param _excludeNulls
	 *            - whether null values should be added
	 * @return a Map constructed according to the specification
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap extractRecordFromArray(Object[] _array, Object[] _keys, int[] _keyIndices, Format[] _keyFormats, boolean _excludeNulls) {
		if (_array == null) {
			return null;
		}

		if (_keys == null) {
			_keys = listForCount(_array.length).toArray(); // 0,1,2,3
		}

		final int count = _keys.length;
		final HashMap map = new HashMap(count);

		for (int i = 0; i < count; i++) {
			final Object key = _keys[i];
			Object value;

			/* retrieve value */

			if (_keyIndices != null) {
				int idx = i < _keyIndices.length ? _keyIndices[i] : -1;
				value = i >= 0 && i < _array.length ? _array[idx] : null;
			} else {
				value = (i < _array.length) ? value = _array[i] : null;
			}

			/* apply format */

			if (_keyFormats != null && _keyFormats.length > i) {
				try {
					value = _keyFormats[i].parseObject(value.toString());
				} catch (ParseException e) {
					value = e;
				}
			}

			/* add to map */

			if (value != null || !_excludeNulls) {
				map.put(key, value);
			}
		}

		return map;
	}

	/**
	 * Extracts a List of Maps from an two-dimensional array of values. This is
	 * useful for converting parsed CSV files to a set of records.
	 * <p>
	 * Using the _keyIndices array you can use one value of the _array multiple
	 * times, eg:
	 * 
	 * <pre>
	 *   array   = [ [ 'Donald', '1954-12-12' ] ]
	 *   keys    = [ 'name', 'birthdayAsText', 'birthdayAsDate' ]
	 *   indices = [ 0, 1, 1 ]
	 *   formats = [ null, null, new SimpleDateFormat() ]
	 * </pre>
	 * 
	 * Results in:
	 * 
	 * <pre>
	 *   [ { name           = Donald;
	 *       birthdayAsText = '1954-12-12';
	 *       birthdayAsDate = &lt;CalendarDate: 1954-12-12&gt;; } ]
	 * </pre>
	 * 
	 * <p>
	 * If no keys are specified, numeric Integer keys are generated
	 * (0,1,2,3,...).
	 * 
	 * @param _array
	 *            - an array of arrays of values
	 * @param _keys
	 *            - array of Map keys to generate
	 * @param _keyIndices
	 *            - if null, _keys and _array indices must match
	 * @param _keyFormats
	 *            - value Format objects, when null, use value as-is
	 * @param _excludeNulls
	 *            - whether null values should be added
	 * @return a List of Maps constructed according to the specification
	 */
	@SuppressWarnings("rawtypes")
	public static List<Map> extractRecordsFromArrays(Object[][] _array, Object[] _keys, int[] _keyIndices, Format[] _keyFormats, boolean _excludeNulls) {
		if (_array == null) {
			return null;
		}

		final List<Map> list = new ArrayList<Map>(_array.length);

		for (Object[] recordArray : _array) {
			list.add(extractRecordFromArray(recordArray, _keys, _keyIndices, _keyFormats, _excludeNulls));
		}

		return list;
	}

	/**
	 * Converts Collections and primitive arrays to List's. If the object
	 * already is a List, its returned directly.
	 * 
	 * @param _array
	 *            - an arbitary Java array (eg int[], Object[]), or Collection
	 * @return a List containing the object items
	 */
	@SuppressWarnings("unchecked")
	public static List<?> asList(final Object _array) {
		// TBD: whats the difference to the Arrays.asList function?
		if (_array == null) {
			return null;
		}
		if (_array instanceof List) {
			return (List<Object>) _array;
		}
		if (_array instanceof Collection) {
			return new ArrayList<Object>((Collection<?>) _array);
		}
		final Class<?> itemClazz = _array.getClass().getComponentType();
		if (itemClazz == null) { /* not an array */
			List<Object> al = new ArrayList<Object>(1);
			al.add(_array);
			return al;
		}

		if (itemClazz == java.lang.Integer.TYPE) {
			final int[] nums = (int[]) _array;
			List<Integer> al = new ArrayList<Integer>(nums.length);
			for (int i = 0; i < nums.length; i++) {
				al.add(nums[i]);
			}
			return al;
		}

		if (itemClazz == java.lang.Long.TYPE) {
			final long[] nums = (long[]) _array;
			List<Long> al = new ArrayList<Long>(nums.length);
			for (int i = 0; i < nums.length; i++) {
				al.add(nums[i]);
			}
			return al;
		}

		return asList(_array);
	}

	/**
	 * If _array is not null, returns a copy of array which has one more element
	 * than _array, _element being the last element in this array (even if
	 * _element is null).
	 * 
	 * If _array is null but _element isn't null, returns an array consisting
	 * just of _element.
	 * 
	 * If both _array and _element are null, returns null.
	 * 
	 * @param _array
	 *            - a Java array or null (see above)
	 * @param _element
	 *            - a Java object of same type as _array or null (see above)
	 * @return an extended array or null (see above)
	 */
	public static <T> T[] arrayByAppending(final T[] _array, final T _element) {
		return arrayByAppending(_array, _element, false);
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] arrayByAppending(final T[] _array, final T _element, boolean createGenericArray) {
		T[] extendedArray;
		if (_array == null) {
			if (_element == null) {
				return null;
			} else {
				extendedArray = (T[]) Array.newInstance(createGenericArray ? Object.class : _element.getClass(), 1);
			}
		} else {
			// NOTE: array isn't null, but element might be!
			Class<?> componentType = _array.getClass().getComponentType();
			extendedArray = (T[]) Array.newInstance(componentType, _array.length + 1);
			System.arraycopy(_array, 0, extendedArray, 0, _array.length);
		}
		extendedArray[extendedArray.length - 1] = _element;
		return extendedArray;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] arrayByRemovingFirst(final T[] _array) {
		if (_array == null) {
			return null;
		}
		Class<?> componentType = _array.getClass().getComponentType();
		T[] extendedArray = (T[]) Array.newInstance(componentType, _array.length - 1);
		System.arraycopy(_array, 1, extendedArray, 0, extendedArray.length);
		return extendedArray;
	}

	/**
	 * Converts an array of objects into an array of int's. If a slot is a
	 * Number object, this will store the intValue() of the number. If its a
	 * String object, the String is parsed using Integer.parseInt(). If a
	 * NumberFormatException occures, we store 0. For other objects we invoke
	 * UObject.intValue() to determine the 'int' representation of the object.
	 * 
	 * @param _values
	 *            - an array of objects to convert to integers
	 * @return an array of int's
	 */
	public static int[] intValuesForObjects(final Object[] _values) {
		if (_values == null) {
			return null;
		}
		final int count = _values.length;
		final int[] nums = new int[count];
		for (int i = 0; i < count; i++) {
			final Object v = _values[i];

			if (v instanceof Number) {
				nums[i] = ((Number) v).intValue();
			} else if (v instanceof String) {
				/* Note: we return 0 for empty strings */
				if (((String) v).length() == 0) {
					nums[i] = 0;
				} else {
					try {
						nums[i] = Integer.parseInt((String) v);
					} catch (NumberFormatException e) {
						nums[i] = 0;
					}
				}
			} else {
				nums[i] = CastingUtil.intValue(v.toString());
			}
		}

		return nums;
	}

	/**
	 * Returns a List which contains a sequence of Integer objects.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * List&lt;Number&gt; a = listForCount(10);
	 * </pre>
	 * 
	 * This will return the List "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]".
	 * 
	 * @param _count
	 *            - number of items in the List
	 * @return a List
	 */
	@SuppressWarnings("unchecked")
	public static List<Number> listForCount(final int _count) {
		// TBD: we should return a special List object which calculates the indices
		//      on the fly
		if (_count < 0) {
			return null; // TBD: we could return neg lists, eg 0,-1,-2,-3,-4
		}
		if (_count == 0) {
			return Collections.EMPTY_LIST;
			//return Collections.EMPTY_LIST;
		}
		final List<Number> l = new ArrayList<Number>(_count);
		for (int i = 0; i < _count; i++) {
			l.add(new Integer(i));
		}
		return l;
	}

	/**
	 * <p>
	 * To calculate size of the specified collection.
	 * </p>
	 * 
	 * @param collectionObj
	 *            of which size is to be known
	 * @return size of the specified collection.
	 * @since Brijframework 1.0
	 */
	public int count(Collection<?> collectionObj) {
		return collectionObj.size();
	}

	public static boolean compareCollectionSize(Collection<?> coll1, Collection<?> coll2) {
		if (coll1 == null && coll2 == null) {
			return true;
		} else {
			if (coll1 == null) {
				return false;
			} else if (coll2 == null) {
				return false;
			} else {
				return coll1.size() == coll2.size();
			}
		}
	}

	public static Map<Object, Object> mapForKey(List<?> list, String key) {
		Map<Object, Object> hash = new HashMap<>();
		for (Object object : list) {
			hash.put(LogicUnit.getField(object, key), object);
		}
		return hash;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> listDifference(Class<T> objectClass, List<?> list1, List<?> list2) {
		List<T> resultantArrayList = new ArrayList<>();
		List<T> tempList1 = new ArrayList<>((List<T>) list1);

		list1.removeAll(list2);
		list2.removeAll(tempList1);

		resultantArrayList.addAll((List<T>) list1);
		resultantArrayList.addAll((List<T>) list2);
		return resultantArrayList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Collection getValuesFormCollection(Collection<?> list, String property) {
		if (property == null) {
			return null;
		}
		Collection<Object> values = (Collection<Object>) InstanceUtil.getInstance(list.getClass());
		for (Object obj : list) {
			if (LogicUnit.getAllField(obj).containsKey(property)) {
				values.add(LogicUnit.getField(obj, property));
			}
		}
		return values;
	}

	/**
	 * this method get List of values for property of object
	 * 
	 * @param Collection
	 * @return Map
	 */
	public Map<?, ?> getMultiValuesFormCollection(Collection<?> list, String property) {
		if (property == null) {
			return null;
		}
		Map<String, Object> objMap = new HashMap<>();
		List<Object> values = new ArrayList<>();
		for (Object obj : list) {
			if (LogicUnit.getAllField(obj).containsKey(property)) {
				values.add(LogicUnit.getField(obj, property));
			}
		}
		objMap.put(property, values);
		return objMap;
	}

	public static void setPropertiesFromCollection(Collection<?> list, Map<String, Object> map) {

	}

	public static Map<String, Object> getPropertiesFromCollection(Collection<?> list, String[] properties) {
		return null;
	}

	

	public static Collection<?> textToCollection(String buffer, String text) {
		if (buffer == null) {
			return new ArrayList<>();
		}

		StringTokenizer token = new StringTokenizer(buffer, text, false);
		Collection<Object> returnVector = new ArrayList<>();
		while (token.hasMoreTokens()) {
			returnVector.add(token.nextElement());
		}
		return returnVector;
	}

	
	public static Collection<?> textToCollectionNoWhiteSpace(String buffer, String delim) {
		if (buffer == null) {
			return new ArrayList<>();
		}
		StringTokenizer token = new StringTokenizer(buffer, delim);
		Collection<Object> returnVector = new ArrayList<>();
		while (token.hasMoreTokens()) {
			String str = token.nextElement().toString();
			if (!str.equals("") && str != null) {
				returnVector.add(str);
			}
		}
		return returnVector;
	}

	public static Collection<?> textToCollection(String buffer) {
		if (buffer == null) {
			return new ArrayList<>();
		}

		StringTokenizer token = new StringTokenizer(buffer, ",", true);
		String prev = " ";
		Collection<Object> returnVector = new ArrayList<>();
		while (token.hasMoreTokens()) {
			String o = (String) token.nextElement();
			if (o.equals(",")) {
				if (prev.equals(o)) {
					returnVector.add(" ");
				}
				prev = o;
			} else {
				returnVector.add(o);
				prev = o;
			}

		}
		return returnVector;
	}

	public static Collection<?> mapToCollection(Map<?, ?> map) {
		Collection<Object> returnVector = new ArrayList<>();
		for (Object key : map.keySet()) {
			returnVector.add(map.get(key));
		}
		return returnVector;
	}
	
	/**
	 * this method get Map of values of objects
	 * 
	 * @param Map,
	 *            Object[]
	 * @return Map
	 */
	public static  Map<String, Object> getPropertiesFromMap(Map<?, ?> map, Object[] properties) {
		Map<String, Object> returnMap = new HashMap<>();
		for (int i = 0; i < properties.length; i++) {
			returnMap.put((String) properties[i], map.get(properties[i]));
		}
		return returnMap;
	}
	
	/**
	 * this method get Map of values of objects
	 * 
	 * @param origMap,map
	 * @return void
	 */
	public static  void setPropertiesFromMap(Map<String, Object> origMap, Map<String, Object> map) {
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			origMap.put(entry.getKey(), entry.getValue());
		}
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Hashtable convertCollectionToMap(Collection<?> vt, String keys) {
		Hashtable returnHash = new Hashtable();
		Collection keyVt = convertStringtoArray(keys);
		for (Iterator<?> i = vt.iterator(); i.hasNext();) {
			Hashtable row = (Hashtable) i.next();
			Hashtable hashTable = returnHash;
			for (Iterator<?> e1 = keyVt.iterator(); i.hasNext();) {
				String key = e1.next().toString();
				Object xxx = row.get(key);
				String hashKey;
				if (xxx != null && xxx instanceof Number) {
					hashKey = key + "_" + ((Number) row.get(key)).intValue();
				} else {
					hashKey = key + "_" + StringUtil.cleanText(xxx.toString());
				}
				if (e1.hasNext()) {
					Hashtable loopHash = (Hashtable) hashTable.get(hashKey);
					if (loopHash == null) {
						loopHash = new Hashtable();
					}
					hashTable.put(hashKey, loopHash);
					hashTable = loopHash;
				} else {
					hashTable.put(hashKey, row);
				}
			}
		}
		return returnHash;
	}

	public void updateObjectFromMap(Object aObject, Map<String, Object> map) {
		this.updateObjectFromMap(aObject, map, true);
	}

	public void updateObjectFromMap(Object object, Map<String, Object> map, boolean skipNullValues) {
		if (map == null) {
			return;
		}
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			if (map.get(key) == null && skipNullValues) {
				continue;
			}
			LogicUnit.setField(object, map.get(key).toString(), key);
		}
	}

	public void updateObjectGraphFromMap(Object object, Map<?, ?> _map) {
		for (Map.Entry<?, ?> entry : _map.entrySet()) {
			String key = (String) entry.getKey();
			Object value = entry.getValue();
			LogicUnit.setField(object,key, value);
		}
	}

	public Map<String, Object> getFieldsMapFromObject(Object obj) {
		Map<String, Object> map = new HashMap<>();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			String key = fields[i].getName();
			Object value = LogicUnit.getField(obj,key);
			map.put(key, value);
		}
		return map;
	}

	public Object[] arrayByAddingObjAtZeroIndex(Object object, Object... paramObjects) {
		Object[] objArray = new Object[paramObjects.length + 1];
		objArray[0] = object;
		for (int i = 1; i <= paramObjects.length; i++) {
			objArray[i] = paramObjects[i - 1];
		}
		return objArray;
	}

	public Object castObject(Object object, String type) {
		if (type == null || type.trim().equals("")) {
			return object;
		}
		return CastingUtil.castObject(object, ClassUtil.getClass(type));
	}

	public static Vector<Object> getLinesForFile(File file) throws IOException {
		return getLinesForString(new String(FileUtil.loadByteFile(file)));
	}

	public static Vector<Object> getLinesForString(String value) {
		try {
			BufferedReader reader = new BufferedReader(new StringReader(value));
			Vector<Object> x = new Vector<>();
			String line;
			while ((line = reader.readLine()) != null) {
				x.addElement(line);
			}
			return x;
		} catch (IOException e) {
			return null;
		}
	}

	public static Vector<Object> getPartVectorForString(String str, int parts) {
		Vector<Object> vt = new Vector<>();
		if (str.length() == 0) {
			vt.addElement(str);
			return vt;
		}

		int seg = (int) Math.ceil((double) str.length() / (double) parts);
		for (int i = 0; i < seg - 1; i++) {
			vt.addElement(str.substring(parts * i, parts * (i + 1)));
		}
		vt.addElement(str.substring(parts * (seg - 1), str.length()));
		return vt;
	}

	public static byte[] hashToBytes(Hashtable<?, ?> hash) {
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			ObjectOutputStream p = new ObjectOutputStream(byteStream);
			p.writeObject(hash);
			p.flush();
			byte[] array = byteStream.toByteArray();
			p.close();
			byteStream.close();
			return array;
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Hashtable<String, Object> readHashTable(File file) {
		try {
			ObjectInputStream p = new ObjectInputStream(new FileInputStream(file));
			Hashtable hash = (Hashtable) p.readObject();
			p.close();
			return hash;
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean getBoolean(Map<String, Object> map, String key) {
		Object value = map.get(key);
		if (value != null && value instanceof Number && ((Number) value).intValue() != 0) {
			return true;
		} else if (value != null && value instanceof String && ((String) value).compareTo("1") == 0) {
			return true;
		} else
			return false;
	}

	

	public static Vector<Object> convertStringtoArray(String buffer, String token1) {
		if (buffer == null) {
			return new Vector<>();
		}

		StringTokenizer token = new StringTokenizer(buffer, token1, false);
		Vector<Object> vt = new Vector<>();
		while (token.hasMoreTokens()) {
			vt.addElement(token.nextElement());
		}
		return vt;
	}

	public static Vector<Object> convertStringtoArray(String buffer) {
		if (buffer == null) {
			return new Vector<>();
		}

		StringTokenizer token = new StringTokenizer(buffer, ",", false);
		Vector<Object> vt = new Vector<>();
		while (token.hasMoreTokens()) {
			vt.addElement(token.nextElement());
		}
		return vt;
	}

	public static Vector<Object> convertStringtoArray1(String buffer) {
		if (buffer == null) {
			return new Vector<>();
		}

		StringTokenizer token = new StringTokenizer(buffer, ",", true);
		String prev = " ";
		Vector<Object> vt = new Vector<>();
		while (token.hasMoreTokens()) {
			String o = (String) token.nextElement();
			if (o.equals(",")) {
				if (prev.equals(o)) {
					vt.addElement(" ");
				}
				prev = o;
			} else {
				vt.addElement(o);
				prev = o;
			}

		}
		return vt;
	}

	public boolean isEquivalent(Object object1, Object object2, Map<String, String> keyMap, boolean isDebug) {
		Iterator<Entry<String, String>> itr = keyMap.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, String> entry = itr.next();
			Object value1 = LogicUnit.getField(object2,entry.getKey());
			Object value2 = LogicUnit.getField(object1,entry.getValue());
			if (value1 != null) {
				if (!value1.equals(value2)) {
					if (isDebug) {
						System.out.println(entry.getKey() + " = " + value1 + " ; " + entry.getValue() + " = " + value2);
					}
					return false;
				}
			} else {
				if (value2 != null) {
					if (isDebug) {
						System.out.println(entry.getKey() + " = " + value1 + " ; " + entry.getValue() + " = " + value2);
					}
					return false;
				}
			}
		}
		return true;
	}

	public static Vector<Object> convertStringToVectorNoWhiteSpace(String buffer, String delim) {
		if (buffer == null) {
			return new Vector<>();
		}
		StringTokenizer token = new StringTokenizer(buffer, delim);
		Vector<Object> vt = new Vector<>();
		while (token.hasMoreTokens()) {
			String str = token.nextElement().toString();
			if (!str.equals("") && str != null) {
				vt.addElement(str);
			}
		}
		return vt;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Vector convertHashtoVector(Hashtable h) {
		Vector returnVector = new Vector();
		Enumeration enumer = h.elements();
		while (enumer.hasMoreElements()) {
			Object item = enumer.nextElement();
			returnVector.add(item);
		}
		return returnVector;
	}

	public static void writeHashTable(File file, Hashtable<String, Object> hash) {
		try {
			ObjectOutputStream p = new ObjectOutputStream(new FileOutputStream(file));
			p.writeObject(hash);
			p.flush();
			p.close();
		} catch (Exception e) {
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Hashtable convertVectorToHash(Vector vt, String keys) {
		Hashtable returnHash = new Hashtable();
		Vector keyVt = convertStringtoArray(keys);
		for (Enumeration e = vt.elements(); e.hasMoreElements();) {
			Hashtable row = (Hashtable) e.nextElement();
			Hashtable hashTable = returnHash;
			for (Enumeration e1 = keyVt.elements(); e1.hasMoreElements();) {
				String key = e1.nextElement().toString();
				Object xxx = row.get(key);
				String hashKey;
				if (xxx != null && xxx instanceof Number) {
					hashKey = key + "_" + ((Number) row.get(key)).intValue();
				} else {
					hashKey = key + "_" + StringUtil.cleanText(xxx.toString());
				}
				if (e1.hasMoreElements()) {
					Hashtable loopHash = (Hashtable) hashTable.get(hashKey);
					if (loopHash == null) {
						loopHash = new Hashtable();
					}
					hashTable.put(hashKey, loopHash);
					hashTable = loopHash;
				} else {
					hashTable.put(hashKey, row);
				}
			}
		}
		return returnHash;
	}

	@SuppressWarnings("rawtypes")
	public static String[] ConvertVectorToStringArray(Vector lines) {
		String[] returnArray = new String[lines.size()];
		for (int i = 0; i < lines.size(); i++) {
			returnArray[i] = (String) lines.elementAt(i);
		}
		return returnArray;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Vector ConvertStringArrayToVector(String[] lines) {
		Vector returnVector = new Vector();
		for (int i = 0; i < lines.length; i++) {
			returnVector.addElement(lines[i]);
		}
		return returnVector;
	}
	
	

	@SuppressWarnings("rawtypes")
	public static int getIntFromHash(Map hash, String key, boolean isMandatory, int min, int max, int defaultValue) {
		Object object = hash.get(key);
		if (object != null && (object instanceof Integer || object instanceof String || object instanceof String[])) {
			if (object instanceof String) {
				object = Integer.valueOf(getUncommentedValue(object.toString()));
			}
			if (object instanceof String[] && ((String[]) object).length > 0) {
				object = Integer.valueOf(((String[]) object)[0]);
			}
			int i = ((Integer) object).intValue();
			if (i < min) {
				System.out.println("Minimum permissible value of " + key + " is " + min + " so taking " + min + " as its value");
				return min;
			}
			if (i > max) {
				System.out.println("Maximum permissible value of " + key + " is " + max + " so taking " + max + " as its value");
				return max;
			}
			return i;
		} else {
			if (isMandatory) {
				throw new RuntimeException(key + " is missing in " + hash);
			} else {
				return defaultValue;
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static double getDoubleFromHash(Map hash, String key, boolean isMandatory, double min, double max, double defaultValue) {
		Object object = hash.get(key);
		if (object != null && (object instanceof Double || object instanceof String)) {
			if (object instanceof String) {
				object = Double.valueOf(getUncommentedValue(object.toString()));
			}
			double d = ((Double) object).doubleValue();
			if (d < min) {
				System.out.println("Minimum permissible value of " + key + " is " + min + " so taking " + min + " as its value");
				return min;
			}
			if (d > max) {
				System.out.println("Maximum permissible value of " + key + " is " + max + " so taking " + max + " as its value");
				return max;
			}
			return d;
		} else {
			if (isMandatory) {
				throw new RuntimeException(key + " is missing in " + hash);
			} else {
				return defaultValue;
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static boolean getBooleanFromHash(Map hash, String key, boolean isMandatory, boolean defaultValue) {
		Object object = hash.get(key);
		if (object != null && (object instanceof Integer || object instanceof String)) {
			if (object instanceof String) {
				object = Integer.valueOf(getUncommentedValue(object.toString()));
			}
			return ((Integer) object).intValue() != 0 ? true : false;
		} else {
			if (isMandatory) {
				throw new RuntimeException(key + " is missing in " + hash);
			} else {
				return defaultValue;
			}
		}
	}

	public static String getUncommentedValue(String str) {
		if (str.contains("#")) {
			str = str.substring(0, str.indexOf("#"));
		}
		return str.trim();
	}

	@SuppressWarnings("rawtypes")
	public static Vector getVectorFromHash(Map hash, String key, boolean isMandatory, Vector defaultValue) {
		Object object = hash.get(key);
		if (object != null && object instanceof Vector) {
			return (Vector) object;
		} else {
			if (isMandatory) {
				throw new RuntimeException(key + " is missing in " + hash);
			} else {
				return defaultValue;
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static Hashtable getHashFromHash(Map hash, String key, boolean isMandatory, Hashtable defaultValue) {
		Object object = hash.get(key);
		if (object != null && object instanceof Hashtable) {
			return (Hashtable) object;
		} else {
			if (isMandatory) {
				throw new RuntimeException(key + " is missing in " + hash);
			} else {
				return defaultValue;
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList<Object> getArrayFromHash(Map hash, String key, boolean isMandatory, ArrayList defaultValue) {
		Object object = hash.get(key);
		if (object != null && object instanceof ArrayList) {
			return (ArrayList<Object>) object;
		} else {
			if (isMandatory) {
				throw new RuntimeException(key + " is missing in " + hash);
			} else {
				return defaultValue;
			}
		}
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList getFilteredList(Collection _beanList, String keyPath, Object filterValue) {
		ArrayList filterList = new ArrayList();
		if (filterValue == null) { // Checked outside for loop so that we can avoid checking inside the loop
			for (Object object : _beanList) {
				Object value = LogicUnit.getField(object, keyPath);
				if (value == null) {
					filterList.add(value);
				}
			}
		} else {
			for (Object object : _beanList) {
				Object value = LogicUnit.getField(object, keyPath);
				if (filterValue.equals(value)) {
					filterList.add(value);
				}
			}
		}
		return filterList;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static void sort(List _beanObjects, String key, boolean isReverse) {
		if (isReverse) {
			Collections.reverse(_beanObjects);
		}
	}

	/**
	 * 
	 * <p>
	 * This method is used to convert string <code>Array</code> into
	 * <code>FNArrayList</code>
	 * </p>
	 * 
	 * @param stringArray
	 *            array of strings.
	 * @return <code>FNArrayList</code>
	 * @since Foundation 1.0
	 * 
	 */
	public static ArrayList<String> listForArray(String[] stringArray) {
		ArrayList<String> stringList = new ArrayList<String>();
		if (stringArray != null) {
			for (String string : stringArray) {
				stringList.add(string);
			}
		}
		return stringList;
	}

	/**
	 * 
	 * <p>
	 * This method is used to convert <code>FNArrayList</code> containing string
	 * <code>Array</code> into <code>FNArrayList</code>.
	 * </p>
	 * 
	 * @param listOfStringArray
	 *            <code>FNArrayList</code> containing <code>Array</code> of
	 *            strings .
	 * @return <code>FNArrayList</code>
	 * @since Foundation 1.0
	 * 
	 */
	public static List<String> listForArray(List<String[]> listOfStringArray) {
		ArrayList<String> stringList = new ArrayList<String>();
		if (listOfStringArray != null) {
			for (String[] stringArray : listOfStringArray) {
				stringList.addAll(listForArray(stringArray));
			}
		}
		return stringList;
	}
	
	@SuppressWarnings("rawtypes")
	public static String getCharSeparatedText(Iterable iterable, String c) {
		StringBuffer strBuf = new StringBuffer();
		Iterator itr = iterable.iterator();
		while (itr.hasNext()) {
			strBuf.append(itr.next());
			if (itr.hasNext()) {
				strBuf.append(c);
			}
		}
		return strBuf.toString();
	}
	
	public static boolean isEmpty(Object[] _array) {
		if(_array==null) {
			return true;
		}
		return _array.length==0;
	}

	public static boolean isEmpty(Collection<?> _collection) {
		if(_collection==null) {
			return true;
		}
		return _collection.isEmpty();
	}
	
	public static boolean isEmpty(Map<?, ?> _map) {
		if(_map==null) {
			return true;
		}
		return _map.isEmpty();
	}
	
	public static Map<Object, Object> mapForKey(String key, Collection<?> objects) {
		Map<Object, Object> map=new HashMap<>();
		for(Object object:objects){
			try {
				Object data=FieldUtil.getField(object.getClass(), key).get(object);
				map.put(data, object);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	public static Map<String,Object> mapForKeyValues(String keys,Object... values){
		String keySet[] = keys.split(Constants.DEFFER);
		Assertion.isTrue(keySet.length == values.length, AssertMessage.KEYS_VALUE_PARAM_NOT_VALID);
		Map<String, Object> map = new HashMap<>();
		for (int index = 0; index < keySet.length; index++) {
			map.put(keySet[index], values[index]);
		}
		return map;
	}

	public static void main(String[] args) {
		Integer array[] = { 0, 1, 3 };
		ObjectUtil.reverseArray(array);
		for (int i : array) {
			System.out.println(i);
		}
	}
}
