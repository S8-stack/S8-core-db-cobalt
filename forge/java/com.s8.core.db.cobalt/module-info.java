/**
 * 
 */

import com.s8.meta.modular.S8Module;

@S8Module(target = "S8-core-db-cobalt")
module com.s8.core.db.cobalt {
	
	exports com.s8.core.db.cobalt;
	exports com.s8.core.db.cobalt.entry;
	exports com.s8.core.db.cobalt.store;
	

	requires transitive com.s8.meta;
	requires transitive com.s8.base.io.xml;
	
	requires transitive com.s8.bohr.io.lithium;
	requires transitive com.s8.core.arch.titanium;
	
}