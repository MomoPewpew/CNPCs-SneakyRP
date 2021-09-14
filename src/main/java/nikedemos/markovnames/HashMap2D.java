package nikedemos.markovnames;

import java.util.HashMap;
import java.util.Map;

public class HashMap2D {
	public final Map mMap = new HashMap();

	public Object put(Object key1, Object key2, Object value) {
		Object map;
		if (this.mMap.containsKey(key1)) {
			map = (Map) this.mMap.get(key1);
		} else {
			map = new HashMap();
			this.mMap.put(key1, map);
		}

		return ((Map) map).put(key2, value);
	}

	public Object get(Object key1, Object key2) {
		return this.mMap.containsKey(key1) ? ((Map) this.mMap.get(key1)).get(key2) : null;
	}

	public boolean containsKeys(Object key1, Object key2) {
		return this.mMap.containsKey(key1) && ((Map) this.mMap.get(key1)).containsKey(key2);
	}

	public void clear() {
		this.mMap.clear();
	}
}
