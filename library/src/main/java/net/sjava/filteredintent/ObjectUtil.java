package net.sjava.filteredintent;

import java.util.List;
import java.util.Map;

/**
 * Created by mcsong@gmail.com on 3/2/16.
 */
public class ObjectUtil {

  public static boolean isNull(Object obj) {
    return obj == null;
  }

  public static boolean isAnyNull(Object obj1, Object obj2) {
    return isNull(obj1) || isNull(obj2);
  }

  public static boolean isNotNull(Object obj) {
    return !isNull(obj);
  }

  public static boolean isEmpty(Object obj) {
    if (obj == null) {
      return true;
    }

    if ((obj instanceof String) && ((String) obj).trim().length() == 0) {
      return true;
    }

    if (obj instanceof Map) {
      return ((Map<?, ?>) obj).isEmpty();
    }

    if (obj instanceof List) {
      return ((List<?>) obj).isEmpty();
    }

    if (obj instanceof Object[] && ((Object[]) obj).length == 0) {
      return true;
    }

    return false;
  }

  public static boolean isNotEmpty(Object obj) {
    return !isEmpty(obj);
  }

  public static boolean isAnyEmpty(Object... objs) {
    for (Object obj : objs) {
      if (isEmpty(obj)) {
        return true;
      }
    }

    return false;
  }

  public static boolean isAllEmpty(Object obj1, Object obj2) {
    return isEmpty(obj1) && isEmpty(obj2);
  }

  public static String toString(Object obj) {
    return obj == null ? "" : obj.toString();
  }

  public static String toString(Object obj, String nullStr) {
    return obj == null ? nullStr : obj.toString();
  }

}
