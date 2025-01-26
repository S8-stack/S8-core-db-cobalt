package com.s8.core.db.cobalt;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.s8.api.exceptions.S8BuildException;
import com.s8.core.arch.silicon.SiliconEngine;
import com.s8.core.bohr.lithium.codebase.LiCodebase;
import com.s8.core.db.cobalt.store.SpaceMgDatabase;
import com.s8.core.io.json.types.JSON_CompilingException;
import com.s8.core.io.xml.annotations.XML_SetElement;
import com.s8.core.io.xml.annotations.XML_Type;


@XML_Type(root=true, name = "Cobalt-config")
public class CoConfiguration {


	public String rootFolderPathname;

	@XML_SetElement(tag = "path")
	public void setRootFolderPathname(String pathname) {
		this.rootFolderPathname = pathname;
	}


	
	/**
	 * 
	 * @param path
	 * @return
	 * @throws JSON_CompilingException 
	 * @throws S8BuildException 
	 * @throws NdBuildException 
	 * @throws BeBuildException 
	 */
	public SpaceMgDatabase create(SiliconEngine ng, Class<?>[] classes) 
			throws JSON_CompilingException, S8BuildException {
		
		LiCodebase codebase = LiCodebase.from(classes); 
		
		Path rootFolderPath = Paths.get(rootFolderPathname);
		
		Path metadataFilePath = SpaceMgDatabase.getMetadataFilePath(rootFolderPath);
		
		boolean isSaved = metadataFilePath.toFile().exists();
		
		return new SpaceMgDatabase(ng, codebase, rootFolderPath, isSaved);	
	}
}
