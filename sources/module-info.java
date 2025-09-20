/**
 * 
 */

import com.s8.core.db.cobalt.S8CoreDbCobalt;
import com.s8.meta.modular.S8ModuleDescriptor;


@S8ModuleDescriptor(def = S8CoreDbCobalt.class)
module com.s8.core.db.cobalt {
	
	exports com.s8.core.db.cobalt;
	exports com.s8.core.db.cobalt.entry;
	exports com.s8.core.db.cobalt.store;
	

	requires transitive com.s8.meta;
	requires transitive com.s8.base.io.xml;
	
	requires transitive com.s8.bohr.io.lithium;
	requires transitive com.s8.core.arch.titanium;
	
}